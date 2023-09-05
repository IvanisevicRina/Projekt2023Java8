package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.Zupljanin;
import entitet.ZupljaninBuilder;
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

/**
 * ekran izmjene župljana.
 */
public class IzmjenaZupljanaController {
    @FXML
    private TextField imeZupljaninaTextField;
    @FXML
    private TextField sifraZupljaninaTextField;
    @FXML
    private TextField prezimeZupljaninaTextField;

    @FXML
    private ListView<String> odabirZupljaninaListView;
    private static final Logger logger = LoggerFactory.getLogger(IzmjenaZupljanaController.class);


    /**
     * ažurira podataka o župljaninu.
     *
     * @throws Exception baca se ko dođe do greške pri pristupu bazi podataka ili pri unosu podataka.
     */
    public void azurirajZupljane() throws Exception {
        String imeZupljana = imeZupljaninaTextField.getText();
        String prezimeZupljanina = prezimeZupljaninaTextField.getText();
        String sifraZupljanina = sifraZupljaninaTextField.getText();
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();
        Zupljanin ovajZupljanin = new ZupljaninBuilder().createZupljanin();
        ObservableList<String> selectedItems = odabirZupljaninaListView.getSelectionModel().getSelectedItems();

        Boolean errorOccured = false;

        if (imeZupljana.isEmpty() && prezimeZupljanina.isEmpty() && sifraZupljanina.isEmpty() ) {
            displayAlert("Greška", "Morate unijeti barem jedno od polja za izmjenu.");
            errorOccured=true;
        }
        if (selectedItems.isEmpty()) {
            displayAlert("Greška", "Morate odabrati župljanina za ažuriranje.");
            return;
        }

        try {
            sadrziBrojeve(imeZupljana);
            sadrziBrojeve(prezimeZupljanina);

        } catch (TekstualniZapisException e) {
            imeZupljana = "";
            prezimeZupljanina = "";
            errorOccured = true;
            displayAlert("Greška", e.getMessage());
        }
        for (Object o : selectedItems) {
            for (Zupljanin zupljanin : sviZupljani) {
                if (o.equals(zupljanin.getSifra() + "-----" + zupljanin.getIme() + " " + zupljanin.getPrezime())) {
                    ovajZupljanin = zupljanin;
                }
            }
        }
        String ime, prezime, sifra;
        if (imeZupljana.isEmpty()) {
            ime = ovajZupljanin.getIme();
        } else {
            ime = imeZupljana;
        }
        if (prezimeZupljanina.isEmpty()) {
            prezime = ovajZupljanin.getPrezime();
        } else {
            prezime = prezimeZupljanina;
        }
        if (sifraZupljanina.isEmpty()) {
            sifra = ovajZupljanin.getSifra();
        } else {
            try {
                ovajZupljanin.provjeraSifre(sifraZupljanina);


                sifra = sifraZupljanina;
            } catch (DuplikatSifreException e) {
                logger.error("Krivi unos! Dupla sifra, vec je registrirana u bazi!", e);
                sifra = "";
                displayAlert("Greška", e.getMessage());
                errorOccured = true;
            }
        }


        if (!errorOccured) {


            Alert potvrdaAlert = new Alert(Alert.AlertType.CONFIRMATION);
            potvrdaAlert.setTitle("Potvrda izmjene");
            potvrdaAlert.setHeaderText("Jeste li sigurni da želite izmjenit podatke župljana?");


            ButtonType daButton = new ButtonType("Da");
            ButtonType neButton = new ButtonType("Ne");

            potvrdaAlert.getButtonTypes().setAll(daButton, neButton);

            Optional<ButtonType> rezultat = potvrdaAlert.showAndWait();

            if (rezultat.isPresent() && rezultat.get() == daButton) {

                Zupljanin noviZupljanin = new ZupljaninBuilder().id(ovajZupljanin.getId()).ime(ime).prezime(prezime).sifra(sifra).datumRodjenja(ovajZupljanin.getDatumRodjenja()).createZupljanin();

                BazaPodataka.azurirajZupljane(noviZupljanin);

                Alert uspjehAlert = new Alert(Alert.AlertType.INFORMATION);
                uspjehAlert.setTitle("Izmjena župljanina");
                uspjehAlert.setHeaderText("Uspješno izmjenjeni podaci župljanina");
                uspjehAlert.showAndWait();
            }
        }
        initialize();
    }

    /**
     * služi za inicijalizaciju ekrana.
     */
    public void initialize() {
        List<Zupljanin> listaZupljana = BazaPodataka.dohvatiSveZupljane();
        List<String> zupljaniList = listaZupljana.stream().map(p -> p.getSifra() + "-----" + p.getIme() + " " + p.getPrezime()).toList();
        odabirZupljaninaListView.setItems(FXCollections.observableList(zupljaniList));

    }

    /**
     * Metoda za provjeru da li tekst sadrži brojeve.
     *
     * @param text Tekst koji se provjerava.
     * @throws TekstualniZapisException Ako tekst sadrži brojeve.
     */
    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }

    /**
     * Metoda za prikazivanje upozorenja (Alert) korisniku.
     *
     * @param title   Naslov upozorenja.
     * @param message Poruka upozorenja.
     */
    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
