package com.example.projekt2023java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Izbornik {
    /**
     * Prikazuje zaslon za pretragu župljana.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showZupljaniSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("zupljaniPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga zupljana");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za pretragu svećenika.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showSveceniciSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sveceniciPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga svecenika");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    /**
     * Prikazuje zaslon za pretragu sakramenata.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showSakramentiSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sakramentiPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za pretragu osobnih sakramenata.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showOsobniSakramentiSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("osobniSakramentiPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga osobnih sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za unos svećenika.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void sveceniciUnosScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos svećenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za unos župljana.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showZupljaniEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosZupljanina.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos zupljanina :");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za unos sakramenata.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showSakramentiEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosSakramenata.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos sakramenta:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za unos osobnih sakramenata.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showOsobniSakramentiEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosOsobnogSakramenta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos osobnog sakramenta:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za brisanje svećenika.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showDeletingSvecenikScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje svecenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za izmjenu svećenika.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showIzmjenaSvecenikScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("izmjenaSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Izmjena svecenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za izmjenu župljana
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showIzmjenaZupljaninScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("izmjenaZupljana.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Izmjena zupljana:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za pregled galerije slika.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showGalerijaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pregledGalerija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pregled Slika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za unos slika u galeriju.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showGalerijaUnosScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosGalerija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos Slika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za prikaz slika župljana.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showGalerijaZupljanina() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prikazSlikaZupljanina.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        HelloApplication.getMainStage().setTitle("Prikaz Slika Zupljanina:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za brisanje župljana.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void deleteZupljaniEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeZupljana.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje zupljana:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za ispis potvrda primljenih sakramenata.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void ispisPotvrdaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("potvrdaPrimljenihSakramenata.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Potvrde sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za registraciju.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void registracijaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registracija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Potvrde sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za logiranje.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void logiranjeScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Potvrde sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    /**
     * Prikazuje zaslon za prikaz promjena.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showPromjeneScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prikazPromjena.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Promjene:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    /**
     * Prikazuje zaslon za brisanje osobnog sakramenta.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void showOsobniSakramentiDeleteScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeOsobnogSakramenta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    /**
     * Prikazuje zaslon za izmjenu osobnog sakramenta.
     *
     * @throws IOException Ako se pojavi greška pri učitavanju FXML-a ili prikazu scene.
     */
    public void  showOsobniSakramentiUpdateScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("izmjenaOsobnogSakramenta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Izmjena:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }


    public void  showDeletingEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeSakramentaZupljanina.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void  showObavijestiScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("zupneObavijesti.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Obavijesti:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

}
