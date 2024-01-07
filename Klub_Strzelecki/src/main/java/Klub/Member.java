package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.others.dates.DateStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Window;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class Member implements Initializable {
    @FXML
    private MFXTextField address_member;
    @FXML
    private MFXDatePicker birthDate_member;
    @FXML
    private MFXTextField birthPlace_member;
    @FXML
    private MFXDatePicker date_member;
    @FXML
    private MFXTextField email_member;
    @FXML
    private MFXComboBox<String> gender_member;
    @FXML
    private MFXTextField lastName_member;
    @FXML
    private MFXButton member_insertion;
    @FXML
    private MFXTextField name_member;
    @FXML
    private MFXTextField pesel_member;
    @FXML
    private MFXTextField phone_member;

    //Editable
    private Boolean IsEditable = Boolean.FALSE;

    String[] genderList = {"K", "M", "O"};

    @FXML
    private Label validationLabelAddress;

    @FXML
    private Label validationLabelBirthDate;

    @FXML
    private Label validationLabelBirthPlace;

    @FXML
    private Label validationLabelDate;

    @FXML
    private Label validationLabelEmail;

    @FXML
    private Label validationLabelGender;

    @FXML
    private Label validationLabelLastName;

    @FXML
    private Label validationLabelName;

    @FXML
    private Label validationLabelPesel;

    @FXML
    private Label validationLabelPhone;

    DbConn connect;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        gender_member.getItems().addAll(genderList);

        //Wbudowane metody (Date picker domyslnie po wybraniu daty ma format dd MMM yyyy)
        //sql date type przyjmuje yyyy-MM-dd
        date_member.setConverterSupplier(() -> new DateStringConverter("yyyy-MM-dd", date_member.getLocale()));
        birthDate_member.setConverterSupplier(() -> new DateStringConverter("yyyy-MM-dd", birthDate_member.getLocale()));
        runValidation();
    }
    public void runValidation(){
        ValidationUtil.addValidation(name_member, validationLabelName, "textField");
        ValidationUtil.addValidation(lastName_member, validationLabelLastName, "textField");
        ValidationUtil.addValidation(gender_member, validationLabelGender, "empty");
        ValidationUtil.addValidation(birthDate_member, validationLabelBirthDate, "empty");
        ValidationUtil.addValidation(birthPlace_member, validationLabelBirthPlace, "empty");
        ValidationUtil.addValidation(pesel_member, validationLabelPesel, "pesel");
        pesel_member.setTextLimit(11);
        ValidationUtil.addValidation(email_member, validationLabelEmail, "email");
        ValidationUtil.addValidation(phone_member, validationLabelPhone, "phone");
        ValidationUtil.addValidation(address_member, validationLabelAddress, "empty");
        ValidationUtil.addValidation(date_member, validationLabelDate, "empty");
    }
    public void MemberInserted(ActionEvent actionEvent) throws SQLException {
        try {
            Window owner = member_insertion.getScene().getWindow();

            if (!name_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę poprawić imię");
                return;
            }
            if (!lastName_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić nazwisko");
                return;
            }
            if (!gender_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić płeć");
                return;
            }
            if (!birthDate_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić datę urodzenia");
                return;
            }
            if (!birthPlace_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić miejsce urodzenia");
                return;
            }
            if (!email_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić email");
                return;
            }
            if (!phone_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić nr telefonu");
                return;
            }
            if (!address_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić addres");
                return;
            }
            if (!pesel_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić pesel");
                return;
            }
            if (!date_member.isValid()) {
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                        "Proszę wprowadzić datę wstąpienia");
                return;
            }
//            if (name_member.getText().isEmpty()) {
//                showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
//                        "Proszę wprowadzić imię");
//                return;
//            }


            //Edit check
            if (IsEditable) {
                handleEditMethod();
                return;
            }

            String name = name_member.getText();
            String lastName = lastName_member.getText();
            String gender = gender_member.getValue();
//            String birthDate = birthDate_member.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String birthDate = birthDate_member.getValue().toString();
            String birthPlace = birthPlace_member.getText();
            String email = email_member.getText();
            String phone = phone_member.getText();
            String address = address_member.getText();
            String pesel = pesel_member.getText();
//            String date = String.valueOf(date_member.getValue());
            String date = date_member.getValue().toString();
//            String date = date_member.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            int flag = connect.insert_Member_query_Executer(name, lastName, gender, birthDate, birthPlace, email, phone, address, pesel, date);

            if (flag == 1) {
                infoBox("Dodano użytkownika", null, "Ok!!");
            } else {
                infoBox("Błąd!!", null, "Nie udało się");

            }
        }catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public void UpdateInformation(Member_list.Members members){
        name_member.setText(members.getName());
        lastName_member.setText(members.getLastName());
        gender_member.setText(members.getGender());
        birthDate_member.setText(members.getBirthDate().toString());
        birthPlace_member.setText(members.getBirthPlace());
        email_member.setText(members.getEmail());
        phone_member.setText(members.getPhone());
        address_member.setText(members.getAddress());
        pesel_member.setText(members.getPesel());
        date_member.setText(members.getDate().toString());

        pesel_member.setEditable(false);
        pesel_member.setDisable(true);

        IsEditable = Boolean.TRUE;
    }
    //update query from edit

    private void handleEditMethod() {

        Member_list.Members member = new Member_list.Members(name_member.getText(),lastName_member.getText(),gender_member.getText(),birthDate_member.getText(),birthPlace_member.getText(),email_member.getText(),phone_member.getText(),address_member.getText(),pesel_member.getText(),date_member.getText());
        if(connect.updateMember(member)){
            AlertMaker.showAlert("Ok!!","Użytkownik zaktualizowany");
        }else{
            AlertMaker.showError("Błąd!","Edycja nie udana handleEditMethod");
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
    private static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        ButtonType buttonOk = new ButtonType("Ok");
        ButtonType buttonAnuluj = new ButtonType("Anuluj");

        alert.getButtonTypes().setAll(buttonOk, buttonAnuluj);
        alert.showAndWait();
    }
}
