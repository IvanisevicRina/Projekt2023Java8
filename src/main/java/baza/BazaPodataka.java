package baza;

import com.example.projekt2023java.PrikazPromjenaController;
import entitet.*;
import niti.NotificationManager;
import niti.WriteAChangeThread;

import java.io.*;
import java.security.spec.ECField;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static niti.ThreadColor.ANSI_GREEN;

public class BazaPodataka {
    private static PrikazPromjenaController prikazPromjenaController;

    public BazaPodataka(PrikazPromjenaController prikazPromjenaController) {
        this.prikazPromjenaController = prikazPromjenaController;
    }

    private static Connection connectToDatabase() throws Exception {
        Properties konfiguracijaBaze = new Properties();
        konfiguracijaBaze.load(new FileInputStream("dat/bazaPodataka.properties"));

        Connection con = DriverManager.getConnection(
                konfiguracijaBaze.getProperty("bazaPodatakaUrl"),
                konfiguracijaBaze.getProperty("korisnickoIme"),
                konfiguracijaBaze.getProperty("lozinka"));
        return con;
    }

    public static LiturgijskoRazdoblje liturgija(int broj_liturgije) {

        return switch (broj_liturgije) {
            case 1 -> LiturgijskoRazdoblje.DOSASCE;
            case 2 -> LiturgijskoRazdoblje.KORIZMA;
            default -> LiturgijskoRazdoblje.OSTATAK;
        };

    }

    private static String getUserKorisnickoIme() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[0]; // Return the role part
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[1]; // Return the role part
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeChangeToBinaryFile(Promjene<?, ?> promjena) {
        WriteAChangeThread writeThread = new WriteAChangeThread(promjena);
        System.out.println(ANSI_GREEN + "Pozdrav iz baze, trenutno smo u procesu dodavanja promjene, čekaj da te obavjestim kad je gotovo");
        writeThread.start();
    }

