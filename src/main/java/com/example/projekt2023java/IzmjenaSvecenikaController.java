package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
import iznimke.DuplikatSifreException;
import iznimke.TekstualniZapisException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


public class IzmjenaSvecenikaController {

    @FXML
    private TextField imeSvecenikaTextField;
    @FXML
    private TextField sifraSvecenikaTextField;
    @FXML
    private TextField prezimeSvecenikaTextField;
    @FXML
    private TextField titulaSvecenikaTextField;
    @FXML
    private ListView<String> odabirSvecenikaListView ;
    private static final Logger logger = LoggerFactory.getLogger(IzmjenaSvecenikaController.class);

    public void azurirajSvecenike() throws Exception {

        String imeSvecenika = imeSvecenikaTextField.getText();
        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        String sifraSvecenika= sifraSvecenikaTextField.getText();
        String titulaSvecenika = titulaSvecenikaTextField.getText();
        List<Svecenik> sviSvecenici = BazaPodataka.dohvatiSveSvecenike();
        Svecenik ovajSvecenik = new SvecenikBuilder().createSvecenik();
        ObservableList<String> selectedItems = odabirSvecenikaListView.getSelectionModel().getSelectedItems();
        Boolean errorOccured = false;
        if (selectedItems.isEmpty()) {
            displayAlert("Greška", "Morate odabrati svećenika za ažuriranje.");
            return;
        }

        try {
            sadrziBrojeve(imeSvecenika);
            sadrziBrojeve(prezimeSvecenika);

        } catch (TekstualniZapisException e) {
            errorOccured=true;
            imeSvecenika = "";
            prezimeSvecenika = "";
            logger.error("Sadrži nedozvoljene znakove(brojeve)", e);
            displayAlert("Greška", e.getMessage());
        }
        for(Object o : selectedItems){
            for (Svecenik svecenik:sviSvecenici) {
                if(o.equals(svecenik.getSifra() + "-----"+svecenik.getIme() + " " +svecenik.getPrezime())){
                    ovajSvecenik = svecenik;}}}

        String ime,prezime,sifra,titula;
        if(imeSvecenika.isEmpty()){
            ime = ovajSvecenik.getIme();
        }else{
            ime = imeSvecenika;}
        if(prezimeSvecenika.isEmpty()){
            prezime = ovajSvecenik.getPrezime();
        }else{prezime = prezimeSvecenika;}
        if(sifraSvecenika.isEmpty()){
            sifra = ovajSvecenik.getSifra();
        }else {
            try {
                ovajSvecenik.provjeraSifre(sifraSvecenika);
                sifra = sifraSvecenika;
            } catch (DuplikatSifreException e) {
                sifra = "";
                logger.error("Krivi unos! Dupla sifra, vec je registrirana u bazi!", e);
                displayAlert("Greška", e.getMessage());
                errorOccured = true;
            }
        }
        if(titulaSvecenika.isEmpty()){
            titula = ovajSvecenik.getTitula();
        }else{titula = titulaSvecenika;}

        if (imeSvecenika.isEmpty() && prezimeSvecenika.isEmpty() && sifraSvecenika.isEmpty() && titulaSvecenika.isEmpty()) {
            displayAlert("Greška", "Morate unijeti barem jedno od polja za izmjenu.");
            errorOccured=true;
        }

        if(!errorOccured) {


            Alert potvrdaAlert = new Alert(Alert.AlertType.CONFIRMATION);
            potvrdaAlert.setTitle("Potvrda izmjene");
            potvrdaAlert.setHeaderText("Jeste li sigurni da želite izmjeniti podatke svećenike?");


            ButtonType daButton = new ButtonType("Da");
            ButtonType neButton = new ButtonType("Ne");

            potvrdaAlert.getButtonTypes().setAll(daButton, neButton);

            Optional<ButtonType> rezultat = potvrdaAlert.showAndWait();

            if (rezultat.isPresent() && rezultat.get() == daButton) {

                Svecenik noviSvecenik = new Svecenik(ovajSvecenik.getId(), ime, prezime, sifra, titula, ovajSvecenik.getDatumRodjenja());

                BazaPodataka.azurirajSvecenika(noviSvecenik);

                Alert uspjehAlert = new Alert(Alert.AlertType.INFORMATION);
                uspjehAlert.setTitle("Izmjena svećenika");
                uspjehAlert.setHeaderText("Uspješno izmjenjeni podaci svećenika");
                uspjehAlert.showAndWait();
            }
        }
        initialize();
    }

    public void initialize(){
        List<Svecenik> listaSvecenika= BazaPodataka.dohvatiSveSvecenike();
        List<String> svecenikList = listaSvecenika.stream().map(p -> p.getSifra() + "-----"+p.getIme() + " " + p.getPrezime()).toList();
        odabirSvecenikaListView.setItems(FXCollections.observableList(svecenikList));

    }

    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
