package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Guns_Returning implements Initializable {
    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableColumn<String, String> colName;
    @FXML
    private TableColumn<String, String> colLastName;
    @FXML
    private TableColumn<String, String> colPesel;
    @FXML
    private TableColumn<String, String> colBrand;
    @FXML
    private TableColumn<String, String> colNumber;
    @FXML
    private TableColumn<String, String> colDate;

    @FXML
    private TableView<Guns> TableViewGuns;

    @FXML
    private TableView<Members> TableViewMembers;
    @FXML
    private MFXButton GunSubmission;

    @FXML
    private MFXTextField SearchIssuedGuns;


    LinkedList<Members> LendingMemberList = new LinkedList<>();
    ObservableList<Members> MembersList = FXCollections.observableArrayList();

    LinkedList<Guns> LendingGunsList = new LinkedList<>();
    ObservableList<Guns> GunsList = FXCollections.observableArrayList();
    @FXML
    private MFXTextField GunIdTaker;
    @FXML
    private MFXTextField MemberIdTaker;


    DbConn connect;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        ValueInsertion();
        loadData();

        TableViewMembers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1) {
                        if (TableViewMembers.getItems().isEmpty()) {
                            // Table view is empty, do nothing
                            return;
                        }

                        TableView.TableViewSelectionModel<Members> selectionModel = TableViewMembers.getSelectionModel();
                        ObservableList selectedCells = selectionModel.getSelectedCells();
                        TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                        int row = tablePosition.getRow();
                        if (selectedCells.isEmpty() || selectedCells.size() == 0) {
                            // No cells are selected, do nothing
                            return;
                        }

                        // Do something with the name and last name
                        String name = (String) TableViewMembers.getColumns().get(0).getCellData(row);
                        String lastName = (String) TableViewMembers.getColumns().get(1).getCellData(row);
                        String Pesel = (String) TableViewMembers.getColumns().get(2).getCellData(row);
                        System.out.println("Name: " + name + ", Last Name: " + lastName + ", Pesel: " + Pesel);
                        String SELECT_LENDING_GUNS = "SELECT bron.marka, bron.nr_fabryczny, uzyczenia.data_uzyczenia FROM uzyczenia INNER JOIN klubowicze ON uzyczenia.klubowiczId = klubowicze.pesel INNER JOIN bron ON uzyczenia.bronId = bron.nr_fabryczny WHERE pesel ="+Pesel+"";
                        loadGunData(SELECT_LENDING_GUNS);

                    }
                }
            }
        });
        // set text
        TableViewGuns.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1) {
                        if (TableViewGuns.getItems().isEmpty()) {
                            // Table view is empty, do nothing
                            return;
                        }

                        TableView.TableViewSelectionModel<Guns> selectedGun = TableViewGuns.getSelectionModel();
                        ObservableList selectedGunCells = selectedGun.getSelectedCells();
                        TablePosition tableGunPosition = (TablePosition) selectedGunCells.get(0);

                        if (selectedGunCells.isEmpty() || selectedGunCells.size() == 0) {
                            // List of selected cells is empty, do nothing
                            return;
                        }
                        int rowGun = tableGunPosition.getRow();
                        String nrFab = (String) TableViewGuns.getColumns().get(1).getCellData(rowGun);
                        SearchIssuedGuns.setText(nrFab);

                    }
                }
            }
        });


    }


    private void ValueInsertion(){
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }


    public static class Members{
        private final SimpleStringProperty name;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty pesel;


        public Members(String name, String lastName, String pesel){
            this.name = new SimpleStringProperty(name);
            this.lastName = new SimpleStringProperty(lastName);
            this.pesel = new SimpleStringProperty(pesel);
        }

        public String getName(){
            return name.get();
        }
        public String getLastName(){
            return lastName.get();
        }
        public String getPesel(){
            return pesel.get();
        }
    }

    public static class Guns {
        private final SimpleStringProperty brand;
        private final SimpleStringProperty number;
        private final SimpleStringProperty date;

        public Guns(String brand, String number, String date) {
            this.brand = new SimpleStringProperty(brand);
            this.number = new SimpleStringProperty(number);
            this.date = new SimpleStringProperty(date);
        }

        public String getBrand() {
            return brand.get();
        }
        public String getNumber() {
            return number.get();
        }
        public String getDate() {
            return date.get();
        }
    }

    //Data for guns lended to specific person
    private void DataTakerGuns(String SELECT_LENDING_GUN){
        DbConn connect = new DbConn();
        ResultSet resultSet = connect.execQuery(SELECT_LENDING_GUN);
        try {
            while (resultSet.next()) {

                String Brand = resultSet.getString("marka");
                String Number = resultSet.getString("nr_fabryczny");
                String Date = resultSet.getString("data_uzyczenia");

                LendingGunsList.add(new Guns(Brand, Number, Date));

            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    //Data structure
    private void DataTaker(){
        DbConn connect = new DbConn();
        String SELECT_LENDING = "SELECT DISTINCT klubowicze.imie, klubowicze.nazwisko, klubowicze.pesel FROM uzyczenia INNER JOIN klubowicze ON uzyczenia.klubowiczId = klubowicze.pesel INNER JOIN bron ON uzyczenia.bronId = bron.nr_fabryczny";
        ResultSet resultSet = connect.execQuery(SELECT_LENDING);
        try {
            while (resultSet.next()) {
                String Name = resultSet.getString("imie");
                String LastName = resultSet.getString("nazwisko");
                String Pesel = resultSet.getString("pesel");

                LendingMemberList.add(new Members(Name, LastName, Pesel));

            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void loadData(){
        LendingMemberList.clear();
        MembersList.clear();
        DataTaker();
        int i = 0;
        while (LendingMemberList.size() != i) {
            MembersList.add(LendingMemberList.get(i));
            i++;
        }
        TableViewMembers.setItems(MembersList);

    }

    private void loadGunData(String SELECT_LENDING_GUNS){
        LendingGunsList.clear();
        GunsList.clear();
        DataTakerGuns(SELECT_LENDING_GUNS);
        int i = 0;
        while (LendingGunsList.size() != i) {
            GunsList.add(LendingGunsList.get(i));
            i++;
        }
        TableViewGuns.setItems(GunsList);
    }


    //zwracanie/oddawanie broni
    //Submit gun
    public void Submission_Of_Issued_Guns(ActionEvent actionEvent) {
        if(SearchIssuedGuns.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Błąd");
            alert.setTitle(null);
            alert.setHeaderText("Proszę wybrać broń do oddania");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Potwierdz oddanie");
        alert.setTitle(null);
        alert.setHeaderText("Czy jesteś pewien że chcesz zwrócić tą broń?");

        Optional<ButtonType> response = alert.showAndWait();
        if(response.get().equals(ButtonType.OK)){
            String id = SearchIssuedGuns.getText();
            String query = "DELETE FROM uzyczenia WHERE bronId = '"+id+"'";
            String query2 = "UPDATE bron SET dostepnosc = true WHERE nr_fabryczny = '"+id+"'";
            if(connect.execAction(query) && connect.execAction(query2)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setContentText("OK!!");
                alert1.setTitle(null);
                alert1.setHeaderText("Oddano broń");
                alert1.showAndWait();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Błąd!!");
                alert1.setTitle(null);
                alert1.setHeaderText("Nie powiodło się!");
                alert1.showAndWait();
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setContentText("Anulowano!!!");
            alert1.setTitle(null);
            alert1.setHeaderText("Zwracanie broni Anulowane!!!");
            alert1.showAndWait();
        }

    }

//uzycz bron
public void IssueGun(ActionEvent actionEvent) {
    //taking id from fields
    String GunId = GunIdTaker.getText();
    String MemberId = MemberIdTaker.getText();
    //Alert
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setContentText("Potwierdz użyczenie broni");
    alert.setTitle("POTWIERDZANIE");
    alert.setHeaderText("Czy jesteś pewien że chcesz użyczyć broń ?");
    Optional<ButtonType> response = alert.showAndWait();
    if(response.get().equals(ButtonType.OK)){
        String query = "INSERT INTO uzyczenia (bronId,klubowiczId) VALUES ('"+GunId+"','"+MemberId+"')";
        String query2 = "UPDATE bron SET dostepnosc = false WHERE nr_fabryczny = '"+GunId+"'";
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
    public void RefreshMember(ActionEvent actionEvent) {
        loadData();
    }
}
