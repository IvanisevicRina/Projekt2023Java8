package com.example.projekt2023java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class IzbornikZupljanina {
    public void ispisPotvrdaScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("potvrdaPrimljenihSakramenata.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Potvrde sakramenata:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }


}
