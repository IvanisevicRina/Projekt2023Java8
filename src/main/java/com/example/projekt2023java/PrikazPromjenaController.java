package com.example.projekt2023java;
import baza.BazaPodataka;
import entitet.Promjene;
import entitet.PromjeneManager;
import entitet.Svecenik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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


    private static final CountDownLatch latch = new CountDownLatch(1);


    public void initialize() throws Exception {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lastRefreshLabel.setText("Last Refresh: " + currentTime);
        NotificationManager.getInstance().registerController(this);


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


            Thread changesWaitThread2 = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    BazaPodataka.spremiSvecenika(new Svecenik(599L,"Nisam","Perić", "22276","POP", null));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
            changesWaitThread2.start();
            System.out.println("Unio sam novog svecenika, izbroji 10 sekundi dok vidis promjenu na ekranu!");


            Thread changesWaitThread = new Thread(() -> {
                synchronized (this) {
                    try {

                        while (true) {
                            System.out.println(ANSI_BLUE +"CHANGESWAIT THREAD OVDJE vama na usluzi");
                            NotificationManager.getInstance().waitForNotification();
                            System.out.println(ANSI_BLUE +"Nisam dobio nikakvu uzbunu, tako da ću biti na stand by dok me ne obavjestiš da imam posla");
                            Platform.runLater(() -> {
                                try {
                                    System.out.println(ANSI_PURPLE +"I have been notify! Promjena obrađena, Sada cu refreshati tablicu sadrzaja promjena");
                                    refreshTableContent();
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



/*                  SEMAFOR
        try {
            SemaphoreManager.acquire();

            // Update the display of promjene
            promjeneTableView.getItems().addAll(promjene);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            SemaphoreManager.release();
        }
 */
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


}

