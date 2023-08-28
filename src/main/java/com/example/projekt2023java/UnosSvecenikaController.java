package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
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


    public static void writeResult(String writeFileName, String text)
    {
        File f = new File(writeFileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append( "\n" +text);
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void spremiSvecenika() throws Exception {

        StringBuilder errorMessages = new StringBuilder();

        String imeSvecenika = imeSvecenikaTextField.getText();

        if(imeSvecenika.isEmpty()){
            errorMessages.append("Ime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(imeSvecenika);
            } catch (TekstualniZapisException e) {
                errorMessages.append("Greška" + e.getMessage() + "\n");
            }
        }

        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        if(prezimeSvecenika.isEmpty()){
            errorMessages.append("Prezime ne bi smjelo bit prazno!\n");
        }else  {
            try {
                sadrziBrojeve(prezimeSvecenika);
            } catch (TekstualniZapisException e) {
                errorMessages.append("Greška" + e.getMessage()+ "\n");
            }
        }


        String sifraSvecenika= sifraSvecenikaTextField.getText();

        if(sifraSvecenika.isEmpty()){
            errorMessages.append("Polje sifra ne bi smjelo bit prazno!\n");
        }

        String titulaSvecenika = titulaSvecenikaTextField.getText();


        if(titulaSvecenika.isEmpty()){
            errorMessages.append("Polje titula ne bi smjelo bit prazno!\n");
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




}
