package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Crkva;
import entitet.LiturgijskoRazdoblje;
import entitet.OsobniSakrament;
import entitet.Svecenik;
import iznimke.NeispravanFormatVremenaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IzmjenaOsobnogSakramentaConteoller {

    @FXML
    private TextField vrijemeOdrzavanjaTextField;

    @FXML
    private DatePicker datumOdrzavanjaDatePicker;

    @FXML
    private TextField crkvaTextField;
    @FXML
    private ListView<String> odabirOsobnogSakramentaListView;
    @FXML
    private ComboBox<String> liturgijaComboBox; // Define a ComboBox for liturgy selection
    private static final Logger logger = LoggerFactory.getLogger(IzmjenaOsobnogSakramentaConteoller.class);




    public void azurirajOsobniSakrament() throws Exception {

        String vrijemeOdrzavanja = vrijemeOdrzavanjaTextField.getText();


        LocalDate selectedDate = datumOdrzavanjaDatePicker.getValue();


        String selectedLiturgija = liturgijaComboBox.getValue();


        String crkvaString = crkvaTextField.getText();
        Crkva crkva = new Crkva(crkvaString);

        List<OsobniSakrament> sviOsobniSakramenti= BazaPodataka.dohvatiSveOsobneSakramente();
        OsobniSakrament ovajOsobniSakrament = null;
        ObservableList<String> selectedItems = odabirOsobnogSakramentaListView.getSelectionModel().getSelectedItems();
        Boolean errorOccured = false;
        if (selectedItems.isEmpty()) {
            displayAlert("Greška", "Morate odabrati osobni sakrament za ažuriranje.");
            return;
        }
        for(Object o : selectedItems) {
            for (OsobniSakrament osobniSakrament : sviOsobniSakramenti) {
                if (o.equals(osobniSakrament.getId() + "-----" + osobniSakrament.getSakrament().getNaziv() + " " + osobniSakrament.getZupljanin().getIme() + " " + osobniSakrament.getZupljanin().getPrezime())) {
                    ovajOsobniSakrament=osobniSakrament;
                }
            }
        }
        LocalDateTime dateTime = ovajOsobniSakrament.getDatumIVrijeme();
        String dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        String vrijeme, datum, liturgija, lokacija;
        if(vrijemeOdrzavanja.isEmpty()){
            vrijeme=dateTimeString.substring(11);
        }else{
            try {
                validateVrijemeFormat(vrijemeOdrzavanja);
                vrijeme=vrijemeOdrzavanja;
            } catch (NeispravanFormatVremenaException e) {

                logger.error("Krivi format vremena!", e);
                vrijeme = dateTimeString.substring(11);
                displayAlert("Greška: ",e.getMessage());
                errorOccured=true;
            }

        }
        if(selectedDate == null){
            datum=ovajOsobniSakrament.getDatumIVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
        }else{
            datum=selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));

        }
        if(selectedLiturgija ==null){
            liturgija=ovajOsobniSakrament.getLiturgijskoRazdoblje().name();

        }else{
            liturgija=selectedLiturgija;
        }
        if(crkvaString.isEmpty()){
            lokacija=ovajOsobniSakrament.getCrkva().nazivCrkve();
        }else{
            lokacija=crkvaString;
        }

        String datumIVrijemeOsobnogSakramenta1 = datum  + vrijeme;

        DateTimeFormatter formatterDatumaIspita = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");

        LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeOsobnogSakramenta1, formatterDatumaIspita);

        if(!errorOccured) {
            OsobniSakrament noviOsobniSakrament = new OsobniSakrament(ovajOsobniSakrament.getId(), ovajOsobniSakrament.getSakrament(), ovajOsobniSakrament.getZupljanin(), datumIVrijeme, new Crkva(lokacija), convertStringToEnum(liturgija));

            BazaPodataka.azurirajOsobniSakrament(noviOsobniSakrament);
        }


    }


    private void validateVrijemeFormat(String vrijeme) throws NeispravanFormatVremenaException {
        // Implement your validation logic here
        // You can use regular expressions to match the desired time format
        if (!vrijeme.matches("^\\d{2}:\\d{2}$")) {
            throw new NeispravanFormatVremenaException("Neispravan format vremena. Očekivani format: HH:mm");
        }
    }
    public void initialize(){
        List<OsobniSakrament> listaOsobnihSakramenata= BazaPodataka.dohvatiSveOsobneSakramente();
        List<String> osobniSakramentiList = listaOsobnihSakramenata.stream().map(p -> p.getId()+ "-----"+ p.getSakrament().getNaziv() + " " + p.getZupljanin().getIme() + " " + p.getZupljanin().getPrezime()).toList();
        odabirOsobnogSakramentaListView.setItems(FXCollections.observableList(osobniSakramentiList));
        ObservableList<String> liturgijaOptions = FXCollections.observableArrayList(LiturgijskoRazdoblje.DOSASCE.name(), LiturgijskoRazdoblje.KORIZMA.name(), LiturgijskoRazdoblje.OSTATAK.name());
        liturgijaComboBox.setItems(liturgijaOptions);

    }
    public static LiturgijskoRazdoblje convertStringToEnum(String input) {
        for (LiturgijskoRazdoblje razdoblje : LiturgijskoRazdoblje.values()) {
            if (razdoblje.name().equals(input)) {
                return razdoblje;
            }
        }
        return null;
    }
    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}



