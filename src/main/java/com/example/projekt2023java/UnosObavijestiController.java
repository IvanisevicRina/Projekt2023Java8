package com.example.projekt2023java;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class UnosObavijestiController {

    @FXML
    private TextField radnimDanTextField;

    @FXML
    private TextField subotaTextField;

    @FXML
    private TextField nedjeljaTextField;

    public void spremiObavijesti() {
        String radnimDanObavijest = radnimDanTextField.getText();
        String subotaObavijest = subotaTextField.getText();
        String nedjeljaObavijest = nedjeljaTextField.getText();

        try (FileWriter writer = new FileWriter("dat/obavijesti.txt")) {
            writer.write(radnimDanObavijest + "\n");
            writer.write(subotaObavijest + "\n");
            writer.write(nedjeljaObavijest + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Optionally, provide feedback to the admin that the data has been saved.
        System.out.println("Obavijesti su spremljene.");
    }
}