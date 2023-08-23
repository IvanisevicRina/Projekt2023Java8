package com.example.projekt2023java;
import baza.BazaPodataka;
import entitet.Slike;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnosGalerijaController {
    @FXML
    private TextField nazivSlikeField;

    @FXML
    private Button odaberiSlikuButton;

    @FXML
    private ListView<String> odabirZupljanaListView ;


    private File odabranaSlikaFile;



    public void odaberiSliku() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odaberi sliku");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Slike", "*.jpg", "*.jpeg", "*.png"));
        odabranaSlikaFile = fileChooser.showOpenDialog(odaberiSlikuButton.getScene().getWindow());

        if (odabranaSlikaFile != null) {
            nazivSlikeField.setText(odabranaSlikaFile.getName());
        }
    }

    public void spremiSliku() {
        if (odabranaSlikaFile != null) {
            List<Slike> slikeList = new ArrayList<>();
            try {
                byte[] slikaBytes = ucitajSlikuBytes(odabranaSlikaFile);

                Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/java-tvz-2023-PROJEKT", "student", "student");
                String query = "INSERT INTO Slike (naziv, slika) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nazivSlikeField.getText());
                statement.setBytes(2, slikaBytes);
                statement.executeUpdate();

                Slike slika = new Slike(nazivSlikeField.getText(), slikaBytes);
                slikeList.add(slika);



                Set<Zupljanin> oviZupljani = new HashSet<>();

                List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

                ObservableList<String> selectedItems = odabirZupljanaListView.getSelectionModel().getSelectedItems();
                for (Object o : selectedItems) {
                    for (Zupljanin zupljanin : sviZupljani) {
                        System.out.println("o.prezime = " + o);
                        System.out.println("zupljanin= " + zupljanin.getPrezime());
                        if (o.equals(zupljanin.getIme() + " " + zupljanin.getPrezime())) {
                            List<Slike> slike_zupljanina = new ArrayList<>();
                            if(zupljanin.getSlike().isEmpty()){
                                slike_zupljanina.add(slika);
                            }else {
                                slike_zupljanina = zupljanin.getSlike();
                                slike_zupljanina.add(slika);
                            }
                            zupljanin.setSlike(slike_zupljanina);
                            azurirajZupljanineSlike(zupljanin);

                        }
                    }
                }

                statement.close();
                connection.close();

                // Ovdje možete dodati potvrdu spremanja ili prikaz poruke o uspješnom unosu
                System.out.println("Slika je uspješno spremljena.");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje slike");
                alert.setHeaderText("Uspješno dodana slika");
                alert.setContentText("SLIKA dodana u aplikaciju!");
                alert.showAndWait();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje slike NEUSPJELO");
                alert.setHeaderText("Neuspješno dodana slika");
                alert.showAndWait();
            }
        }
    }
    public void azurirajZupljanineSlike(Zupljanin zupljanin) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/java-tvz-2023-PROJEKT", "student", "student")) {
            String query = "UPDATE Zupljanin SET slike = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            // Pretvorba liste slika u byte[] i postavljanje parametara
            byte[] slikeBytes = pretvoriListuSlikaUByteArray(zupljanin.getSlike());
            statement.setBytes(1, slikeBytes);
            statement.setLong(2, zupljanin.getId());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public byte[] pretvoriListuSlikaUByteArray(List<Slike> slike) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(slike);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private byte[] ucitajSlikuBytes(File slikaFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(slikaFile)) {
            byte[] buffer = new byte[(int) slikaFile.length()];
            fileInputStream.read(buffer);
            return buffer;
        }
    }

    public void initialize(){

        List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();
        List<String> zupljaniList = zupljani.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljanaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirZupljanaListView.setItems(FXCollections.observableList(zupljaniList));
    }
}
