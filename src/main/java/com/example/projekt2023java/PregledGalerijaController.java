package com.example.projekt2023java;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PregledGalerijaController {
    @FXML
    private ImageView imageView;

    private ResultSet resultSet;
    private Statement statement;

    public void initialize() {
        prikaziSlike();
    }
    public void prikaziSlike() {
        try {
            Connection connection =DriverManager.getConnection("jdbc:h2:tcp://localhost/~/java-tvz-2023-PROJEKT", "student", "student");
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = statement.executeQuery("SELECT slika FROM Slike");

            // Prikaz prvog prikaza slike
            if (resultSet.next()) {
                byte[] slikaBytes = resultSet.getBytes("slika");
                Image image = new Image(new ByteArrayInputStream(slikaBytes));
                imageView.setImage(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


}
