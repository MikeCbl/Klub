package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.*;
import javafx.event.ActionEvent;


public class GunsCollection implements Initializable {

   //Datastructers
   LinkedList<Guns> GunList = new LinkedList<Guns>();
   ObservableList<Guns> list = FXCollections.observableArrayList();
    @FXML
    private AnchorPane rootpane;
    @FXML
    private javafx.scene.control.TableView<Guns> TableView;
    @FXML
    private TableColumn<Guns, String> colBrand;
    @FXML
    private TableColumn<Guns, String> colCaliber;
    @FXML
    private TableColumn<Guns, String> colDate;
    @FXML
    private TableColumn<Guns, String> colKind;
    @FXML
    private TableColumn<Guns, String> colNumber;
    @FXML
    private TableColumn<Guns, String> colProductionDate;
    @FXML
    private TableColumn<Guns, String> colType;
    @FXML
    private TableColumn<Guns, Boolean> colAvailiblity;
    @FXML
    private TextField searchEngine;


    //Radio button
    @FXML
    private RadioButton sortAll;
    @FXML
    private RadioButton sortAvail;
    @FXML
    private RadioButton sortBrand;
    @FXML
    private RadioButton sortCaliber;
    @FXML
    private RadioButton sortDate;
    @FXML
    private RadioButton sortKind;
    @FXML
    private RadioButton sortNumber;
    @FXML
    private RadioButton sortProductionDate;
    @FXML
    private RadioButton sortType;
    //Data base
    DbConn connect;
   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {
       connect = DbConn.getInstance();
       ValueInsertion();
       loadData();
   }

