package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Guns_Issue implements Initializable {

    @FXML
    private MFXTextField GunIdTaker;
    @FXML
    public static MFXButton IssueGunBtn;

    @FXML
    private MFXTextField MemberIdTaker;

    DbConn connect;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        System.out.println(Member_list.selectedPesel);
        MemberIdTaker.setText(Member_list.selectedPesel);


    }
    public void IssueGun(ActionEvent event) {
        String number = GunIdTaker.getText();
        //Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Potwierdz użyczenie broni");
        alert.setTitle("POTWIERDZANIE");
        alert.setHeaderText("Czy jesteś pewien że chcesz użyczyć broń ?");
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get().equals(ButtonType.OK)){
            String query = "INSERT INTO uzyczenia (bronId,klubowiczId) VALUES ('"+number+"','"+Member_list.selectedPesel+"')";
            String query2 = "UPDATE bron SET dostepnosc = false WHERE nr_fabryczny = '"+number+"'";
            if(connect.execAction(query) && connect.execAction(query2)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("Ok!!!");
                alert1.setTitle(null);
                alert1.setHeaderText("Użyczanie broni zakończone!!!");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Błąd!!!");
                alert1.setTitle(null);
                alert1.setHeaderText("Użyczanie bronie się nie powiodło!!!");
                alert1.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Anulowano!!!");
            alert1.setTitle(null);
            alert1.setHeaderText("Anulowano użyczanie broni!!!");
            alert1.showAndWait();
        }
    }
}