    public static List<Zupljanin> dohvatiSveZupljane() {
        List<Zupljanin> listaZupljana = new ArrayList<>();
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPLJANIN");
            while (rs.next()) {
                Long id = rs.getLong("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra = rs.getString("sifra");
                LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();

                Zupljanin noviZupljanin = new Zupljanin(id, ime, prezime, sifra, datumRodjenja);
                listaZupljana.add(noviZupljanin);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaZupljana;
    }

    public static Zupljanin dohvatiZupljana(Long id) {
        try {
            Connection con = connectToDatabase();

            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ZUPLJANIN WHERE ID = ?");
            pstmt.setLong(1, id); //////
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra = rs.getString("sifra");
                LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();


                Zupljanin noviZupljanin = new Zupljanin(id, ime, prezime, sifra, datumRodjenja);

                return noviZupljanin;

            }
            con.close();

        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return new ZupljaninBuilder().createZupljanin();

    }

    public static int spremiZupljanina(Zupljanin zupljanin) throws Exception {

        try (Connection con = connectToDatabase()) {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO ZUPLJANIN(IME,PREZIME,SIFRA,DATUM_RODJENJA) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, zupljanin.getIme());
            pstmt.setString(2, zupljanin.getPrezime());
            pstmt.setString(3, zupljanin.getSifra());
            pstmt.setDate(4, Date.valueOf(zupljanin.getDatumRodjenja()));

            pstmt.executeUpdate();

            String promjenaOpis = "Dodan novi zupljanin: " + zupljanin.getPrezime() + " " + zupljanin.getIme();
            String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
            LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

            Promjene<String, Zupljanin> novaPromjena = new Promjene<>(
                    null, zupljanin.toString(), zupljanin,
                    promjenaOpis, promjenaRola, formattedDatumIVrijeme
            );

            writeChangeToBinaryFile(novaPromjena);

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                con.close();
                return generatedId;
            } else {
                con.close();
                throw new SQLException("Failed to retrieve the generated ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public static void azurirajZupljane(Zupljanin zupljanin) throws Exception {
        Connection con = connectToDatabase();
        Zupljanin stariZupljanin = new ZupljaninBuilder().createZupljanin();
        List<Zupljanin> zupljaninList = BazaPodataka.dohvatiSveZupljane();
        for (Zupljanin zupljanin1 : zupljaninList) {
            if (Objects.equals(zupljanin.getId(), zupljanin1.getId())) {
                stariZupljanin = zupljanin1;
                System.out.println("nasao sam ga");
            }

        }
        PreparedStatement pstmt = con.prepareStatement("UPDATE ZUPLJANIN SET IME = ?, PREZIME = ?, SIFRA =?, DATUM_RODJENJA = ? WHERE ID = ?");

        pstmt.setString(1, zupljanin.getIme());
        pstmt.setString(2, zupljanin.getPrezime());
        pstmt.setString(3, zupljanin.getSifra());
        pstmt.setDate(4, Date.valueOf(zupljanin.getDatumRodjenja()));
        pstmt.setLong(5, zupljanin.getId());

        pstmt.executeUpdate();
        String promjenaOpis = "Azuriran zupljanin: " + zupljanin.getPrezime() + " " + zupljanin.getIme() + ":";

        Integer brojPromjena = 0;
        String staraVrijednost = "";
        String novaVrijednost = "";
        if (!Objects.equals(zupljanin.getSifra(), stariZupljanin.getSifra())) {
            promjenaOpis = promjenaOpis + " šifra ";
            staraVrijednost = staraVrijednost + "  " + stariZupljanin.getSifra();
            novaVrijednost = novaVrijednost + " " + zupljanin.getSifra();
            brojPromjena = 1;
        }
        if (!Objects.equals(zupljanin.getPrezime(), stariZupljanin.getPrezime())) {
            promjenaOpis = promjenaOpis + " prezime ";
            staraVrijednost = staraVrijednost + "  " + stariZupljanin.getPrezime();
            novaVrijednost = novaVrijednost + " " + zupljanin.getPrezime();
            brojPromjena = 1;
        }
        if (!Objects.equals(zupljanin.getIme(), stariZupljanin.getIme())) {
            promjenaOpis = promjenaOpis + " ime ";
            staraVrijednost = staraVrijednost + "  " + stariZupljanin.getIme();
            novaVrijednost = novaVrijednost + " " + zupljanin.getIme();
            brojPromjena = 1;

        }

        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, Zupljanin> novaPromjena = new Promjene<>(
                staraVrijednost, novaVrijednost, zupljanin,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );
        if (brojPromjena != 0) {
            writeChangeToBinaryFile(novaPromjena);
        }

        con.close();

    }

    public static void azurirajSvecenika(Svecenik svecenik) throws Exception {
        Connection con = connectToDatabase();

        Svecenik stariSvecenik = new SvecenikBuilder().createSvecenik();
        List<Svecenik> svecenikList = BazaPodataka.dohvatiSveSvecenike();
        for (Svecenik svecenik1 : svecenikList) {
            if (Objects.equals(svecenik.getId(), svecenik1.getId())) {
                stariSvecenik = svecenik1;
                System.out.println("------------nasao sam ga-------");
            }

        }
        PreparedStatement pstmt = con.prepareStatement("UPDATE SVECENIK SET IME = ?, PREZIME = ?, SIFRA =?, TITULA = ? WHERE ID = ?");
        pstmt.setString(1, svecenik.getIme());
        pstmt.setString(2, svecenik.getPrezime());
        pstmt.setString(3, svecenik.getSifra());
        pstmt.setString(4, svecenik.getTitula());
        pstmt.setLong(5, svecenik.getId());

        pstmt.executeUpdate();

        String promjenaOpis = "Azuriran svecenik: " + svecenik.getPrezime() + " " + svecenik.getIme() + ":";
        System.out.println("Svecenik: " + stariSvecenik.getPrezime() + " " + stariSvecenik.getIme() + ":");
        System.out.println("Novisvecenik: " + svecenik.getPrezime() + " " + svecenik.getIme() + ":");

        Integer brojPromjena = 0;
        String staraVrijednost = "";
        String novaVrijednost = "";
        if (!Objects.equals(svecenik.getSifra(), stariSvecenik.getSifra())) {
            promjenaOpis = promjenaOpis + " šifra ";
            staraVrijednost = staraVrijednost + "  " + stariSvecenik.getSifra();
            novaVrijednost = novaVrijednost + " " + svecenik.getSifra();
            System.out.println("drugacija sifra");
            brojPromjena = 1;
        }
        if (!Objects.equals(svecenik.getPrezime(), stariSvecenik.getPrezime())) {
            promjenaOpis = promjenaOpis + " prezime ";
            staraVrijednost = staraVrijednost + "  " + stariSvecenik.getPrezime();
            novaVrijednost = novaVrijednost + " " + svecenik.getPrezime();
            brojPromjena = 1;
            System.out.println("drugacije prezime");
        }
        if (!Objects.equals(svecenik.getIme(), stariSvecenik.getIme())) {
            staraVrijednost = staraVrijednost + "  " + stariSvecenik.getIme();
            novaVrijednost = novaVrijednost + " " + svecenik.getIme();
            promjenaOpis = promjenaOpis + " ime ";
            brojPromjena = 1;
            System.out.println("drugacije ime");

        }
        if (!Objects.equals(svecenik.getTitula(), stariSvecenik.getTitula())) {
            staraVrijednost = staraVrijednost + "  " + stariSvecenik.getTitula();
            novaVrijednost = novaVrijednost + " " + svecenik.getTitula();
            promjenaOpis = promjenaOpis + " titula ";
            brojPromjena = 1;
            System.out.println("drugacija Titula");

        }

        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, Svecenik> novaPromjena = new Promjene<>(
                staraVrijednost, novaVrijednost, svecenik,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );
        if (brojPromjena != 0) {
            writeChangeToBinaryFile(novaPromjena);
        }

        con.close();

    }
    public static void azurirajOsobniSakrament(OsobniSakrament noviOsobniSakrament) throws Exception {
        Connection con = connectToDatabase();

        // Find the old OsobniSakrament based on ID
        List<OsobniSakrament> sviOsobniSakramenti = BazaPodataka.dohvatiSveOsobneSakramente();
        OsobniSakrament stariOsobniSakrament = null;
        for (OsobniSakrament osobniSakrament : sviOsobniSakramenti) {
            if (Objects.equals(noviOsobniSakrament.getId(), osobniSakrament.getId())) {
                stariOsobniSakrament = osobniSakrament;
                break;
            }
        }

        if (stariOsobniSakrament == null) {
            System.out.println("Osobni sakrament s ID " + noviOsobniSakrament.getId() + " nije pronađen.");
            con.close();
            return;
        }

        // Update the OsobniSakrament in the database
        PreparedStatement pstmt = con.prepareStatement("UPDATE OSOBNI_SAKRAMENT SET  DATUM_I_VRIJEME = ?, LITURGIJA = ?, CRKVA = ? WHERE ID = ?");
        pstmt.setTimestamp(1, Timestamp.valueOf(noviOsobniSakrament.getDatumIVrijeme()));
        pstmt.setString(2, noviOsobniSakrament.getLiturgijskoRazdoblje().toString());
        pstmt.setString(3, noviOsobniSakrament.getCrkva().nazivCrkve());
        pstmt.setInt(4, noviOsobniSakrament.getId().intValue());

        pstmt.executeUpdate();

        // Generate and write change log
        String promjenaOpis = "Azuriran osobni sakrament za: " + stariOsobniSakrament.getZupljanin().getIme() + " " + stariOsobniSakrament.getZupljanin().getPrezime();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);
        Integer brojPromjena = 0;
        String staraVrijednost = "";
        String novaVrijednost = "";

        if (!Objects.equals(noviOsobniSakrament.getDatumIVrijeme(), stariOsobniSakrament.getDatumIVrijeme())) {
            promjenaOpis += " DatumIvrijeme ";
            staraVrijednost += " " + stariOsobniSakrament.getDatumIVrijeme().format(formatter);
            novaVrijednost += " " + noviOsobniSakrament.getDatumIVrijeme().format(formatter);
            brojPromjena++;
        }

        if (!Objects.equals(noviOsobniSakrament.getLiturgijskoRazdoblje(), stariOsobniSakrament.getLiturgijskoRazdoblje())) {
            promjenaOpis += " Liturgijsko razdoblje ";
            staraVrijednost += " " + stariOsobniSakrament.getLiturgijskoRazdoblje().name();
            novaVrijednost += " " + noviOsobniSakrament.getLiturgijskoRazdoblje().name();
            brojPromjena++;
        }

        if (!Objects.equals(noviOsobniSakrament.getCrkva(), stariOsobniSakrament.getCrkva())) {
            promjenaOpis += " Crkva ";
            staraVrijednost += " " + stariOsobniSakrament.getCrkva().nazivCrkve();
            novaVrijednost += " " + noviOsobniSakrament.getCrkva().nazivCrkve();
            brojPromjena++;
        }
        if (brojPromjena != 0) {
            Promjene<String, OsobniSakrament> novaPromjena = new Promjene<>(
                    staraVrijednost, novaVrijednost, noviOsobniSakrament,
                    promjenaOpis, promjenaRola, formattedDatumIVrijeme
            );

            writeChangeToBinaryFile(novaPromjena);
        }

        con.close();
    }

    public static void obrisiZupljanina(Integer id) throws Exception {
        Connection con = connectToDatabase();
        List<Zupljanin> zupljaniList = BazaPodataka.dohvatiSveZupljane();
        Zupljanin zupljanin = new ZupljaninBuilder().createZupljanin();
        for (Zupljanin zupljanink : zupljaniList) {
            if (zupljanink.getId() == id.longValue()) {
                zupljanin = zupljanink;
            }

        }
        String promjenaOpis = "Izbrisan župljanin: " + zupljanin.getPrezime() + " " + zupljanin.getIme();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, Zupljanin> novaPromjena = new Promjene<>(
                null, zupljanin.toString(), zupljanin,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );

        PreparedStatement stmt0 = con.prepareStatement("DELETE FROM ZUPLJANINSLIKE WHERE ZUPLJANIN_ID = ?");
        stmt0.setInt(1, id);
        stmt0.executeUpdate();


        PreparedStatement stmt1 = con.prepareStatement("DELETE FROM SAKRAMENT_ZUPLJANIN WHERE ZUPLJANIN_ID = ?");
        stmt1.setInt(1, id);
        stmt1.executeUpdate();

        PreparedStatement stmt2 = con.prepareStatement("DELETE FROM OSOBNI_SAKRAMENT WHERE ZUPLJANIN_ID = ?");
        stmt2.setInt(1, id);
        stmt2.executeUpdate();

        PreparedStatement stmt3 = con.prepareStatement("DELETE FROM ZUPLJANIN WHERE ID = ?");
        stmt3.setInt(1, id);
        stmt3.executeUpdate();
        writeChangeToBinaryFile(novaPromjena);


        con.close();
    }

    public static void obrisiSvecenika(Integer id) throws Exception {
        Connection con = connectToDatabase();

        List<Svecenik> svecenikList = BazaPodataka.dohvatiSveSvecenike();
        Svecenik svecenik = new SvecenikBuilder().createSvecenik();
        for (Svecenik sveceniks : svecenikList) {
            if (sveceniks.getId() == id.longValue()) {
                svecenik = sveceniks;
            }

        }
        String promjenaOpis = "Izbrisan svećenik: " + svecenik.getPrezime() + " " + svecenik.getIme();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, Svecenik> novaPromjena = new Promjene<>(
                null, svecenik.toString(), svecenik,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );


        PreparedStatement stmt5 = con.prepareStatement("DELETE FROM SVECENIK WHERE ID = ?");
        stmt5.setInt(1, id);
        stmt5.executeUpdate();

        writeChangeToBinaryFile(novaPromjena);


        con.close();


    }

    public static List<Svecenik> dohvatiSveSvecenike() {
        List<Svecenik> listaSvecenika = new ArrayList<>();
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SVECENIK");
            while (rs.next()) {
                Long id = rs.getLong("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra = rs.getString("sifra");
                String titula = rs.getString("titula");

                Svecenik noviSvecenik = new SvecenikBuilder().setId(id).setSifra(sifra).setIme(ime).setPrezime(prezime).setTitula(titula).createSvecenik();

                listaSvecenika.add(noviSvecenik);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaSvecenika;
    }

    public static Set<Zupljanin> dohvatiZupljaneSakramenta(Long id_Sakramenta) {
        Set<Zupljanin> zupljaninSet = new HashSet<>();
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAKRAMENT_ZUPLJANIN WHERE SAKRAMENT_ID = ?");
            pstmt.setLong(1, id_Sakramenta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                List<Zupljanin> listaZupljana = BazaPodataka.dohvatiSveZupljane();
                Integer zupljanin_id = rs.getInt("zupljanin_id");

                for (Zupljanin zupljanin : listaZupljana) {
                    if (zupljanin_id.longValue() == zupljanin.getId()) {
                        zupljaninSet.add(zupljanin);
                    }

                }

            }
            con.close();
        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return zupljaninSet;
    }

    public static void spremiSvecenika(Svecenik svecenik) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SVECENIK(IME,PREZIME,SIFRA,TITULA) VALUES(?,?,?,?)");

        pstmt.setString(1, svecenik.getIme());
        pstmt.setString(2, svecenik.getPrezime());
        pstmt.setString(3, svecenik.getSifra());
        pstmt.setString(4, svecenik.getTitula());

        pstmt.executeUpdate();

        String promjenaOpis = "Dodan novi svećenik: " + svecenik.getPrezime() + " " + svecenik.getIme();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, Svecenik> novaPromjena = new Promjene<>(
                null, svecenik.toString(), svecenik,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );

        writeChangeToBinaryFile(novaPromjena);


        con.close();
    }

    public static void spremiSakrament(Sakrament sakrament) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SAKRAMENT(SIFRA,NAZIV) VALUES(?,?)");

        pstmt.setString(1, sakrament.getSifra());
        pstmt.setString(2, sakrament.getNaziv());

        pstmt.executeUpdate();
        con.close();

    }

    public static void spojiZupljaninaNaSakrament(Integer sakrament_id, Integer zupljanin_id) throws Exception {
        Connection con = connectToDatabase();
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SAKRAMENT_ZUPLJANIN(SAKRAMENT_ID, ZUPLJANIN_ID) VALUES(?,?)");

        pstmt.setInt(1, sakrament_id);
        pstmt.setInt(2, zupljanin_id);
        pstmt.executeUpdate();

        Sakrament sakrament=BazaPodataka.dohvatiSakrament(sakrament_id.longValue());
        Zupljanin zupljanin=BazaPodataka.dohvatiZupljana(zupljanin_id.longValue());


        String promjenaOpis = "Dodan župljanin: " + zupljanin.getIme() + " "+zupljanin.getPrezime() + " na sakrament " + sakrament.getNaziv();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, String> novaPromjena = new Promjene<>(
                null, zupljanin.getIme() + " "+zupljanin.getPrezime() + "("+sakrament.getNaziv()+")", sakrament.getClass().getName()+"-"+zupljanin.getClass().getName(),
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );

        writeChangeToBinaryFile(novaPromjena);
        con.close();
    }


    public static void odspojiZupljaninaOdSakramenta(Integer sakrament_id, Integer zupljanin_id) throws Exception {
        Connection con = connectToDatabase();


        PreparedStatement deleteStmt1 = con.prepareStatement("DELETE FROM OSOBNI_SAKRAMENT WHERE SAKRAMENT_ID = ? AND ZUPLJANIN_ID = ?");
        deleteStmt1.setInt(1, sakrament_id);
        deleteStmt1.setInt(2, zupljanin_id);
        deleteStmt1.executeUpdate();


        PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM SAKRAMENT_ZUPLJANIN WHERE SAKRAMENT_ID = ? AND ZUPLJANIN_ID = ?");
        deleteStmt.setInt(1, sakrament_id);
        deleteStmt.setInt(2, zupljanin_id);
        deleteStmt.executeUpdate();

        Sakrament sakrament=BazaPodataka.dohvatiSakrament(sakrament_id.longValue());
        Zupljanin zupljanin=BazaPodataka.dohvatiZupljana(zupljanin_id.longValue());


        String promjenaOpis = "Obrisan: " + zupljanin.getIme() + " "+zupljanin.getPrezime() + "sa sakramenta " + sakrament.getNaziv();
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, String> novaPromjena = new Promjene<>(
                zupljanin.getIme() + " "+zupljanin.getPrezime() + "("+sakrament.getNaziv()+")", null, sakrament.getClass().getName()+"-"+zupljanin.getClass().getName(),
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );

        writeChangeToBinaryFile(novaPromjena);


        con.close();
    }


    public static List<Sakrament> dohvatiSveSakramente() {
        List<Sakrament> listaSakramenata = new ArrayList<>();
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SAKRAMENT");
            while (rs.next()) {
                Long idSakramenta = rs.getLong("id");
                String sifra = rs.getString("sifra");
                String naziv = rs.getString("naziv");


                Set<Zupljanin> setOdabranihZupljana = BazaPodataka.dohvatiZupljaneSakramenta(idSakramenta);


                Sakrament noviSakrament = new Sakrament(idSakramenta, sifra, naziv, setOdabranihZupljana);
                listaSakramenata.add(noviSakrament);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaSakramenata;

    }

    public static Sakrament dohvatiSakrament(Long idSakramenta) {
        try {
            Connection con = connectToDatabase();

            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAKRAMENT WHERE ID = ?");
            pstmt.setLong(1, idSakramenta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String sifra = rs.getString("sifra");
                String naziv = rs.getString("naziv");

                Set<Zupljanin> setOdabranihZupljana = BazaPodataka.dohvatiZupljaneSakramenta(idSakramenta);

                Sakrament noviSakrament = new Sakrament(idSakramenta, sifra, naziv, setOdabranihZupljana);
                return noviSakrament;

            }
            con.close();

        } catch (Exception e) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return null;
    }

    public static void spremiOsobniSakrament(OsobniSakrament osobniSakrament) throws Exception {
        try {
            Connection con = connectToDatabase();

            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO OSOBNI_SAKRAMENT(SAKRAMENT_ID, ZUPLJANIN_ID, DATUM_I_VRIJEME,LITURGIJA,CRKVA) VALUES(?,?,?,?,?)");
        pstmt.setInt(1, osobniSakrament.getSakrament().getId().intValue());
        pstmt.setInt(2, osobniSakrament.getZupljanin().getId().intValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        osobniSakrament.getDatumIVrijeme().format(formatter);
        pstmt.setTimestamp(3, Timestamp.valueOf(osobniSakrament.getDatumIVrijeme()));
        pstmt.setString(4, osobniSakrament.getLiturgijskoRazdoblje().toString());
        pstmt.setString(5, osobniSakrament.getCrkva().nazivCrkve());

        pstmt.executeUpdate();
        String promjenaOpis = "Dodan novi osobni sakrament za zupljanina: " + osobniSakrament.getZupljanin().getIme() + osobniSakrament.getZupljanin().getPrezime() + " (" + osobniSakrament.getSakrament().getNaziv() + ")";
        String promjenaRola = getUserKorisnickoIme() + " - " + getUserRole();
        LocalDateTime promjenaDatumIVrijeme = LocalDateTime.now();

        String formattedDatumIVrijeme = promjenaDatumIVrijeme.format(formatter);

        Promjene<String, OsobniSakrament> novaPromjena = new Promjene<>(
                null, osobniSakrament.toString(), osobniSakrament,
                promjenaOpis, promjenaRola, formattedDatumIVrijeme
        );

        writeChangeToBinaryFile(novaPromjena);
        con.close();

    } catch (Exception e) {
        System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
        e.printStackTrace();
    }
    }

    public static void obrisiOsobniSakrament(Integer osobniSakramentId) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM OSOBNI_SAKRAMENT WHERE ID = ?");
        deleteStmt.setInt(1, osobniSakramentId);
        deleteStmt.executeUpdate();
        System.out.println("Osobni sakrament je uspješno obrisan.");


        con.close();
    }


    public static List<OsobniSakrament> dohvatiSveOsobneSakramente() throws Exception{
        List<OsobniSakrament> listaOsobnihSakramenata = new ArrayList<>();
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBNI_SAKRAMENT");
            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer sakrament_id = rs.getInt("sakrament_id");
                Integer zupljanin_id = rs.getInt("zupljanin_id");
                LocalDateTime datumIvrijeme = rs.getTimestamp("datum_i_vrijeme").toLocalDateTime();
                String crkva = rs.getString("crkva");
                String liturgijaSakramentaString = rs.getString("liturgija");
                LiturgijskoRazdoblje liturgijskoRazdoblje = convertStringToEnum(liturgijaSakramentaString);

                List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
                Sakrament ovajSakrament = sakramenti.get(0);
                for (Sakrament sakrament : sakramenti) {
                    if (sakrament.getId() == sakrament_id.longValue()) {
                        ovajSakrament = sakrament;
                    }
                }
                List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();
                Zupljanin ovajZupljanin = zupljani.get(0);
                for (Zupljanin zupljanin : zupljani) {
                    if (zupljanin.getId() == zupljanin_id.longValue()) {
                        ovajZupljanin = zupljanin;
                    }
                }
                OsobniSakrament noviOsobniSakrament = new OsobniSakrament(id.longValue(), ovajSakrament, ovajZupljanin, datumIvrijeme, new Crkva(crkva), liturgijskoRazdoblje);
                listaOsobnihSakramenata.add(noviOsobniSakrament);
            }
            con.close();

        } catch (Exception ex) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            ex.printStackTrace();

        }
        return listaOsobnihSakramenata;
    }


