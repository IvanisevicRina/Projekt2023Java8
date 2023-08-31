package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class  HelloApplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Aplikacija Župne zajednice!");
        stage.setScene(scene);
        stage.show();

        // Pokreni osvježavanje svakih 5 minuta
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshData(); // Metoda za osvježavanje podataka
            }
        }, 0, 1 * 60 * 1000); // Počni odmah i ponavljaj svakih 5 minuta
    }

    public static void main(String[] args) {
        launch();
    }
    public static Stage getMainStage(){
        return mainStage;
    }
    private void refreshData() {
        Platform.runLater(() -> {
            // Ovdje osvježite podatke na ekranu
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
            String formattedDate = dateFormat.format(currentDate);

            SveciKalendar kalendar = new SveciKalendar(); // Kreirajte instancu klase s imenima svetaca
            String imeSveca = kalendar.getImeSveca(formattedDate);


            List<Osoba> sveOsobe = new ArrayList<>();
            sveOsobe.addAll(BazaPodataka.dohvatiSveZupljane());
            sveOsobe.addAll(BazaPodataka.dohvatiSveSvecenike());

            List<Osoba> osobeSaImendanom = new ArrayList<>();

            for (Osoba osoba : sveOsobe) {
                if (osoba.getIme().equals(imeSveca)) {
                    osobeSaImendanom.add(osoba);
                }
            }

            Imendan<Osoba> imendanOsobama = new Imendan<>();
            imendanOsobama.cestitajImendan(osobeSaImendanom, imeSveca, formattedDate);
        });
    }




}