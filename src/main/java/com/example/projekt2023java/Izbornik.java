package com.example.projekt2023java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Izbornik {
    public void showZupljaniSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("zupljaniPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga zupljana");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void showSveceniciSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sveceniciPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga svecenika");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void showSakramentiSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sakramentiPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showOsobniSakramentiSearchScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("osobniSakramentiPregled.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pretraga osobnih sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void sveceniciUnosScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos svećenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showZupljaniEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosZupljanina.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos zupljanina :");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showSakramentiEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosSakramenata.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos sakramenta:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showOsobniSakramentiEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosOsobnogSakramenta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos osobnog sakramenta:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showDeletingSvecenikScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje svecenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showIzmjenaSvecenikScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("izmjenaSvecenika.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Izmjena svecenika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showGalerijaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pregledGalerija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Pregled Slika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showGalerijaUnosScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosGalerija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Unos Slika:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void showGalerijaZupljanina() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prikazSlikaZupljanina.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Prikaz Slika Zupljanina:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void deleteZupljaniEnteringScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeZupljana.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Brisanje zupljana:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void ispisPotvrdaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("potvrdaPrimljenihSakramenata.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Potvrde sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }



}