    public static void spremiSliku(String nazivSlike, byte[] slikaBytes) throws Exception{
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO Slike (naziv, slika) VALUES (?, ?)");
            pstmt.setString(1,nazivSlike);
            pstmt.setBytes(2, slikaBytes);
            pstmt.executeUpdate();

            con.close();

        }catch (Exception ex) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            ex.printStackTrace();

        }
    }
    public static void spremiSlikuZupljanina(Integer zupljanin_id, String nazivSlike, byte[] slikaBytes) throws Exception{
        try {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO ZupljaninSlike (zupljanin_id, naziv, slika) VALUES (?, ?, ?)");

            pstmt.setInt(1,zupljanin_id);
            pstmt.setString(2,nazivSlike);
            pstmt.setBytes(3, slikaBytes);

            pstmt.executeUpdate();

            con.close();
        }catch (Exception ex) {
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            ex.printStackTrace();

        }
    }
    public static void azurirajZupljanineSlike(Zupljanin zupljanin) {
        try  {
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }

            PreparedStatement statement = con.prepareStatement("UPDATE Zupljanin SET slike = ? WHERE id = ?");

            // Pretvorba liste slika u byte[] i postavljanje parametara
            byte[] slikeBytes = pretvoriListuSlikaUByteArray(zupljanin.getSlike());
            statement.setBytes(1, slikeBytes);
            statement.setLong(2, zupljanin.getId());

            statement.executeUpdate();

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<byte[]>  dohvatiSlikeZupljanina(Zupljanin zupljanin){
        List<byte[]> imagesList = new ArrayList<>();

        try{
            Connection con = connectToDatabase();
            if (con != null) {
                System.out.println("Uspješno smo se spojili na bazu!");
            }


            PreparedStatement statement = con.prepareStatement("SELECT slika FROM ZupljaninSlike WHERE zupljanin_id = ?");

            statement.setLong(1, zupljanin.getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                imagesList.add(slikaBytes);
            }

            con.close();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return imagesList;


    }
    public static byte[] pretvoriListuSlikaUByteArray(List<Slike> slike) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(slike);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
    public static LiturgijskoRazdoblje convertStringToEnum(String input) {
        for (LiturgijskoRazdoblje razdoblje : LiturgijskoRazdoblje.values()) {
            if (razdoblje.name().equals(input)) {
                return razdoblje;
            }
        }
        return null;
    }


    public static void fixAutoicrementaZupljnaId(Long id) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("ALTER TABLE ZUPLJANIN ALTER COLUMN ID RESTART WITH ?");
        pstmt.setInt(1, id.intValue());
        pstmt.executeUpdate();

        con.close();

    }

    public static void fixAutoicrementaSvecenikaId(Long id) throws Exception {
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("ALTER TABLE SVECENIK ALTER COLUMN ID RESTART WITH ?");
        pstmt.setInt(1, id.intValue());
        pstmt.executeUpdate();

        con.close();

    }





}
