package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class SveceniciController {

    @FXML
    private TextField sifraSvecenikaTextField;

    @FXML
    private TextField prezimeSvecenikaTextField;

    @FXML
    private TextField imeSvecenikaTextField;

    @FXML
    private TextField titulaSvecenikaTextField;

    @FXML
    private TableView<Svecenik> svecenikTableView;

    @FXML
    private TableColumn<Svecenik, String> sifraSvecenikaTableColumn;

    @FXML
    private TableColumn<Svecenik, String> prezimeSvecenikaTableColumn;

    @FXML
    private TableColumn<Svecenik, String> imeSvecenikaTableColumn;

    @FXML
    private TableColumn<Svecenik, String> titulaSvecenikaTableColumn;


    private List<Svecenik> svecenikList;


    //ona se poziva kad dodjemo na ekran, znaci prije svega
    public void initialize(){

        svecenikList = BazaPodataka.dohvatiSveSvecenike();
        sifraSvecenikaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSifra()));
        prezimeSvecenikaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getPrezime()));

        imeSvecenikaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getIme()));
        titulaSvecenikaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getTitula()));



        svecenikTableView.setItems(FXCollections.observableList(svecenikList));

    }


    public void dohvatiSvecenike(){

        String sifra = sifraSvecenikaTextField.getText();
        String prezime = prezimeSvecenikaTextField.getText();
        String ime =imeSvecenikaTextField.getText();
        String titula = titulaSvecenikaTextField.getText();

        List<Svecenik> filtriraniSvecenici = svecenikList.stream()
                .filter(p -> p.getPrezime().toLowerCase().contains(prezime.toLowerCase()))
                .filter(p -> p.getIme().toLowerCase().contains(ime.toLowerCase()))
                .filter(p -> p.getSifra().contains(sifra))
                .filter(p -> p.getTitula().toLowerCase().contains(titula.toLowerCase()))
                .toList();


        svecenikTableView.setItems(FXCollections.observableList(filtriraniSvecenici));
    }

}
