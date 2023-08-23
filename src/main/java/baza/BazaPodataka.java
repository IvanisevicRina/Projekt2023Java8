package baza;

import entitet.*;

import java.io.FileInputStream;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BazaPodataka {
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

    public static List<Zupljanin> dohvatiSveZupljane(){
        List<Zupljanin> listaZupljana = new ArrayList<>();
        try{
            Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPLJANIN");
            while(rs.next()){
                Long id = rs.getLong("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra= rs.getString("sifra");
                LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();

                Zupljanin noviZupljanin = new Zupljanin(id, ime, prezime,sifra,datumRodjenja);
                listaZupljana.add(noviZupljanin);
            }
            con.close();
        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaZupljana;
    }
    public static Optional<Zupljanin> dohvatiZupljane(Long id){
        try{
            Connection con = connectToDatabase();

            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ZUPLJANIN WHERE ID = ?");
            pstmt.setLong(1,id); ////// provjeri jel trebalo 1L?
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra=rs.getString("sifra");
                LocalDate datumRodjenja = rs.getDate("datum_rodjenja").toLocalDate();


                Zupljanin noviZupljanin = new Zupljanin(id, ime, prezime,sifra,datumRodjenja);

                return Optional.of(noviZupljanin);

            }
            con.close();

        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void spremiZupljanina(Zupljanin zupljanin) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO ZUPLJANIN(IME,PREZIME,SIFRA,DATUM_RODJENJA) VALUES(?,?,?,?)");

        pstmt.setString(1,zupljanin.getIme());
        pstmt.setString(2,zupljanin.getPrezime());
        pstmt.setString(3, zupljanin.getSifra());
        pstmt.setDate(4,Date.valueOf(zupljanin.getDatumRodjenja()));

        pstmt.executeUpdate();

        con.close();

    }
    public static void azurirajZupljane(Zupljanin zupljanin) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("UPDATE ZUPLJANIN SET IME = ?, PREZIME = ?, SIFRA =?, DATUM_RODJENJA = ? WHERE ID = ?");

        pstmt.setString(1,zupljanin.getIme());
        pstmt.setString(2,zupljanin.getPrezime());
        pstmt.setString(3, zupljanin.getSifra());
        pstmt.setDate(4,Date.valueOf(zupljanin.getDatumRodjenja()));
        pstmt.setLong(5, zupljanin.getId());

        pstmt.executeUpdate();

        con.close();

    }
    public static void azurirajSvecenika(Svecenik svecenik) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("UPDATE SVECENIK SET IME = ?, PREZIME = ?, SIFRA =?, TITULA = ? WHERE ID = ?");
        pstmt.setString(1,svecenik.getIme());
        pstmt.setString(2,svecenik.getPrezime());
        pstmt.setString(3,svecenik.getSifra());
        pstmt.setString(4,svecenik.getTitula());
        pstmt.setLong(5,svecenik.getId());

        pstmt.executeUpdate();
        con.close();

    }
    public static void obrisiZupljanina(Integer id) throws Exception{
        Connection con = connectToDatabase();



        PreparedStatement stmt1= con.prepareStatement("DELETE FROM SAKRAMENT_ZUPLJANIN WHERE ZUPLJANIN_ID = ?");
        stmt1.setInt(1, id);
        stmt1.executeUpdate();

        PreparedStatement stmt2= con.prepareStatement("DELETE FROM OSOBNI_SAKRAMENT WHERE ZUPLJANIN_ID = ?");
        stmt2.setInt(1, id);
        stmt2.executeUpdate();

        PreparedStatement stmt3= con.prepareStatement("DELETE FROM ZUPLJANIN WHERE ID = ?");
        stmt3.setInt(1, id);
        stmt3.executeUpdate();

        con.close();
    }
    public static void obrisiSvecenika(Integer id) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement stmt5= con.prepareStatement("DELETE FROM SVECENIK WHERE ID = ?");
        stmt5.setInt(1, id);
        stmt5.executeUpdate();

        con.close();
    }

    public static List<Svecenik> dohvatiSveSvecenike(){
        List<Svecenik> listaSvecenika= new ArrayList<>();
        try{
            Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SVECENIK");
            while(rs.next()){
                Long id = rs.getLong("id");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String sifra= rs.getString("sifra");
                String titula= rs.getString("titula");

                Svecenik noviSvecenik = new SvecenikBuilder().setId(id).setSifra(sifra).setIme(ime).setPrezime(prezime).setTitula(titula).createSvecenik();

                listaSvecenika.add(noviSvecenik);
            }
            con.close();
        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaSvecenika;
    }
    public static Set<Zupljanin> dohvatiZupljaneSakramenta(Long id_Sakramenta){
        Set<Zupljanin> zupljaninSet = new HashSet<>();
        try{
            Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAKRAMENT_ZUPLJANIN WHERE SAKRAMENT_ID = ?");
            pstmt.setLong(1,id_Sakramenta);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                List<Zupljanin> listaZupljana = BazaPodataka.dohvatiSveZupljane();
                Integer zupljanin_id = rs.getInt("zupljanin_id");

                for (Zupljanin zupljanin:listaZupljana) {
                    if(zupljanin_id.longValue() ==zupljanin.getId()){
                        zupljaninSet.add(zupljanin);
                    }

                }

            }
            con.close();
        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return zupljaninSet;
    }

    public static void spremiSvecenika(Svecenik svecenik) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SVECENIK(IME,PREZIME,SIFRA,TITULA) VALUES(?,?,?,?)");

        pstmt.setString(1,svecenik.getIme());
        pstmt.setString(2,svecenik.getPrezime());
        pstmt.setString(3,svecenik.getSifra());
        pstmt.setString(4,svecenik.getTitula());


        pstmt.executeUpdate();

        con.close();
    }
    public static void spremiSakrament(Sakrament sakrament) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SAKRAMENT(SIFRA,NAZIV) VALUES(?,?)");

        pstmt.setString(1,sakrament.getSifra());
        pstmt.setString(2,sakrament.getNaziv());

        pstmt.executeUpdate();
        con.close();

    }

    public static void spojiZupljaninaNaSakrament(Integer sakrament_id, Integer zupljanin_id) throws Exception {
        Connection con = connectToDatabase();
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO SAKRAMENT_ZUPLJANIN(SAKRAMENT_ID, ZUPLJANIN_ID) VALUES(?,?)");

        pstmt.setInt(1,sakrament_id);
        pstmt.setInt(2,zupljanin_id);
        pstmt.executeUpdate();

        con.close();
    }

    public static List<Sakrament> dohvatiSveSakramente(){
        List<Sakrament> listaSakramenata = new ArrayList<>();
        try{
            Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SAKRAMENT");
            while(rs.next()){
                Long idSakramenta = rs.getLong("id");
                String sifra= rs.getString("sifra");
                String naziv = rs.getString("naziv");



                Set<Zupljanin> setOdabranihZupljana =BazaPodataka.dohvatiZupljaneSakramenta(idSakramenta);


                Sakrament noviSakrament = new Sakrament(idSakramenta,sifra,naziv,setOdabranihZupljana,liturgija(1));
                listaSakramenata.add(noviSakrament);
            }
            con.close();
        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return listaSakramenata;

    }
    public static Sakrament dohvatiSakrament(Long idSakramenta){
        try{
            Connection con = connectToDatabase();

            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAKRAMENT WHERE ID = ?");
            pstmt.setLong(1,idSakramenta);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                String sifra= rs.getString("sifra");
                String naziv = rs.getString("naziv");
                Integer liturgijsko_razdoblje = rs.getInt("liturgijsko_razdoblje");

                Set<Zupljanin> setOdabranihZupljana =BazaPodataka.dohvatiZupljaneSakramenta(idSakramenta);

                Sakrament noviSakrament = new Sakrament(idSakramenta, sifra, naziv,setOdabranihZupljana,liturgija(liturgijsko_razdoblje));
                return noviSakrament;

            }
            con.close();

        }catch(Exception e){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            e.printStackTrace();
        }
        return null;
    }

    public static void spremiOsobniSakrament(OsobniSakrament osobniSakrament) throws Exception{
        Connection con = connectToDatabase();

        PreparedStatement pstmt = con.prepareStatement("INSERT INTO OSOBNI_SAKRAMENT(SAKRAMENT_ID, ZUPLJANIN_ID, DATUM_I_VRIJEME) VALUES(?,?,?)");
        pstmt.setInt(1,osobniSakrament.getSakrament().getId().intValue());
        pstmt.setInt(2,osobniSakrament.getZupljanin().getId().intValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss.SS");
        osobniSakrament.getDatumIVrijeme().format(formatter);
        pstmt.setTimestamp(3,Timestamp.valueOf(osobniSakrament.getDatumIVrijeme()));

        pstmt.executeUpdate();

        con.close();
    }

    public static List<OsobniSakrament> dohvatiSveOsobneSakramente(){
        List<OsobniSakrament> listaOsobnihSakramenata = new ArrayList<>();
        try{Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBNI_SAKRAMENT");
            while(rs.next()){
                Integer id = rs.getInt("id");
                Integer sakrament_id = rs.getInt("sakrament_id");
                Integer zupljanin_id = rs.getInt("zupljanin_id");
                LocalDateTime datumIvrijeme= rs.getTimestamp("datum_i_vrijeme").toLocalDateTime();
                List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
                Sakrament ovajSakrament=sakramenti.get(0);
                for (Sakrament sakrament:sakramenti) {
                    if(sakrament.getId() == sakrament_id.longValue()){
                        ovajSakrament=sakrament;
                    }
                }
                List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();
                Zupljanin ovajZupljanin = zupljani.get(0);
                for(Zupljanin zupljanin : zupljani){
                    if(zupljanin.getId() == zupljanin_id.longValue()){
                        ovajZupljanin=zupljanin;
                    }
                }
                OsobniSakrament noviOsobniSakrament = new OsobniSakrament(id.longValue(),ovajSakrament,ovajZupljanin,datumIvrijeme,new Crkva("sveti Roko"));
                listaOsobnihSakramenata.add(noviOsobniSakrament);
            }
            con.close();

        }catch(Exception ex){
            System.out.println("Došlo je do pogreške kod spajanja na bazu podataka!");
            ex.printStackTrace();

        }
        return listaOsobnihSakramenata;
    }






}
