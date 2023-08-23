package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Zupljanin;
import util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PregledZupljana {

        @FXML
        private TextField imeZupljaninaTextField;

        @FXML
        private TextField prezimeZupljaninaTextField;

        @FXML
        private TextField sifraZupljaninaTextField;

        @FXML
        private DatePicker datumRodjenjaDatePicker;

        @FXML
        private TableView<Zupljanin> zupljaninTableView;

        @FXML
        private TableColumn<Zupljanin, String> imeZupljaninaTableColumn;

        @FXML
        private TableColumn<Zupljanin, String> prezimeZupljaninaTableColumn;

        @FXML
        private TableColumn<Zupljanin, String> sifraZupljaninaTableColumn;

        @FXML
        private TableColumn<Zupljanin, String> datumZupljaninaTableColumn;


        private List<Zupljanin> zupljaniList;


        //ona se poziva kad dodjemo na ekran, znaci prije svega
        public void initialize() {

        zupljaniList = BazaPodataka.dohvatiSveZupljane();


        imeZupljaninaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeZupljaninaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPrezime()));
        sifraZupljaninaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSifra()));
        datumZupljaninaTableColumn
                .setCellValueFactory(
                        new Callback<TableColumn.CellDataFeatures<Zupljanin, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(
                                    TableColumn.CellDataFeatures<Zupljanin, String> student) {
                                SimpleStringProperty property = new SimpleStringProperty();
                                DateTimeFormatter formatter =
                                        DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                                property.setValue(
                                        student.getValue().getDatumRodjenja().format(formatter));
                                return property;
                            }
                        }
                );


        zupljaninTableView.setItems(FXCollections.observableList(zupljaniList));

    }


        public void dohvatiZupljane() {

        String prezime = prezimeZupljaninaTextField.getText();
        String sifra= sifraZupljaninaTextField.getText();
        String ime = imeZupljaninaTextField.getText();
        LocalDate datumRodjenja = datumRodjenjaDatePicker.getValue();

        List<Zupljanin> filtriraniZupljani = zupljaniList.stream()
                .filter(s -> s.getPrezime().toLowerCase().contains(prezime.toLowerCase()))
                .filter(s -> s.getIme().toLowerCase().contains(ime.toLowerCase()))
                .filter(s -> s.getSifra().contains(sifra))
                .filter(student -> student.getDatumRodjenja().equals(Optional.ofNullable(datumRodjenja)
                        .isEmpty() ? student.getDatumRodjenja() : datumRodjenja))
                .toList();


        zupljaninTableView.setItems(FXCollections.observableList(filtriraniZupljani));
    }
}
