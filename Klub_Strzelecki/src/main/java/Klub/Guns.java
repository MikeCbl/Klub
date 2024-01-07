package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAll;
import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;

public class Guns implements Initializable {

    @FXML
    private MFXTextField brand_gun;
    @FXML
    private MFXTextField caliber_gun;
    @FXML
    private MFXTextField kind_gun;
    @FXML
    private MFXTextField number_gun;
    @FXML
    private MFXTextField productionDate_gun;
    @FXML
    private MFXFilterComboBox<String> type_gun_combo;
    @FXML
    private MFXButton gun_insertion;
    @FXML
    private Label validationLabelKind;
    @FXML
    private Label validationLabelBrand;
    @FXML
    private Label validationLabelNumber;
    @FXML
    private Label validationLabelYear;
    @FXML
    private Label validationLabelCal;


    //Editable
    private Boolean IsEditable = Boolean.FALSE;
    DbConn connect;

    //lista typów broni
    String[] typeList = {"","Sportowa", "Półautomatyczna", "Samopowtarzalna", "Gładkolufowa", "Czarnoprochowa"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        type_gun_combo.getItems().addAll(typeList);
        runValidation();

    }


    public void runValidation(){
        ValidationUtil.addValidation(kind_gun, validationLabelKind, "textField");
        ValidationUtil.addValidation(brand_gun, validationLabelBrand, "textField");
        ValidationUtil.addValidation(number_gun, validationLabelNumber, "nr_fab");
        ValidationUtil.addValidation(productionDate_gun, validationLabelYear, "year");
        ValidationUtil.addValidation(caliber_gun, validationLabelCal, "empty");
    }

    public void GunInserted(ActionEvent actionEvent) throws SQLException {
        Window owner = gun_insertion.getScene().getWindow();

//        runValidation();

        if (!kind_gun.isValid()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Proszę poprawić błędy");
            return;
        }
        if (!brand_gun.isValid()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Proszę poprawić błędy");
            return;
        }if(!number_gun.isValid()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Proszę poprawić numer fabryczny");
            return;
        }if(!productionDate_gun.isValid()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Proszę poprawić rok produkcji");
            return;
        }if(!caliber_gun.isValid()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Proszę poprawić kaliber");
            return;
        }
//        if(kind_gun.getText().isEmpty()) {
//            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                    "Proszę podać Rodzaj");
//            return;
//        }


        //Edit check
        if(IsEditable){
            handleEditMethod();
            return;
        }

        String kind = kind_gun.getText();
        String type = type_gun_combo.getValue();
        String brand = brand_gun.getText();
        String caliber = caliber_gun.getText();
        String number = number_gun.getText();
        String productionDate = productionDate_gun.getText();

        int flag = connect.insert_Guns_query_Executer(kind,type,brand,caliber,number,productionDate);
        if(flag == 1){
            infoBox("Dodano broń!", null, "Ok");
        }
        else{
            infoBox("nie powiodło się", null, "Błąd");
        }
    }

    public void UpdateInformation(GunsCollection.Guns gun){
        kind_gun.setText(gun.getKind());
        type_gun_combo.setText(gun.getType());
        brand_gun.setText(gun.getBrand());
        caliber_gun.setText(gun.getCaliber());
        number_gun.setText(gun.getNumber());
        productionDate_gun.setText(gun.getProductionDate());

        number_gun.setEditable(false);
        number_gun.setDisable(true);
        IsEditable = Boolean.TRUE;
    }
    public String timestamp(){
        return "";
    }


    //update query for edit
    private void handleEditMethod() {
        GunsCollection.Guns gun = new GunsCollection.Guns(kind_gun.getText(),type_gun_combo.getText(),brand_gun.getText(),caliber_gun.getText(),number_gun.getText(),productionDate_gun.getText(),timestamp(),true);
        if(connect.updateGun(gun)){
            AlertMaker.showAlert("Ok!!","Broń edytowana");
            System.out.println(timestamp());
        }else{
            AlertMaker.showError("Błąd!","Edycja nie powiodła się!");
        }
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    private static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }


}
