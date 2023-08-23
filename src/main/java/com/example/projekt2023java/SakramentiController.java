package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Sakrament;
import entitet.Svecenik;
import entitet.Zupljanin;
import javafx.beans.property.SimpleIntegerProperty;
import util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class SakramentiController {



    @FXML
    private TextField sifraSakramentaTextField;

    @FXML
    private TextField nazivSakramentaTextField;

    @FXML
    private TextField sifraZupljanaTextField;




    @FXML
    private TableView<Sakrament> sakramentiTableView;

    @FXML
    private TableColumn<Sakrament, String> sifraSakramentaTableColumn;

    @FXML
    private TableColumn<Sakrament, String> nazivSakramentaTableColumn;

    @FXML
    private TableColumn<Sakrament, String> sifreZupljanaSakramentaTableColumn;
    @FXML
    private TableColumn<Sakrament, String> brojZupljanaSakramentaTableColumn;











    private List<Sakrament> sakramentiList;


    public void initialize(){


        sakramentiList = BazaPodataka.dohvatiSveSakramente();


        sifraSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSifra()));
        nazivSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getNaziv()));
        sifreZupljanaSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getZupljani().stream().map(z -> z.getSifra()).toList().toString()));
        brojZupljanaSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(String.valueOf(cellData.getValue().getZupljani().size())));


        sakramentiTableView.setItems(FXCollections.observableList(sakramentiList));

    }


    public void dohvatiSakramenti(){

        String sifra = sifraSakramentaTextField.getText();
        String naziv = nazivSakramentaTextField.getText();
        String sifraZupljana = sifraZupljanaTextField.getText();


        List<Sakrament> filtriraniSakramenti = sakramentiList.stream()
                .filter(p -> p.getNaziv().toLowerCase().contains(naziv.toLowerCase()))
                .filter(p -> p.getSifra().toLowerCase().contains(sifra.toLowerCase()))
                .filter(p -> p.getZupljani().stream().anyMatch(z -> z.getSifra().equalsIgnoreCase(sifraZupljana)))

                .toList();

        sakramentiTableView.setItems(FXCollections.observableList(filtriraniSakramenti));
    }



}
