package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.*;
import javafx.scene.Node;
import util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private TextField lokacijaSakramentaTextField;

    @FXML
    private TextField liturgijaSakramentaTextField;



    @FXML
    private Node izbornikInclude;

    @FXML
    private Node izbornikZupljaninaInclude;



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

    @FXML
    private TableColumn<OsobniSakrament, String> liturgijaOsobnogSakramentaTableColumn;


    @FXML
    private TableColumn<OsobniSakrament, String> lokacijaSakramentaTableColumn;



    private List<OsobniSakrament> osobniSakramentiList;

    private static final long serialVersionUID = 5396125547749070789L;

    public void initialize() throws Exception {

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

        liturgijaOsobnogSakramentaTableColumn.setCellValueFactory(cellData -> {
            LiturgijskoRazdoblje liturgijskoRazdoblje = cellData.getValue().getLiturgijskoRazdoblje();
            if (liturgijskoRazdoblje != null) {
                return new SimpleStringProperty(liturgijskoRazdoblje.toString());
            } else {
                return new SimpleStringProperty("");
            }
        });
        lokacijaSakramentaTableColumn
                .setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().getCrkva().nazivCrkve()));




        osobniSakramentiTableView.setItems(FXCollections.observableList(osobniSakramentiList));

        boolean useIzbornikZupljanina = !getUserRole().equals("Svecenik");
        System.out.println("rola: " + getUserRole());
        izbornikInclude.setVisible(!useIzbornikZupljanina);
        izbornikZupljaninaInclude.setVisible(useIzbornikZupljanina);

    }


    public void dohvatiOsobneSakramente(){


        String nazivPredmeta = nazivSakramentaOsobnogSakramentaTextField.getText();
        String imeStudenta = imeZupljaninaOsobnogSakramentaTextField.getText();
        String prezimeStudenta = prezimeZupljaninaOsobnogSakramentaTextField.getText();
        String datumIVrijeme =datumOsobnogSakramentaTextField.getText();
        String liturgija = liturgijaSakramentaTextField.getText();
        String crkva = lokacijaSakramentaTextField.getText();




        List<OsobniSakrament> filtriraniOsobniSakramenti = osobniSakramentiList.stream()
                .filter(p -> p.getSakrament().getNaziv().toLowerCase().contains(nazivPredmeta.toLowerCase()))
                .filter(p -> p.getZupljanin().getIme().toLowerCase().contains(imeStudenta.toLowerCase()))
                .filter(p -> p.getZupljanin().getPrezime().toLowerCase().contains(prezimeStudenta.toLowerCase()))
                .filter(p -> p.getDatumIVrijeme().toString().contains(datumIVrijeme))
                .filter(p->p.getLiturgijskoRazdoblje().toString().toLowerCase().contains(liturgija.toLowerCase()))
                .filter(p->p.getCrkva().nazivCrkve().toLowerCase().contains(crkva.toLowerCase()))
                .toList();

        osobniSakramentiTableView.setItems(FXCollections.observableList(filtriraniOsobniSakramenti));
    }
    private String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}