    private void ValueInsertion(){
       colKind.setCellValueFactory(new PropertyValueFactory<>("kind"));
       colType.setCellValueFactory(new PropertyValueFactory<>("type"));
       colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
       colCaliber.setCellValueFactory(new PropertyValueFactory<>("caliber"));
       colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
       colProductionDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));

       colDate.setCellValueFactory(new PropertyValueFactory<Guns,String>("insertdate"));
       colAvailiblity.setCellValueFactory(new PropertyValueFactory<>("availiblity"));
   }

    public static class Guns{
        private final SimpleStringProperty kind;
        private final SimpleStringProperty type;
        private final SimpleStringProperty brand;
        private final SimpleStringProperty caliber;
        private final SimpleStringProperty number;
        private final SimpleStringProperty productionDate;
        private final SimpleStringProperty insertdate;
        private final SimpleBooleanProperty availiblity;

        public Guns(String kind, String type, String brand, String caliber,  String number, String productionDate, String insertedDate, Boolean Availiblity ){
            this.kind = new SimpleStringProperty(kind);
            this.type = new SimpleStringProperty(type);
            this.brand = new SimpleStringProperty(brand);
            this.caliber = new SimpleStringProperty(caliber);
            this.number = new SimpleStringProperty(number);
            this.productionDate = new SimpleStringProperty(productionDate);

            this.insertdate = new SimpleStringProperty(insertedDate);
            this.availiblity = new SimpleBooleanProperty(Availiblity);
        }

        public String getKind() {
            return kind.get();
        }
        public String getType() {
            return type.get();
        }
        public String getBrand() {
            return brand.get();
        }
        public String getCaliber() {
            return caliber.get();
        }
        public String getNumber() {
            return number.get();
        }
        public String getProductionDate() {
            return productionDate.get();
        }
        public String getInsertdate() {
            return insertdate.get();
        }
        //nie moze byc prymitywnym typem bool (toString error)
        public String getAvailiblity() {
            return availiblity.get() ? "Tak" : "Nie";
        }
        //setter
        public void setKind(String kind) {
            this.kind.set(kind);
        }
        public void setType(String type) {
            this.type.set(type);
        }
        public void setBrand(String brand) {
            this.brand.set(brand);
        }
        public void setCaliber(String caliber) {
            this.caliber.set(caliber);
        }
        public void setNumber(String number) {
            this.number.set(number);
        }
        public void setProductionDate(String productionDate) {
            this.productionDate.set(productionDate);
        }
        public void setInsertdate(String insertdate) {
            this.insertdate.set(insertdate);
        }

        public void setAvailiblity(boolean availiblity) {
            this.availiblity.set(availiblity);
        }

        public SimpleStringProperty indateProperty() {
          return insertdate;
        }

    }

    private void DataTaker(){
        String SELECT_GUN_QUERY = "select * from bron";
        //  PreparqedStatement preparedStatement = connect.connection.prepareStatement(SELECT_BOOK_QUERY);
        ResultSet resultSet = connect.execQuery(SELECT_GUN_QUERY);
        try {
            while (resultSet.next()) {
                String kind = resultSet.getString("rodzaj");
                String type = resultSet.getString("typ");
                String brand = resultSet.getString("marka");
                String caliber = resultSet.getString("kaliber");
                String number = resultSet.getString("nr_fabryczny");
                String productionDate = resultSet.getString("rok_produkcji");

                String Date = resultSet.getString("data_dodania");
                Boolean Avail = resultSet.getBoolean("dostepnosc");
                GunList.add(new Guns(kind,type,brand,caliber,number,productionDate,Date,Avail));
            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void loadData(){
        GunList.clear();
        list.clear();
        DataTaker();
        int i = 0;
        while (GunList.size() != i) {
            list.add(GunList.get(i));
            i++;
        }
        TableView.setItems(list);
        //Advance Sorting of table items
        AdvanceSearch();
    }

    private void AdvanceSearch(){
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Guns> filteredData = new FilteredList<>(list, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchEngine.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(search -> {

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if(sortKind.isSelected()){
                    if(search.getKind().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortType.isSelected()){
                    if(search.getType().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortBrand.isSelected()){
                    if (search.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortCaliber.isSelected()){
                    if (search.getCaliber().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortNumber.isSelected()){
                    if (search.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortProductionDate.isSelected()){
                    if (search.getProductionDate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortDate.isSelected()){
                    if(search.getInsertdate().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortAvail.isSelected()){
                    if(search.getAvailiblity().toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortAll.isSelected()){
                    if(search.getKind().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getType().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (search.getCaliber().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getNumber().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getProductionDate().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getInsertdate().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getAvailiblity().toString().contains(lowerCaseFilter)){
                        return true;
                    }
                }
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Guns> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(TableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        TableView.setItems(sortedData);


    }
    //CRUD
    public void GunEdit(ActionEvent actionEvent) {
        Guns selectedForEdit = TableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            AlertMaker.showError("Błąd!","Proszę wybrać broń");
            return;
        }
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/Gun_Insertion.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Klub.Guns controller = (Klub.Guns) fxmlLoader.getController();
//            Guns controller = (Guns) fxmlLoader.getController();
            controller.UpdateInformation(selectedForEdit);
            Stage stage = new Stage();
            stage.setTitle("Edit Gun");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest((e)->{
                Refresh(new ActionEvent());
            });
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GunDelete(ActionEvent actionEvent) {
        Guns selectDeleteRow = TableView.getSelectionModel().getSelectedItem();
        if(selectDeleteRow == null){
            AlertMaker.showError("Błąd!","Nie można usunąć, Proszę wybrać broń");
            return;
        }
        boolean check = connect.IsGunAlreadyIssued(selectDeleteRow);
        if(check){
            AlertMaker.showError("Błąd!!","Nie można usunąc "+selectDeleteRow.getBrand()+" \nBroń jest już użyczona");
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuwanie Broni klubowej");
            alert.setContentText("Czy jesteś pewien że chcesz usunąć  " + selectDeleteRow.getBrand() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get().equals(ButtonType.OK)) {
                //Delete Gun
                boolean result = connect.DeleteGuns(selectDeleteRow);
                if (result) {
                    AlertMaker.showAlert("Ok!", "Broń" + selectDeleteRow.getBrand() + " Usunięta!!");
                    list.remove(selectDeleteRow);
                } else {
                    AlertMaker.showError("Błąd!!", "Nie można usunąc " + selectDeleteRow.getBrand() + " Broni");
                }
            } else {
                AlertMaker.showAlert("Błąd przy usuwaniu!!", "Broni nie można usunąć");
            }
        }
    }

    public void CopyNumber(ActionEvent actionEvent){
       Guns selectNumberRow = TableView.getSelectionModel().getSelectedItem();
       String number = selectNumberRow.getNumber();

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(number);
        clipboard.setContent(content);

    }

    public void Refresh(ActionEvent actionEvent) {
         loadData();
    }
}