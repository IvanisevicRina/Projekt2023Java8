package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
import entitet.Zupljanin;
import iznimke.DuplikatSifreException;
import iznimke.PrekoracenjeBrojaZnakovaException;
import iznimke.TekstualniZapisException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.List;
import java.util.OptionalLong;

public class UnosSvecenikaController implements Serializable {


    @FXML
    private TextField imeSvecenikaTextField;
    @FXML
    private TextField sifraSvecenikaTextField;
    @FXML
    private TextField prezimeSvecenikaTextField;
    @FXML
    private TextField titulaSvecenikaTextField;



    public void spremiSvecenika() throws Exception {

        StringBuilder errorMessages = new StringBuilder();

        String imeSvecenika = imeSvecenikaTextField.getText();

        if(imeSvecenika.isEmpty()){
            errorMessages.append("Ime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(imeSvecenika);
                validateImeSvecenika(imeSvecenika);
            } catch (TekstualniZapisException  | PrekoracenjeBrojaZnakovaException e) {
                errorMessages.append("Greška" + e.getMessage() + "\n");
            }
        }

        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        if(prezimeSvecenika.isEmpty()){
            errorMessages.append("Prezime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(prezimeSvecenika);
                validateImeSvecenika(prezimeSvecenika);
            } catch (TekstualniZapisException | PrekoracenjeBrojaZnakovaException e) {
                errorMessages.append("Greška" + e.getMessage()+ "\n");
            }
        }


        String sifraSvecenika= sifraSvecenikaTextField.getText();

        if(sifraSvecenika.isEmpty()){
            errorMessages.append("Polje sifra ne bi smjelo bit prazno!\n");
        }else {
            try {
                validateSifraSvecenika(sifraSvecenika);
                provjeraSifri(sifraSvecenika);
            } catch (PrekoracenjeBrojaZnakovaException | DuplikatSifreException e) {
                errorMessages.append("Greška: " + e.getMessage() + "\n");
            }
        }

        String titulaSvecenika = titulaSvecenikaTextField.getText();


        if(titulaSvecenika.isEmpty()){
            errorMessages.append("Polje titula ne bi smjelo bit prazno!\n");
        }else {
            try {
                validateTitulaSvecenika(sifraSvecenika);
            } catch (PrekoracenjeBrojaZnakovaException e) {
                errorMessages.append("Greška: " + e.getMessage() + "\n");
            }
        }



        if(errorMessages.isEmpty()) {



            List<Svecenik> svecenici = BazaPodataka.dohvatiSveSvecenike();

            OptionalLong maksimalniId =svecenici.stream()
                    .mapToLong(sveceniks -> sveceniks.getId()).max();
            long id;
            if(maksimalniId.isEmpty()){
                id = 1L;
            }else {
                id = maksimalniId.getAsLong()+1;
            }



            System.out.println("Maksimalni id grupe " + maksimalniId);
            System.out.println("Maksimalni id svecenika ;" + id);
            BazaPodataka.fixAutoicrementaSvecenikaId(id);

            Svecenik noviSvecenik= new SvecenikBuilder().setId(id).setSifra(sifraSvecenika).setIme(imeSvecenika).setPrezime(prezimeSvecenika).setTitula(titulaSvecenika).createSvecenik();

                try {
                    BazaPodataka.spremiSvecenika(noviSvecenik);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            imeSvecenikaTextField.clear();
            prezimeSvecenikaTextField.clear();
            sifraSvecenikaTextField.clear();
            titulaSvecenikaTextField.clear();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje svecenika");
            alert.setHeaderText("Uspješno dodan novi svecenik");
            alert.setContentText("Svecenik " + prezimeSvecenika + " " + imeSvecenika+ " je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje svecenika NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi svecenik");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }

    }

    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }
    private void validateSifraSvecenika(String sifra) throws PrekoracenjeBrojaZnakovaException {
        if (sifra.length() > 10) {
            throw new PrekoracenjeBrojaZnakovaException("Prekoračenje dozvoljenog broja znakova za sifru svecenika.");
        }
    }
    private void validateImeSvecenika(String ime) throws PrekoracenjeBrojaZnakovaException {
        if (ime.length() > 30) {
            throw new PrekoracenjeBrojaZnakovaException("Prekoračenje dozvoljenog broja znakova za ime/prezime svecenika.");
        }
    }
    private void validateTitulaSvecenika(String titula) throws PrekoracenjeBrojaZnakovaException {
        if (titula.length() > 50) {
            throw new PrekoracenjeBrojaZnakovaException("Prekoračenje dozvoljenog broja znakova za titulu svecenika.");
        }
    }
    private void provjeraSifri(String sifra) throws DuplikatSifreException {
        List<Svecenik> svecenici = BazaPodataka.dohvatiSveSvecenike();
        for (Svecenik svecenik : svecenici) {
            if (svecenik.getSifra().equals(sifra)) {
                throw new DuplikatSifreException("Svecenik sa istom šifrom već postoji!");
            }
        }
    }





}
