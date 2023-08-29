package com.example.projekt2023java;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class PregledGalerijaController {
    @FXML
    private ImageView imageView;

    private ResultSet resultSet;
    @FXML
    private Node izbornikInclude;

    @FXML
    private Node izbornikZupljaninaInclude;

    private static Connection connectToDatabase() throws Exception {
        Properties konfiguracijaBaze = new Properties();
        konfiguracijaBaze.load(new FileInputStream("dat/bazaPodataka.properties"));

        Connection con = DriverManager.getConnection(
                konfiguracijaBaze.getProperty("bazaPodatakaUrl"),
                konfiguracijaBaze.getProperty("korisnickoIme"),
                konfiguracijaBaze.getProperty("lozinka"));
        return con;
    }
    public void initialize() {
        prikaziSlike();
        boolean useIzbornikZupljanina = !getUserRole().equals("Svecenik");
        System.out.println("rola: " + getUserRole());
        izbornikInclude.setVisible(!useIzbornikZupljanina);
        izbornikZupljaninaInclude.setVisible(useIzbornikZupljanina);

    }

    public void prikaziSlike() {
        try {
            Connection con = connectToDatabase();
            if(con!=null){
                System.out.println("Uspješno smo se spojili na bazu!");
            }Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = statement.executeQuery("SELECT slika FROM Slike");

            // Prikaz prvog prikaza slike
            if (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                Image image = new Image(new ByteArrayInputStream(slikaBytes));
                imageView.setImage(image);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void prikaziSljedecuSliku() {
        try {
            if (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                Image image = new Image(new ByteArrayInputStream(slikaBytes));
                imageView.setImage(image);
            } else {
                resultSet.beforeFirst(); // Vrati na početak rezultat seta ako je kraj dostignut
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
