package util;


import entitet.*;
import sortiranje.ZupljaninSorter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Datoteke {

    private static final int BROJ_ZAPISA_SVECENIKA = 6;
    private static final int BROJ_ZAPISA_SAKRAMENATA = 6;
    private static final int BROJ_ZAPISA_ZUPLJANA = 5;
    private static final int BROJ_ZAPISA_OSOBNIH_SAKRAMENATA = 5;
    private static final int BROJ_ZAPISA_ZUPA = 6;


    public static LiturgijskoRazdoblje liturgija(int broj_liturgije) {

        return switch (broj_liturgije) {
            case 1 -> LiturgijskoRazdoblje.DOSASCE;
            case 2 -> LiturgijskoRazdoblje.KORIZMA;
            default -> LiturgijskoRazdoblje.OSTATAK;
        };

    }

    public static List<Svecenik> dohvatiSvecenike() {
        List<Svecenik> svecenici = new ArrayList<>();
        try (BufferedReader bufReaderSvecenika = new BufferedReader(new FileReader(new File("dat/svecenici.txt")))) {

            List<String> datotekaSvecenika = bufReaderSvecenika.lines().collect(Collectors.toList());

            for (int i = 0; i < datotekaSvecenika.size() / BROJ_ZAPISA_SVECENIKA; i++) {
                String idString = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA);
                Long id = Long.parseLong(idString);
                String ime = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA + 1);
                String prezime = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA + 2);
                String sifra = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA + 3);
                String titula = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA + 4);
                String datumString = datotekaSvecenika.get(i * BROJ_ZAPISA_SVECENIKA + 5);
                LocalDate datumRodjenja = LocalDate.parse(datumString, DateTimeFormatter.ofPattern("dd.MM.yyyy."));

                svecenici.add(new SvecenikBuilder().setId(id).setIme(ime).setPrezime(prezime).setSifra(sifra).setTitula(titula).setDatumRodjenja(datumRodjenja).createSvecenik());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return svecenici;
    }


    public static void unosSvecenika(Scanner unos, List<Svecenik> svecenici) {
        for (int i = 0; i < BROJ_ZAPISA_SVECENIKA; i++) {
            System.out.println("Unesite " + (i + 1) + ". svecenika:");
            System.out.print("Unesite šifru svecenika: ");
            String sifra = unos.nextLine();

            System.out.print("Unesite ime svecenika: ");
            String ime = unos.nextLine();

            System.out.print("Unesite prezime svecenika: ");
            String prezime = unos.nextLine();

            System.out.print("Unesite titulu svecenika: ");
            String titula = unos.nextLine();

            System.out.print("Unesite datum rođenja svecenika " + prezime + " " + ime + " u formatu (dd.MM.yyyy.):");
            String datumRodjenjaString = unos.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            LocalDate datumRodjenja = LocalDate.parse(datumRodjenjaString, formatter);


            svecenici.add(new SvecenikBuilder().setIme(ime).setPrezime(prezime).setSifra(sifra).setTitula(titula).setDatumRodjenja(datumRodjenja).createSvecenik());

        }


    }


    public static List<Zupljanin> dohvatiZupljane() {
        List<Zupljanin> zupljani = new ArrayList<>();
        try (BufferedReader bufReaderZupljana = new BufferedReader(new FileReader(new File("dat/zupljani.txt")))) {

            List<String> datotekaZupljana = bufReaderZupljana.lines().collect(Collectors.toList());
            for(int i=0;i<datotekaZupljana.size()/BROJ_ZAPISA_ZUPLJANA;i++ ) {
                String idZupljanaString = datotekaZupljana.get(i*BROJ_ZAPISA_ZUPLJANA);
                Long id = Long.parseLong(idZupljanaString);
                String ime = datotekaZupljana.get(i*BROJ_ZAPISA_ZUPLJANA+1);
                String prezime = datotekaZupljana.get(i*BROJ_ZAPISA_ZUPLJANA+2);
                String sifra =  datotekaZupljana.get(i*BROJ_ZAPISA_ZUPLJANA+3);
                String datumString =  datotekaZupljana.get(i*BROJ_ZAPISA_ZUPLJANA+4);
                LocalDate datumRodjenja = LocalDate.parse(datumString, DateTimeFormatter.ofPattern("dd.MM.yyyy."));

                zupljani.add(new Zupljanin(id, ime, prezime,sifra,datumRodjenja));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return zupljani;

    }
    public static List<Sakrament> dohvatiSakramente(List<Svecenik> svecenici, List<Zupljanin> zupljani){
        List<Sakrament> sakramenti = new ArrayList<>();

        try(BufferedReader bufReaderSakramenata = new BufferedReader(new FileReader(new File("dat/sakramenti.txt")))){

            List<String> datotekaSakramenata= bufReaderSakramenata.lines().collect(Collectors.toList());



            for(int i=0;i<datotekaSakramenata.size()/BROJ_ZAPISA_SAKRAMENATA;i++ ) {
                String idPredmetaString = datotekaSakramenata.get(i*BROJ_ZAPISA_SAKRAMENATA);
                Long id = Long.parseLong(idPredmetaString);

                String sifra = datotekaSakramenata.get(i*BROJ_ZAPISA_SAKRAMENATA+1);

                String naziv = datotekaSakramenata.get(i*BROJ_ZAPISA_SAKRAMENATA+2);

                String zupljaninIDString = datotekaSakramenata.get(i*BROJ_ZAPISA_SAKRAMENATA+3);
                String[] zupljaniIDStringovi = zupljaninIDString.split("\\W+");
                Set<Zupljanin> zupljaniSet = new HashSet<>();
                for (String s : zupljaniIDStringovi) {
                    Long idZupljanina = Long.parseLong(s);
                    for (Zupljanin zupljanin : zupljani) {
                        if (zupljanin.getId() == idZupljanina) {
                            zupljaniSet.add(zupljanin);
                        }

                    }
                }
                String liturgijaString = datotekaSakramenata.get(i*BROJ_ZAPISA_SAKRAMENATA+4);
                Integer liturgijaBroj = Integer.parseInt(liturgijaString);
                LiturgijskoRazdoblje liturgijskoRazdoblje = liturgija(liturgijaBroj);

                sakramenti.add(new Sakrament(id,sifra,naziv,zupljaniSet));






            }



        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sakramenti;

    }
    public static List<OsobniSakrament> dohvatiOsobneSakramente(List<Zupljanin> zupljani, List<Sakrament> sakramenti){
        List<OsobniSakrament> osobniSakramenti = new ArrayList<>();
        try(BufferedReader bufReaderOsobnihSakramenata = new BufferedReader(new FileReader(new File("dat/osobniSakramenti.txt")))) {
            List<String> datotekaOsobnihSakramenata = bufReaderOsobnihSakramenata.lines().collect(Collectors.toList());

            for(int i=0;i<datotekaOsobnihSakramenata.size()/BROJ_ZAPISA_OSOBNIH_SAKRAMENATA;i++ ) {

                String idOsobnogSakramentaString = datotekaOsobnihSakramenata.get(i * BROJ_ZAPISA_OSOBNIH_SAKRAMENATA);
                Long id = Long.parseLong(idOsobnogSakramentaString);

                String sakramentIDString = datotekaOsobnihSakramenata.get(i * BROJ_ZAPISA_OSOBNIH_SAKRAMENATA+1);
                Long sakramentID = Long.parseLong(sakramentIDString);
                Sakrament sakramentOsobnogSakramenta = null;
                for (Sakrament sakrament : sakramenti) {
                    if (Objects.equals(sakrament.getId(), sakramentID)) {
                        sakramentOsobnogSakramenta = sakrament;
                        break;
                    }
                }
                String zupljaninIDString = datotekaOsobnihSakramenata.get(i * BROJ_ZAPISA_OSOBNIH_SAKRAMENATA+2);
                Long zupljaninID = Long.parseLong(zupljaninIDString);
                Zupljanin zupljaninOsobnohSakramenta = null;
                for (Zupljanin zupljanin : zupljani) {
                    if (Objects.equals(zupljanin.getId(), zupljaninID)) {
                        zupljaninOsobnohSakramenta = zupljanin;
                        break;
                    }

                }
                String datumIVrijemeString =datotekaOsobnihSakramenata.get(i * BROJ_ZAPISA_OSOBNIH_SAKRAMENATA+3);
                LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeString, DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));

                String crkvaNaziv = datotekaOsobnihSakramenata.get(i * BROJ_ZAPISA_OSOBNIH_SAKRAMENATA+4);
                Crkva crkva = new Crkva(crkvaNaziv);

                osobniSakramenti.add(new OsobniSakrament(id, sakramentOsobnogSakramenta,zupljaninOsobnohSakramenta, datumIVrijeme,crkva, liturgija(1)));





            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        return osobniSakramenti;
    }



    public static void unosSakramenata(Scanner unos, List<Svecenik> svecenici, List<Sakrament> sakramenti, Map<Svecenik, List<Sakrament>> mapaSakramenataSvecenika) {
        long id = 0;
        for (int i = 0; i < BROJ_ZAPISA_SAKRAMENATA; i++) {
            id++;
            System.out.println("Unesite " + (i + 1) + ". sakramenta");
            System.out.print("Unesite šifru sakramenta: ");
            String sifra = unos.nextLine();

            System.out.print("Unesite naziv sakramenta: ");
            String naziv = unos.nextLine();


            System.out.print("Odabir >> ");
            int odabir = unos.nextInt();


            Set<Zupljanin> zupljani = new HashSet<>();

            System.out.print("Unesite kojem liturgijskom razdoblju pripada sakrament[BOZICNI(1) / KORIZMA(2) / OSTATAK (3) ]");
            LiturgijskoRazdoblje liturgijskoRazdoblje = liturgija(unos.nextInt());
            unos.nextLine();

            sakramenti.add(new Sakrament(id, sifra, naziv, zupljani));






















        }
    }

    public static List<Zupa> dohvatiZupnuZajednicu(List<Sakrament> sakramenti, List<Svecenik> svecenici, List<Zupljanin> zupljani, List<OsobniSakrament> osobniSakramenti){
        List<Zupa> zupe = new ArrayList<>();

        try(BufferedReader bufReaderZupi = new BufferedReader(new FileReader(new File("dat/zupe.txt")))) {

            List<String> datotekaZupi = bufReaderZupi.lines().toList();
            for(int i=0;i<datotekaZupi.size()/BROJ_ZAPISA_ZUPA;i++ ){


                String idZupaString = datotekaZupi.get(i*BROJ_ZAPISA_ZUPA);
                Long id = Long.parseLong(idZupaString);

                String naziv =  datotekaZupi.get(i*BROJ_ZAPISA_ZUPA+1);
                String sakramentIDString =  datotekaZupi.get(i*BROJ_ZAPISA_ZUPA+2);
                String[] sakramentiIDStringovi = sakramentIDString.split("\\W+");
                List<Sakrament> sakramentiZupe = new ArrayList<>();
                for (String s: sakramentiIDStringovi) {
                    Long idSakramenta = Long.parseLong(s);
                    for (Sakrament sk : sakramenti) {
                        if (sk.getId() == idSakramenta) {
                            sakramentiZupe.add(sk);
                        }
                    }
                }
                String svecenikIDString =  datotekaZupi.get(i*BROJ_ZAPISA_ZUPA+3);
                String[] sveceniciIDStringovi = svecenikIDString.split("\\W+");
                List<Svecenik> sveceniciZupe = new ArrayList<>();
                for (String s: sveceniciIDStringovi) {
                    Long idSvecenika = Long.parseLong(s);
                    for (Svecenik sk : svecenici) {
                        if (Objects.equals(sk.getId(), idSvecenika)) {
                            sveceniciZupe.add(sk);
                        }
                    }
                }

                String zupljaninIDString =  datotekaZupi.get(i*BROJ_ZAPISA_ZUPA+4);
                String[] zupljaniIDStringovi = zupljaninIDString.split("\\W+");
                List<Zupljanin> zupljaniZupe = new ArrayList<>();
                for (String s: zupljaniIDStringovi) {
                    Long idZupljanina= Long.parseLong(s);
                    for (Zupljanin zp : zupljani) {
                        if (Objects.equals(zp.getId(), idZupljanina)) {
                            zupljaniZupe.add(zp);
                        }
                    }
                }
                String osobniSakramentIDString =  datotekaZupi.get(i*BROJ_ZAPISA_ZUPA+5);
                String[] osobniSakramentiIDStringovi = osobniSakramentIDString.split("\\W+");
                List<OsobniSakrament> osobniSakramentiZupe = new ArrayList<>();
                for (String s: osobniSakramentiIDStringovi) {
                    Long idOsobnogSakramenta = Long.parseLong(s);
                    for (OsobniSakrament os : osobniSakramenti) {
                        if (os.getId() == idOsobnogSakramenta) {
                            osobniSakramentiZupe.add(os);
                        }
                    }
                }

                zupe.add(new Zupa(id, naziv, sakramentiZupe, sveceniciZupe, zupljaniZupe, osobniSakramentiZupe) {
                    @Override
                    public Zupljanin odrediNajboljegZupljaninaUGodini(Integer godina) {
                        return null;
                    }
                });


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return zupe;
    }


    public static void sortiranjeZupljanaPoPrezimenuPaImenu(List<Sakrament> sakramenti) {
        for (Sakrament sakrament : sakramenti) {

            if (sakrament.getZupljani().isEmpty()) {
                System.out.println("Nema zupljana sa ovim sakramentom  '" + sakrament.getNaziv() + "'.");
            } else {
                System.out.println("Zupljani sa ovim sakramentom '" + sakrament.getNaziv() + "' su:");
                Set<Zupljanin> sortiraniZupljani = new HashSet<>(sakrament.getZupljani());
                sortiraniZupljani.stream()
                        .sorted(new ZupljaninSorter())
                        .map(s -> s.getPrezime() + " " + s.getIme())
                        .forEach(System.out::println);
            }

        }

    }



}
