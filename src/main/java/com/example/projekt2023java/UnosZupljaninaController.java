package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Sakrament;
import entitet.Zupljanin;
import entitet.ZupljaninBuilder;
import iznimke.PrekoracenjeBrojaZnakovaException;
import iznimke.TekstualniZapisException;
import iznimke.DuplikatSifreException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalLong;

public class UnosZupljaninaController {

    private static final Logger logger = LoggerFactory.getLogger(UnosZupljaninaController.class);


    @FXML
    private TextField prezimeZupljaninaTextField;
    @FXML
    private TextField imeZupljaninaTextField;

    @FXML
    private TextField sifraZupljaninaTextField;

    @FXML
    private DatePicker datumRodjenjaDatePicker;

    @FXML
    private ListView<String> odabirSakramentaListView;



    @FXML
    public void spremiZupljaninaButtonClicked(ActionEvent event) {
        try {
            spremiZupljanina();
            imeZupljaninaTextField.clear();
            prezimeZupljaninaTextField.clear();
            sifraZupljaninaTextField.clear();
            datumRodjenjaDatePicker.setValue(null);
            odabirSakramentaListView.getSelectionModel().clearSelection();
        } catch (DuplikatSifreException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText("Greška prilikom spremanja župljanina");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void spremiZupljanina() throws Exception {



        StringBuilder errorMessages = new StringBuilder();





        String imeZupljanina = imeZupljaninaTextField.getText();
        if(imeZupljanina.isEmpty()){
            errorMessages.append("Ime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(imeZupljanina);
                validateImeZupljanina(imeZupljanina);
            } catch (TekstualniZapisException | PrekoracenjeBrojaZnakovaException e) {
                errorMessages.append("Greška " + e.getMessage() + "\n");
            }
        }

        String prezimeZupljanina = prezimeZupljaninaTextField.getText();

        if(prezimeZupljanina.isEmpty()){
            errorMessages.append("Prezime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(prezimeZupljanina);
                validateImeZupljanina(prezimeZupljanina);
            } catch (TekstualniZapisException | PrekoracenjeBrojaZnakovaException e) {
                errorMessages.append("Greška " + e.getMessage()+ "\n");
            }
        }


        String sifraZupljanina = sifraZupljaninaTextField.getText();

        if(sifraZupljanina.isEmpty()){
            errorMessages.append("Polje sifre bi smjelo bit prazno!\n");
        }
        try{
            Zupljanin zupljanin = new ZupljaninBuilder().createZupljanin();
            zupljanin.provjeraSifre(sifraZupljanina);
            logger.info("unesena sifra:" + sifraZupljanina);
        }catch(DuplikatSifreException e){
            logger.error("Krivi unos!", e);
            errorMessages.append("Greška ").append(e.getMessage()).append("\n");
        }





        if(datumRodjenjaDatePicker.getValue() == null){
            errorMessages.append("Datum rodjenja ne bi smjelo bit prazno!\n");
        }

        if(errorMessages.isEmpty()) {


            String datumRodjenjaZupljanaString = datumRodjenjaDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));

            DateTimeFormatter formatterDatumaIspita = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

            LocalDate datumRodjenjaZupljana = LocalDate.parse(datumRodjenjaZupljanaString, formatterDatumaIspita);
            List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();

            OptionalLong maksimalniId = zupljani.stream()
                    .mapToLong(zupljanin -> zupljanin.getId()).max();


            if(maksimalniId.isEmpty()){
                maksimalniId = OptionalLong.of(0L);
            }

            long maxId = maksimalniId.getAsLong() +1;

            BazaPodataka.fixAutoicrementaZupljnaId(maxId);


            Zupljanin noviZupljanin = new Zupljanin( maksimalniId.getAsLong()+1,imeZupljanina,prezimeZupljanina,sifraZupljanina,datumRodjenjaZupljana);

            zupljani.add(noviZupljanin);

            int savedZupljaninId;
            try {
                savedZupljaninId =BazaPodataka.spremiZupljanina(noviZupljanin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            List<Sakrament> sviSakramenti = BazaPodataka.dohvatiSveSakramente();
            ObservableList<String> selectedItems = odabirSakramentaListView.getSelectionModel().getSelectedItems();

            for(Object o : selectedItems){
                for (Sakrament sakrament:sviSakramenti) {
                    if(o.equals(sakrament.getNaziv())){
                        BazaPodataka.spojiZupljaninaNaSakrament(sakrament.getId().intValue(),savedZupljaninId);
                    }
                }
            }






            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje zupljanina");
            alert.setHeaderText("Uspješno dodan novi zupljanin");
            alert.setContentText("Zupljanin " + prezimeZupljanina + " " + imeZupljanina + " je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje zupljanina NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi zupljanin");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }


    }
    public void initialize(){
        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        System.out.println("sakramenti");
        sakramenti.stream().map(p->p.getNaziv()).forEach(System.out::println);
        List<String> predmetList = sakramenti.stream().map(s -> s.getNaziv()).toList();
        odabirSakramentaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirSakramentaListView.setItems(FXCollections.observableList(predmetList));

    }
    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }


    private void validateImeZupljanina(String ime) throws PrekoracenjeBrojaZnakovaException {
        if (ime.length() > 30) {
            throw new PrekoracenjeBrojaZnakovaException("Prekoračenje dozvoljenog broja znakova za ime/prezime zupljanina.");
        }
    }




}
