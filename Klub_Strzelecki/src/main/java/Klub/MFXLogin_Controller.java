package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MFXLogin_Controller implements Initializable {
    @FXML
    private MFXButton login_button;
    @FXML
    private MFXPasswordField password;
    @FXML
    private MFXTextField username;
    LinkedList<Login_Info> Login = new LinkedList<Login_Info>();

    public AnchorPane login_page;



    DbConn connect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
    }

    public void LoadDate(){
        String SELECT_LOGIN_QUERY = "select * from login";
        ResultSet resultSet = connect.execQuery(SELECT_LOGIN_QUERY);
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String User = resultSet.getString("name");
                String Pass = resultSet.getString("password");
                Login.add(new Login_Info(id,User,Pass));
            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public boolean validate(String user, String password) throws SQLException {
        int count_login_index = 0;
        LoadDate();
        while(Login.size()!=count_login_index){
            count_login_index++;
            if(Login.element().getUser().equals(user) && Login.element().getPass().equals(password)){
                return true;
            }else{
                System.out.println("Nie istnieje");
                return false;
            }
        }
        return false;
    }



    //w fxml defaultButton="true" dzieki temu klaiwsz enter dziala (bez tego tez )
    //https://openjfx.io/javadoc/17/javafx.controls/javafx/scene/control/Button.html

    public void submitbutton(ActionEvent event) throws SQLException, IOException,Exception {


        Window owner = login_button.getScene().getWindow();

        if(username.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Błąd logowania!",
                    "Proszę podać Nazwę Użytkownika");
            return;
        }
        if(password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Błąd logowania!",
                    "Proszę podać Hasło");
            return;
        }

                String user = username.getText();
                String passwords = password.getText();

                DbConn jdb = new DbConn();
                boolean flag = validate(user, passwords);

                if(!flag) {
                    infoBox("Proszę podać poprawne Email i Hasło", null, "Błąd");
                }else {
//                    infoBox("Login Successful!", null, "Successful!!!!");
                    closeStage();
                    LoadMain();
                }
    }

            public  void  closeStage(){
                ((Stage)username.getScene().getWindow()).close();
            }

            //load the main pannel
            public void LoadMain(){
                 try{
                     Parent parent = FXMLLoader.load(getClass().getResource("FXML/Pannel.fxml"));
                     Stage stage = new Stage();
                     //powieksz okno
                     stage.setMaximized(true);
                     //ustaw ikone pzss-u
                     stage.getIcons().add(new Image(getClass().getResource("IMG/target.png").openStream()));

                     stage.setTitle("Zarządzanie Klubem Strzeleckim");
                     stage.setScene(new Scene(parent));
                     stage.show();
                 }
                 catch (IOException e){
                     e.printStackTrace();
                 }
            }

    private static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);


        ButtonType buttonTypeOne = new ButtonType("Ok");
        ButtonType buttonTypeTwo = new ButtonType("Anuluj");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
