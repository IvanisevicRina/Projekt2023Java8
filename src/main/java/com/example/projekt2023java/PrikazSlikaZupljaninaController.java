package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Slike;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class PrikazSlikaZupljaninaController {
    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;




    private int currentIndex;
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
        List<String> zupljaninList = BazaPodataka.dohvatiSveZupljane().stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));

    }

    public void prikaziSlike(){
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

        Zupljanin ovajZupljanin=sviZupljani.get(0);

        for (Zupljanin zupljanin:sviZupljani) {
            if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                ovajZupljanin = zupljanin;
            }
        }
        try (Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/java-tvz-2023-PROJEKT", "student", "student")) {
            String query = "SELECT slika FROM ZupljaninSlike WHERE zupljanin_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, ovajZupljanin.getId());  // zupljanin_id

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                Image image = new Image(new ByteArrayInputStream(slikaBytes));
                imageView.setImage(image);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }



}
