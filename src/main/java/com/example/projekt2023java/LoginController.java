package com.example.projekt2023java;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Controller klasa za ekran prijave korisnika.
 */
public class LoginController {

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private PasswordField lozinkaPasswordField;

    /**
     * Metoda za obradu događaja prijave korisnika.
     *
     * @throws IOException Ako dođe do greške pri prikazu drugog ekrana ili učitavanju datoteka.
     */
    @FXML
    private void handlePrijava() throws IOException {
        String korisnickoIme = korisnickoImeTextField.getText();
        String lozinka = lozinkaPasswordField.getText();

        if (autentifikacija(korisnickoIme, lozinka)) {
            prikaziPoruku("Prijava uspješna!", Alert.AlertType.INFORMATION);
            String userRole = extractUserRoleFromDatabase("dat/lozinke.txt", korisnickoIme);
            saveUserRoleToFile(korisnickoIme, userRole);
            if ("Svecenik".equals(userRole)) {
                // Redirect to Svecenik-specific screen/menu
                prikaziSveceniciPregled();
            } else if ("Zupljanin".equals(userRole)) {
                prikaziZupljaniPregled();
            }
            System.out.println(userRole);

            prikaziSveceniciPregled();
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

    private void saveUserRoleToFile(String korisnickoIme, String role) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dat/rola.txt", false))) {
            writer.write(korisnickoIme);
            writer.write(":");
            writer.write(role);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void prikaziSveceniciPregled() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("zupneObavijesti.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Obavijesti:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    private void prikaziZupljaniPregled() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("zupneObavijesti.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Obavijesti:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    private String procitajHashIzDatoteke(String putanjaDatoteke, String korisnickoIme) {
        try (BufferedReader citac = new BufferedReader(new FileReader(putanjaDatoteke))) {
            String linija;
            while ((linija = citac.readLine()) != null) {
                String[] dijelovi = linija.split(":");
                if (dijelovi.length == 3 && dijelovi[0].equals(korisnickoIme)) {
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

    /**
     * Metoda za prikazivanje poruke (Alert) korisniku.
     *
     * @param poruka   Tekst poruke.
     * @param tipPoruke Tip poruke (INFORMATION, ERROR, itd.).
     */

    private void prikaziPoruku(String poruka, Alert.AlertType tipPoruke) {
        Alert alert = new Alert(tipPoruke);
        alert.setTitle("Obavijest o prijavi");
        alert.setHeaderText(null);
        alert.setContentText(poruka);
        alert.showAndWait();
    }

    /**
     * Metoda za preusmjeravanje korisnika na ekran za registraciju.
     *
     * @throws IOException Ako dođe do greške pri prikazu ekrana za registraciju.
     */
    @FXML
    private void registracija() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registracija.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        HelloApplication.getMainStage().setTitle("Registracija:");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    private String extractUserRoleFromDatabase(String putanjaDatoteke, String korisnickoIme) {
        try (BufferedReader citac = new BufferedReader(new FileReader(putanjaDatoteke))) {
            String linija;
            while ((linija = citac.readLine()) != null) {
                String[] dijelovi = linija.split(":");
                if (dijelovi.length == 3 && dijelovi[0].equals(korisnickoIme)) {
                    return dijelovi[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
