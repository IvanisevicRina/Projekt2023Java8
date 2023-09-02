package entitet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PromjeneSerijalizer implements Serializable{



    public static void pisiPromjene(List<Promjene<?, ?>> listaPromjena, String putanja) {

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(putanja))) {
                outputStream.writeObject(listaPromjena);
                System.out.println("Promjene su uspješno zapisane u datoteku.");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public static List<Promjene<?, ?>> citajPromjene(String putanja) {

            List<Promjene<?, ?>> listaPromjena = new ArrayList<>();
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(putanja))) {
                listaPromjena = (List<Promjene<?, ?>>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return listaPromjena;

    }

}
