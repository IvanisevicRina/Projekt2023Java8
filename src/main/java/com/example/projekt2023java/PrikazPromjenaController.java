package com.example.projekt2023java;
import baza.BazaPodataka;
import entitet.Promjene;
import entitet.PromjeneManager;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import niti.NotificationManager;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static niti.ThreadColor.ANSI_BLUE;
import static niti.ThreadColor.ANSI_PURPLE;

public class PrikazPromjenaController   {

    @FXML
    private TableView<Promjene<?, ?>> promjeneTableView;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> staraVrijednostColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> novaVrijednostColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> objektPromjeneColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> opisColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> rolaColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> datumIVrijemeColumn;
    private PromjeneManager promjeneManager = new PromjeneManager();

    @FXML
    private Label lastRefreshLabel;
    @FXML
    private Button notificationButton;



    public void initialize() throws Exception {
        notificationButton.setStyle("-fx-background-color: #00FF00;"); // Green color

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lastRefreshLabel.setText("Last Refresh: " + currentTime);
        NotificationManager.getInstance().registerController(this);
        notificationButton.setOnAction(event -> {
            // Call the refreshTableContent method when the button is clicked
            refreshTableContent();
            // Change the button's style back to green
            notificationButton.setStyle("-fx-background-color: #00FF00;"); // Green color
        });

        try{
            List<Promjene<?,?>> promjeneList = promjeneManager.dohvatiSvePromjene();
            if (promjeneList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Recent Changes");
                alert.setHeaderText("Warning");
                alert.setContentText("There are no recent changes available.");
                alert.showAndWait();
            }
            Platform.runLater(() -> setupUI(promjeneList));



            // Schedule automatic refresh every 30 seconds
            Duration refreshDuration = Duration.seconds(60);
            KeyFrame refreshKeyFrame = new KeyFrame(refreshDuration, event -> refreshTableContent());
            Timeline refreshTimeline = new Timeline(refreshKeyFrame);
            refreshTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
            refreshTimeline.play();

            addChangesWaitThread(10000, "Pavao", "PrviĆ");
            addChangesWaitThread(30000, "Drago", "Drugić");
            addChangesWaitThread(50000, "Tvrtko", "Trečić");
            addChangesWaitThread(70000, "Čiril", "Četvrtić");



            Thread changesWaitThread = new Thread(() -> {
                synchronized (this) {
                    try {

                        while (true) {
                            System.out.println(ANSI_BLUE +"CHANGESWAIT THREAD OVDJE vama na usluzi");
                            NotificationManager.getInstance().waitForNotification();
                            Thread.sleep(5000); // Adjust the sleep duration as needed

                            Platform.runLater(() -> {
                                try {
                                    System.out.println(ANSI_PURPLE +"I have been notify! Promjena obrađena, Sada cu refreshati tablicu sadrzaja promjena");
                                    Thread.sleep(5000);
                                    refreshTableContent();
                                    System.out.println("jesam");
                                } catch (Exception e) {
                                    e.printStackTrace(); // Print the exception for debugging

                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            changesWaitThread.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
    private void refreshTableContent() {
        try {
            List<Promjene<?, ?>> newPromjeneList = promjeneManager.dohvatiSvePromjene();
            promjeneTableView.getItems().setAll(newPromjeneList);
            updateLastRefreshLabel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void setupUI(List<Promjene<?, ?>> promjeneList) {
        staraVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("staraVrijednost"));
        novaVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("novaVrijednost"));
        objektPromjeneColumn.setCellValueFactory(new PropertyValueFactory<>("objektPromjene"));
        opisColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
        rolaColumn.setCellValueFactory(new PropertyValueFactory<>("rola"));
        datumIVrijemeColumn.setCellValueFactory(new PropertyValueFactory<>("datumIVrijeme"));
        promjeneTableView.getItems().setAll(promjeneList);
    }


    private void updateLastRefreshLabel() {
        Platform.runLater(() -> {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            lastRefreshLabel.setText("Last Refresh: " + currentTime);
        });
    }
    @FXML
    private void handleNotificationButtonClick(ActionEvent event) {
        // Call the refreshTableContent method when the button is clicked
        refreshTableContent();
        // Change the button's style back to green
        notificationButton.setStyle("-fx-background-color: #00FF00;"); // Green color
    }

    private void addChangesWaitThread(int sleepDuration, String firstName, String lastName) {
        Thread changesWaitThread = new Thread(() -> {
            try {
                Thread.sleep(sleepDuration);
                BazaPodataka.spremiSvecenika(new SvecenikBuilder().setIme(firstName).setPrezime(lastName).setSifra("sifra").setTitula("titula").createSvecenik());
                System.out.println("Unio sam novog svecenika, izbroji " + sleepDuration / 1000 + " sekundi dok vidis promjenu na ekranu!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        changesWaitThread.start();
    }

}

