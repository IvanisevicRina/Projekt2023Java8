package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.*;
import iznimke.NeispravanFormatVremenaException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UnosOsobnogSakramentaController {
    private static final Logger logger = LoggerFactory.getLogger(UnosOsobnogSakramentaController.class);


    @FXML
    private TextField vrijemeOdrzavanjaTextField;

    @FXML
    private TextField crkvaOsobnogSakramentaTextField;

    @FXML
    private DatePicker datumOdrzavanjaDatePicker;

    @FXML
    private ComboBox<String> odabirSakramentaComboBox;

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;

    @FXML
    private TextField liturgijaTextField;


    public static LiturgijskoRazdoblje liturgija(int broj_liturgije) {

        return switch (broj_liturgije) {
            case 1 -> LiturgijskoRazdoblje.DOSASCE;
            case 2 -> LiturgijskoRazdoblje.KORIZMA;
            default -> LiturgijskoRazdoblje.OSTATAK;
        };

    }


    public void unesiOsobnogSakramenta() throws Exception {
        StringBuilder errorMessages = new StringBuilder();



        if(odabirSakramentaComboBox.getValue() == null){
            errorMessages.append("Morate odabrati sakrament!\n");
        }
        if(odabirZupljaninaComboBox.getValue()== null){
            errorMessages.append("Morate odabrati zupljanina!\n");
        }
        String liturgijaSakramentaString = liturgijaTextField.getText();
        if (liturgijaSakramentaString.isEmpty()) {
            errorMessages.append("Polje liturgije ne bi smjelo bit prazno!\n");
        }


        String vrijemeOdrzavanja= vrijemeOdrzavanjaTextField.getText();

        if(vrijemeOdrzavanja.isEmpty()){
            errorMessages.append("Vrijeme ne bi smjelo bit prazno!\n");
        }else {
            try {
                validateVrijemeFormat(vrijemeOdrzavanja);
            } catch (NeispravanFormatVremenaException e) {
                logger.error("Krivi format vremena!", e);
                errorMessages.append("Greška: " + e.getMessage() + "\n");
            }
        }

        if(datumOdrzavanjaDatePicker.getValue() == null){
            errorMessages.append("Datum ne bi smjelo bit prazno!\n");
        }

        String crkvaOsobnogSakramenta = crkvaOsobnogSakramentaTextField.getText();

        if(crkvaOsobnogSakramenta.isEmpty()){
            errorMessages.append("Crkva ne bi smjelo bit prazno!\n");
        }



        if(errorMessages.isEmpty()) {

            String datumOdrzavanjaString = datumOdrzavanjaDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));

            String datumIVrijemeOsobnogSakramenta = datumOdrzavanjaString + vrijemeOdrzavanja;


            List<OsobniSakrament> osobniSakramenti = BazaPodataka.dohvatiSveOsobneSakramente();
            OptionalLong maksimalniId = OptionalLong.of(0L);
            maksimalniId=
                    osobniSakramenti.stream()
                            .mapToLong(s -> s.getId()).max();

            long id;
            if(maksimalniId.isEmpty()){
                id = 1L;
            }else {
                id = maksimalniId.getAsLong()+1;
            }

            List<Sakrament> sviSakramenti=BazaPodataka.dohvatiSveSakramente();
            Sakrament ovajSakrament=sviSakramenti.get(0);

            for (Sakrament sakrament: sviSakramenti) {
                if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                    ovajSakrament= sakrament;
                }
            }
            List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

            Zupljanin ovajZupljanin=sviZupljani.get(0);

            for (Zupljanin zupljanin:sviZupljani) {
                if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                    ovajZupljanin = zupljanin;
                }
            }

            DateTimeFormatter formatterDatumaIspita = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");

            LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeOsobnogSakramenta, formatterDatumaIspita);

            OsobniSakrament noviOsobniSakrament= new OsobniSakrament(id,ovajSakrament,ovajZupljanin,datumIVrijeme,new Crkva(crkvaOsobnogSakramenta),liturgija(Integer.parseInt(liturgijaSakramentaString)));
            noviOsobniSakrament.setLiturgijskoRazdoblje(liturgija(Integer.parseInt(liturgijaSakramentaString)));

            try {
                BazaPodataka.spremiOsobniSakrament(noviOsobniSakrament);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje osobnog sakramenta");
            alert.setHeaderText("Uspješno dodan novi osobni sakrament");
            alert.setContentText("Osobni sakrament je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje osobnog sakramenta NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi osobni sakrament");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }



    }

    public void initialize(){

        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        List<String> sakramentiList = sakramenti.stream().map(p->p.getNaziv()).toList();

        odabirSakramentaComboBox.setItems(FXCollections.observableList(sakramentiList));

        List<Zupljanin> zupljaniNaSakramentu= new ArrayList<>();
        for (Sakrament sakrament:sakramenti) {
            if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                Sakrament ovajSakrament= sakrament;
                List<OsobniSakrament> osobniSakramenti = BazaPodataka.dohvatiSveOsobneSakramente();
                Set<Zupljanin> zupljaniniSet = new HashSet<>();

                for (OsobniSakrament osobniSakrament : osobniSakramenti) {
                    if (osobniSakrament.getSakrament().getSifra().equals(ovajSakrament.getSifra())) {
                        zupljaniniSet.add(osobniSakrament.getZupljanin());
                    }
                }
                System.out.println("ZUPLJANI U SETU:");
                for (Zupljanin zupljanin : zupljaniniSet) {
                    System.out.println(zupljanin.getIme() + " " + zupljanin.getPrezime());
                }


                System.out.println("Zupljani sa sakramentom: "+ovajSakrament.getNaziv());
                for (Zupljanin z:ovajSakrament.getZupljani()) {
                    boolean found = false;
                    for (Zupljanin zupljanin : zupljaniniSet) {
                        if (z.getIme().equals(zupljanin.getIme()) && z.getPrezime().equals(zupljanin.getPrezime())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        zupljaniNaSakramentu.add(z);
                        System.out.println(z.getIme() + " " + z.getPrezime());
                    }

                }

            }
        }

        List<String> zupljaninList = zupljaniNaSakramentu.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));



    }
    private void validateVrijemeFormat(String vrijeme) throws NeispravanFormatVremenaException {
        // Implement your validation logic here
        // You can use regular expressions to match the desired time format
        if (!vrijeme.matches("^\\d{2}:\\d{2}$")) {
            throw new NeispravanFormatVremenaException("Neispravan format vremena. Očekivani format: HH:mm");
        }
    }





}
