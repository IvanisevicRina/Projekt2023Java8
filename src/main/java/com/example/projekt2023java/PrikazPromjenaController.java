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


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    private static volatile boolean changesAdded = false; // Flag to signal new changes




    public void initialize() throws Exception {
        changesAdded = false;
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lastRefreshLabel.setText("Last Refresh: " + currentTime);


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
                    BazaPodataka.spremiSvecenika(new Svecenik(599L,"Rina","Ivanišević", "22276","POP", null));


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
            changesWaitThread2.start();
            System.out.println("Unio sam novog svecenika, izbroji 10 sekundi dok vidis promjenu na ekranu!");


            Thread changesWaitThread = new Thread(() -> {
                synchronized (this) {
                    long startTime = System.currentTimeMillis();
                    long timeout = 10000; // Timeout in milliseconds
                    while (!changesAdded) {
                        try {
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            long remainingTime = timeout - elapsedTime;

                            if (remainingTime <= 0) {
                                break; // Break out of the loop if the timeout is reached
                            }

                            wait(remainingTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Platform.runLater(() -> {
                        try {
                            List<Promjene<?, ?>> newPromjeneList = promjeneManager.dohvatiSvePromjene();
                            promjeneTableView.getItems().setAll(newPromjeneList);
                            updateLastRefreshLabel();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
            changesWaitThread.setDaemon(true); // Set the thread as daemon to allow it to exit when the application exits
            changesWaitThread.start();
            updateLastRefreshLabel();
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

        changesAdded = false;
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

    public synchronized void signalChangesAdded() {
        changesAdded = true;
        notifyAll(); // Notify the waiting thread
    }
    private void updateLastRefreshLabel() {
        Platform.runLater(() -> {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            lastRefreshLabel.setText("Last Refresh: " + currentTime);
        });
    }


}

