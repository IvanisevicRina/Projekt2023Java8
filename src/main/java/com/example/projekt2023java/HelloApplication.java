package com.example.projekt2023java;

import baza.BazaPodataka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entitet.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Služi za pokretanje aplikacije
 */

public class  HelloApplication extends Application {
    private static Stage mainStage;

    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    /**
     * Služi za postavljanje logina kao sljedeće scene
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Aplikacija Župne zajednice!");
        stage.setScene(scene);
        stage.show();


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshData();
            }
        }, 0, 2 * 60 * 1000);
    }

    public static void main(String[] args) {

        logger.info("Exampe log from {}", HelloApplication.class.getSimpleName());
        launch();
    }
    public static Stage getMainStage(){
        return mainStage;
    }

    /**
     * Osvježava podatke za imendan
     */

    private void refreshData() {
        Platform.runLater(() -> {

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
            String formattedDate = dateFormat.format(currentDate);

            SveciKalendar kalendar = new SveciKalendar();
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