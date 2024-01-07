package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

//excel
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class Gun_history implements Initializable {
    LinkedList<GunHistory> HistoryList = new LinkedList<>();
    ObservableList<GunHistory> list = FXCollections.observableArrayList();

    @FXML
    private TableView<GunHistory> TableViewHistory;

    @FXML
    private TableColumn<GunHistory, String> colBrand;

    @FXML
    private TableColumn<GunHistory, String> colDateIssiue;

    @FXML
    private TableColumn<GunHistory, String> colKind;

    @FXML
    private TableColumn<GunHistory, String> colLastName;

    @FXML
    private TableColumn<GunHistory, String> colName;

    @FXML
    private TableColumn<GunHistory, String> colNumber;

    @FXML
    private TableColumn<GunHistory, String> colPesel;

    @FXML
    private TableColumn<GunHistory, String> colReturnDate;

    //radio buttons
    @FXML
    private TextField searchEngine;

    @FXML
    private MFXRadioButton sortAll;

    @FXML
    private MFXRadioButton sortBrand;

    @FXML
    private MFXRadioButton sortIssueDate;

    @FXML
    private MFXRadioButton sortLastName;

    @FXML
    private MFXRadioButton sortName;

    @FXML
    private MFXRadioButton sortNumber;

    @FXML
    private MFXRadioButton sortPesel;

    @FXML
    private MFXRadioButton sortReturnDate;

    @FXML
    private MFXRadioButton sortKind;
    DbConn connect;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        ValueInsertion();
        loadData();
    }


    private void ValueInsertion(){
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        colKind.setCellValueFactory(new PropertyValueFactory<>("kind"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        colDateIssiue.setCellValueFactory(new PropertyValueFactory<>("issuedate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returndate"));
    }

    public static class GunHistory{
        private final SimpleStringProperty name;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty pesel;
        private final SimpleStringProperty kind;
        private final SimpleStringProperty brand;
        private final SimpleStringProperty number;
        private final SimpleStringProperty issuedate;
        private final SimpleStringProperty returndate;


        public GunHistory(String name, String lastName, String pesel, String kind,String brand, String number, String issuedate, String returndate){
            this.name = new SimpleStringProperty(name);
            this.lastName = new SimpleStringProperty(lastName);
            this.pesel = new SimpleStringProperty(pesel);

            this.kind = new SimpleStringProperty(kind);
            this.brand = new SimpleStringProperty(brand);
            this.number = new SimpleStringProperty(number);

            this.issuedate = new SimpleStringProperty(issuedate);
            this.returndate = new SimpleStringProperty(returndate);

        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getLastName() {
            return lastName.get();
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public String getPesel() {
            return pesel.get();
        }

        public SimpleStringProperty peselProperty() {
            return pesel;
        }

        public void setPesel(String pesel) {
            this.pesel.set(pesel);
        }

        public String getKind() {
            return kind.get();
        }

        public SimpleStringProperty kindProperty() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind.set(kind);
        }

        public String getBrand() {
            return brand.get();
        }

        public SimpleStringProperty brandProperty() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand.set(brand);
        }

        public String getNumber() {
            return number.get();
        }

        public SimpleStringProperty numberProperty() {
            return number;
        }

        public void setNumber(String number) {
            this.number.set(number);
        }

        public String getIssuedate() {
            return issuedate.get();
        }

        public SimpleStringProperty issuedateProperty() {
            return issuedate;
        }

        public void setIssuedate(String issuedate) {
            this.issuedate.set(issuedate);
        }

        public String getReturndate() {
            return returndate.get();
        }

        public SimpleStringProperty returndateProperty() {
            return returndate;
        }

        public void setReturndate(String returndate) {
            this.returndate.set(returndate);
        }
    }
    private void DataTaker(){
        DbConn connect = new DbConn();
        String SELECT_HISTORY_QUERY = "select * from uzyczenia_history";
        ResultSet resultSet = connect.execQuery(SELECT_HISTORY_QUERY);
        try {
            while (resultSet.next()) {
                String Name = resultSet.getString("imie");
                String LastName = resultSet.getString("nazwisko");
                String Pesel = resultSet.getString("pesel");

                String Kind = resultSet.getString("rodzaj");
                String Brand = resultSet.getString("marka");
                String Number = resultSet.getString("nr_fabryczny");
                String Insertdate = resultSet.getString("data_uzyczenia");
                String Returndate = resultSet.getString("data_oddania");
                HistoryList.add(new GunHistory(Name,LastName,Pesel,Kind,Brand,Number,Insertdate,Returndate));

            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void loadData(){
        HistoryList.clear();
        list.clear();
        DataTaker();
        int i = 0;
        while (HistoryList.size() != i) {
            list.add(HistoryList.get(i));
            i++;
        }
        TableViewHistory.setItems(list);
        AdvanceSearch();
    }

    private void AdvanceSearch(){
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<GunHistory> filteredData = new FilteredList<>(list, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchEngine.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(search -> {

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if(sortName.isSelected()){
                    if(search.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortLastName.isSelected()){
                    if(search.getLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortPesel.isSelected()){
                    if (search.getPesel().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortKind.isSelected()){
                    if (search.getKind().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortBrand.isSelected()){
                    if (search.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortNumber.isSelected()){
                    if (search.getNumber().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortIssueDate.isSelected()){
                    if(search.getIssuedate().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortReturnDate.isSelected()){
                    if(search.getReturndate().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortAll.isSelected()){
                    if(search.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getPesel().contains(lowerCaseFilter)) {
                        return true;
                    }else if (search.getKind().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getBrand().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getNumber().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getIssuedate().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getReturndate().contains(lowerCaseFilter)){
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
        SortedList<GunHistory> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(TableViewHistory.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        TableViewHistory.setItems(sortedData);


    }
    public void RefreshMember(ActionEvent actionEvent) {
        loadData();
    }
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    public void saveHistory(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if(selectedFile != null) {

            String filePath = selectedFile.getAbsolutePath();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("historia_uzyczen");

            DbConn connect = new DbConn();
            String SELECT_HISTORY_QUERY = "select * from uzyczenia_history";
            ResultSet resultSet = connect.execQuery(SELECT_HISTORY_QUERY);

            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int numberOfColumns = metaData.getColumnCount();
                //int i jest od 2 bo pomija kolumne id
                //Create the header row
                Row headerRow = sheet.createRow(0);
                sheet.autoSizeColumn(1);
                for (int i = 2; i <= numberOfColumns; i++) {
                    String columnName = metaData.getColumnName(i);
                    Cell cell = headerRow.createCell(i - 1);

                    cell.setCellValue(columnName);
                    CellStyle style = workbook.createCellStyle();
                    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(style);

                }

                //Insert data into the sheet
                int rowCount = 1;
                while (resultSet.next()) {
                    Row row = sheet.createRow(rowCount++);
                    for (int i = 2; i <= numberOfColumns; i++) {
                        String columnValue = resultSet.getString(i);
                        row.createCell(i - 1).setCellValue(columnValue);
                    }
                }

                //AutoFit Row Height
                //AutoFit Column Width
                for(int i = 0; i < sheet.getRow(0).getLastCellNum(); i++){
                    sheet.autoSizeColumn(i);
                }
                for (Row row : sheet) {
                    row.setHeight((short) -1);
                }
                try {
                    FileOutputStream fileOut = new FileOutputStream(filePath);
                    workbook.write(fileOut);
                    fileOut.close();
                } catch (FileNotFoundException e) {
                    Window owner = TableViewHistory.getScene().getWindow();
                    showAlert(Alert.AlertType.ERROR, owner, "Błąd!", "Nie można zapisać pliku, \nponieważ jest otworzony w innym programie");
                }


            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
