package entitet;

import javafx.scene.control.Alert;

import java.util.List;

public class Imendan<T extends Osoba> {


    public void cestitajImendan(List<T> osobe, String imeSveca, String formattedDate) {

        StringBuilder message = new StringBuilder();
        message.append("Današnji datum: ").append(formattedDate).append("\nDanas je spomen sveca: ").append(imeSveca).append("\nSretan imendan:\n");
        for (T osoba : osobe) {
            message.append(osoba.getIme()).append(" ").append(osoba.getPrezime());
            if (osoba instanceof Svecenik) {
                message.append(" - Svećenik");
            } else if (osoba instanceof Zupljanin) {
                message.append(" - Župljanin");
            }
            message.append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Današnji Imendan");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());


        alert.showAndWait();
    }
}