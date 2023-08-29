package com.example.projekt2023java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistracijaController {
    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaPasswordField;
    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private PasswordField svecenikPasswordField;

    @FXML
    private void handleRegistracija(ActionEvent event) {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = lozinkaPasswordField.getText();
        String selectedRole = roleChoiceBox.getValue();


        if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
            prikaziPoruku("Molimo unesite korisničko ime i lozinku.", Alert.AlertType.WARNING);
            return;
        }
        if (roleChoiceBox.getValue() ==null) {
            prikaziPoruku("Molimo odaberite korisnicku rolu.", Alert.AlertType.WARNING);
            return;
        }

        if (korisnickoImeExists(korisnickoIme)) {
            prikaziPoruku("Korisničko ime već postoji. Molimo odaberite drugo ime.", Alert.AlertType.ERROR);
            return;
        }
        if ("Svecenik".equals(selectedRole)) {
            String svecenikPassword = svecenikPasswordField.getText();
            if (!"13579".equals(svecenikPassword)) {
                prikaziPoruku("Unesite ispravan password za Svecenik ulogu.", Alert.AlertType.ERROR);
                return;
            }
            // Proceed with registration and password hashing
        }

        String hashiranaLozinka = hashirajLozinku(lozinka);
        if (hashiranaLozinka != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("dat/lozinke.txt", true))) {
                writer.write(korisnickoIme + ":" + hashiranaLozinka + ":" + selectedRole + "\n");
                prikaziPoruku("Registracija uspješna!", Alert.AlertType.INFORMATION);

                prebaciNaEkranPrijave();
            } catch (IOException e) {
                prikaziPoruku("Pogreška prilikom zapisivanja u datoteku.", Alert.AlertType.ERROR);
            }
        }
    }


    private boolean korisnickoImeExists(String korisnickoIme) {
        try (BufferedReader citac = new BufferedReader(new FileReader("dat/lozinke.txt"))) {
            String linija;
            while ((linija = citac.readLine()) != null) {
                String[] dijelovi = linija.split(":");
                if (dijelovi.length == 3 && dijelovi[0].equals(korisnickoIme)) {
                    return true; // Username already exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username does not exist
    }
    private void prebaciNaEkranPrijave() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Login:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    @FXML
    private void prebaciNaEkranPrijave(ActionEvent event) throws IOException {
        prebaciNaEkranPrijave();
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
            prikaziPoruku("Pogreška prilikom hashiranja lozinke.", Alert.AlertType.ERROR);
        }
        return null;
    }

    private void prikaziPoruku(String poruka, Alert.AlertType tipPoruke) {
        Alert alert = new Alert(tipPoruke);
        alert.setTitle("Obavijest o registraciji");
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }
}
