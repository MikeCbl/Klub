package Klub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        
        AnchorPane root = FXMLLoader.load(getClass().getResource("FXML/Login_page_MFX.fxml"));
        primaryStage.resizableProperty().setValue(false);// zablokuj guzik od powiekszenia okna

        //Ustaw ikone pzss-u
        primaryStage.getIcons().add(new Image(getClass().getResource("IMG/target.png").openStream()));


        // System.out.println(getClass().getResource("IMG/pzss.png")); //sprawdz czy zwraca null czy scierzke do pliku
        primaryStage.setTitle("LOGOWANIE");

        Screen screen = Screen.getPrimary();
        Scene scene = new  Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
