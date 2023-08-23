package com.example.projekt2023java;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaPasswordField;

    @FXML
    private void handlePrijava(ActionEvent event) {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = lozinkaPasswordField.getText();

        if (autentifikacija(korisnickoIme, lozinka)) {
            prikaziPoruku("Prijava uspješna!", Alert.AlertType.INFORMATION);
            // Ovdje možete izvršiti željenu akciju nakon uspješne prijave
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sveceniciPregled.fxml"));
            Parent root;
            try {
                root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Pregled svećenika");
                stage.setScene(new Scene(root));
                stage.show();

                // Zatvaranje trenutnog prozora
                Stage currentStage = (Stage) korisnickoImeTextField.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            prikaziPoruku("Neuspješna prijava. Provjerite korisničko ime i lozinku.", Alert.AlertType.ERROR);
        }
    }

    private boolean autentifikacija(String korisnickoIme, String lozinka) {
        // Čitanje hashiranih lozinki iz tekstualne datoteke
        String putanjaDatoteke = "dat/lozinke.txt";
        String spremljeniHash = procitajHashIzDatoteke(putanjaDatoteke, korisnickoIme);
        if (spremljeniHash == null) {
            return false;
        }

        System.out.println("lozinka:" + lozinka);
        // Hashiranje unesene lozinke
        String uneseniHash = hashirajLozinku(lozinka);

        System.out.println("uneseniHash:" +uneseniHash);

        // Usporedba hashiranih lozinki
        return spremljeniHash.equals(uneseniHash);
    }

    private String procitajHashIzDatoteke(String putanjaDatoteke, String korisnickoIme) {
        try (BufferedReader citac = new BufferedReader(new FileReader(putanjaDatoteke))) {
            String linija;
            while ((linija = citac.readLine()) != null) {
                String[] dijelovi = linija.split(":");
                if (dijelovi.length == 2 && dijelovi[0].equals(korisnickoIme)) {
                    return dijelovi[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String hashirajLozinku(String lozinka) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashiranje = digest.digest(lozinka.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashiranje) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void prikaziPoruku(String poruka, Alert.AlertType tipPoruke) {
        Alert alert = new Alert(tipPoruke);
        alert.setTitle("Obavijest o prijavi");
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }
}
