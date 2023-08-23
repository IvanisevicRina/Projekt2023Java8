package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.OsobniSakrament;
import entitet.Sakrament;
import entitet.Svecenik;
import entitet.Zupljanin;
import util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class OsobniSakramentController {


    @FXML
    private TextField nazivSakramentaOsobnogSakramentaTextField;

    @FXML
    private TextField imeZupljaninaOsobnogSakramentaTextField;

    @FXML
    private TextField prezimeZupljaninaOsobnogSakramentaTextField;


    @FXML
    private TextField datumOsobnogSakramentaTextField;



    @FXML
    private TableView<OsobniSakrament> osobniSakramentiTableView;

    @FXML
    private TableColumn<OsobniSakrament, String> nazivSakramentaOsobnogSakramentaTableColumn;

    @FXML
    private TableColumn<OsobniSakrament, String> imeZupljaninaOsobnogSakramentaTableColumn;

    @FXML
    private TableColumn<OsobniSakrament, String> prezimeZupljaninaOsobnogSakramentaTableColumn;


    @FXML
    private TableColumn<OsobniSakrament, String> datumOsobnogSakramentaTableColumn;



    private List<OsobniSakrament> osobniSakramentiList;


    //ona se poziva kad dodjemo na ekran, znaci prije svega
    public void initialize(){

         osobniSakramentiList= BazaPodataka.dohvatiSveOsobneSakramente();




        nazivSakramentaOsobnogSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getSakrament().getNaziv()));
        imeZupljaninaOsobnogSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getZupljanin().getIme()));
        prezimeZupljaninaOsobnogSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getZupljanin().getPrezime()));



        datumOsobnogSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getDatumIVrijeme().toString()));



        osobniSakramentiTableView.setItems(FXCollections.observableList(osobniSakramentiList));

    }


    public void dohvatiOsobneSakramente(){


        String nazivPredmeta = nazivSakramentaOsobnogSakramentaTextField.getText();
        String imeStudenta = imeZupljaninaOsobnogSakramentaTextField.getText();
        String prezimeStudenta = prezimeZupljaninaOsobnogSakramentaTextField.getText();
        String datumIVrijeme =datumOsobnogSakramentaTextField.getText();



        List<OsobniSakrament> filtriraniOsobniSakramenti = osobniSakramentiList.stream()
                .filter(p -> p.getSakrament().getNaziv().toLowerCase().contains(nazivPredmeta.toLowerCase()))
                .filter(p -> p.getZupljanin().getIme().toLowerCase().contains(imeStudenta.toLowerCase()))
                .filter(p -> p.getZupljanin().getPrezime().toLowerCase().contains(prezimeStudenta.toLowerCase()))
                .filter(p -> p.getDatumIVrijeme().toString().contains(datumIVrijeme))
                .toList();

        osobniSakramentiTableView.setItems(FXCollections.observableList(filtriraniOsobniSakramenti));
    }





}

