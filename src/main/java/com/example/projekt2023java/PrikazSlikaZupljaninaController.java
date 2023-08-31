package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Slike;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

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

    @FXML
    private Button nextButton;
    @FXML
    private Node izbornikInclude;

    @FXML
    private Node izbornikZupljaninaInclude;

    private List<byte[]> imagesList;
    private int currentIndex;


    public void initialize() {
        List<String> zupljaninList = BazaPodataka.dohvatiSveZupljane().stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));
        nextButton.setDisable(true); // Disable the "Next" button initially

        boolean useIzbornikZupljanina = !getUserRole().equals("Svecenik");
        System.out.println("rola: " + getUserRole());
        izbornikInclude.setVisible(!useIzbornikZupljanina);
        izbornikZupljaninaInclude.setVisible(useIzbornikZupljanina);

    }

    public void prikaziSlike(){
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

        Zupljanin ovajZupljanin=sviZupljani.get(0);

        for (Zupljanin zupljanin:sviZupljani) {
            if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                ovajZupljanin = zupljanin;
            }
        }

        imagesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/java-tvz-2023-PROJEKT", "student", "student")) {
            String query = "SELECT slika FROM ZupljaninSlike WHERE zupljanin_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, ovajZupljanin.getId());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                imagesList.add(slikaBytes);
            }

            resultSet.close();
            statement.close();
            currentIndex = 0; // Initialize index for images
            if (!imagesList.isEmpty()) {
                setImageInView();
                nextButton.setDisable(false); // Enable the "Next" button
            } else {
                imageView.setImage(null); // No images found, clear the image view
                nextButton.setDisable(true); // Disable the "Next" button
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @FXML
    private void showNextImage(ActionEvent event) {
        currentIndex = (currentIndex + 1) % imagesList.size();
        setImageInView();
    }

    private void setImageInView() {
        byte[] slikaBytes = imagesList.get(currentIndex);
        Image image = new Image(new ByteArrayInputStream(slikaBytes));
        imageView.setImage(image);
    }
    private String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[1]; // Return the role part
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
