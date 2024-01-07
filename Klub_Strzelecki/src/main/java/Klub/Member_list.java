package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Member_list implements Initializable {
    LinkedList<Members> MemberList = new LinkedList<>();
    ObservableList<Members> list = FXCollections.observableArrayList();
    public AnchorPane rootPane;
    public javafx.scene.control.TableView<Members> TableView;

    public TableColumn<Members,String> colName;
    public TableColumn<Members,String> colLastName;
    public TableColumn<Members,String> colGender;
    public TableColumn<Members,String> colBirthDate;
    public TableColumn<Members,String> colBirthPlace;
    public TableColumn<Members,String> colEmail;
    public TableColumn<Members,String> colPhone;
    public TableColumn<Members,String> colAddress;
    public TableColumn<Members,String> colPesel;
    public TableColumn<Members, String> colDate;



//RadioButton do sortowania
    @FXML
    private MFXRadioButton sortName;
    @FXML
    private MFXRadioButton sortLastName;
    @FXML
    private MFXRadioButton sortGender;
    @FXML
    private MFXRadioButton sortBirthDate;
    @FXML
    private MFXRadioButton sortBirthPlace;
    @FXML
    private MFXRadioButton sortEmail;
    @FXML
    private MFXRadioButton sortPhone;
    @FXML
    private MFXRadioButton sortAddress;
    @FXML
    private MFXRadioButton sortPesel;
    @FXML
    private MFXRadioButton sortDate;
    @FXML
    private MFXRadioButton sortAll;
    @FXML
    private TextField searchEngineMember;
    @FXML
    private Pagination pagination;

    public AnchorPane rootpane;

    DbConn connect;
    int rowsPerPage = 11;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();
        ValueinSertion();
        loadData();
        AdvanceSearch();
// wyłacz scroll
        TableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaY() != 0 || event.getDeltaX() != 0) {
                event.consume();
            }
        });

        //paginacja na start
//        pagination.setPageCount((int) Math.ceil(list.size() / (double) rowsPerPage));
        updateTableViewOnStart(0, list);
    }

    private void updateTableViewOnStart(int pageIndex, ObservableList<Members> data) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        TableView.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
    }

//dodać value insertion columny z xml (adres pesel itd)
    private void ValueinSertion(){
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colBirthPlace.setCellValueFactory(new PropertyValueFactory<>("birthPlace"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("insertdate"));

//        TableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    public static class Members{
        private final SimpleStringProperty name;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty birthDate;
        private final SimpleStringProperty birthPlace;
        private final SimpleStringProperty email;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty address;
        private final SimpleStringProperty pesel;
        private final SimpleStringProperty insertdate;


         public Members(String name, String lastName, String gender, String birthDate, String birthPlace, String email, String phone, String address,String pesel, String insertion_date){
             this.name = new SimpleStringProperty(name);
             this.lastName = new SimpleStringProperty(lastName);
             this.gender = new SimpleStringProperty(gender);
             this.birthDate = new SimpleStringProperty(birthDate);
             this.birthPlace = new SimpleStringProperty(birthPlace);
             this.email = new SimpleStringProperty(email);
             this.phone = new SimpleStringProperty(phone);
             this.address = new SimpleStringProperty(address);
             this.pesel = new SimpleStringProperty(pesel);
             this.insertdate = new SimpleStringProperty(insertion_date);

         }

         public String getName(){
             return name.get();
         }
         public String getLastName(){
             return lastName.get();
         }
        public String getGender(){
            return gender.get();
        }
        public Date getBirthDate(){
            return Date.valueOf(birthDate.get());
        }
        public String getBirthPlace(){
            return birthPlace.get();
        }
        public String getEmail(){
            return email.get();
        }
        public String getPhone(){
            return phone.get();
        }
         public String getAddress(){
             return address.get();
         }
        public String getPesel(){
            return pesel.get();
        }
        public Date getDate(){return Date.valueOf(insertdate.get());}
        public SimpleStringProperty insertdateProperty() {
            return insertdate;
         }
    }


    //Data structure
    private void DataTaker(){
        DbConn connect = new DbConn();
        String SELECT_MEMBER_QUERY = "select * from klubowicze";//member_collection
        //  PreparedStatement preparedStatement = connect.connection.prepareStatement(SELECT_BOOK_QUERY);
        ResultSet resultSet = connect.execQuery(SELECT_MEMBER_QUERY);
        try {
            while (resultSet.next()) {
                String Name = resultSet.getString("imie");
                String LastName = resultSet.getString("nazwisko");
                String Gender = resultSet.getString("plec");
                String BirthDate = resultSet.getString("data_urodzenia");
                String BirthPlace = resultSet.getString("miejsce_urodzenia");
                String Email = resultSet.getString("email");
                String Phone = resultSet.getString("nr_telefonu");
                String Address = resultSet.getString("adres_zamieszkania");
                String Pesel = resultSet.getString("pesel");
                String Date= resultSet.getString("data_wstapienia");
                MemberList.add(new Members(Name,LastName,Gender,BirthDate,BirthPlace,Email,Phone,Address,Pesel,Date));

            }
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void loadData(){
        MemberList.clear();
        list.clear();
        DataTaker();
        int i = 0;
        while (MemberList.size() != i) {
            list.add(MemberList.get(i));
            i++;
        }
        TableView.setItems(list);
        //Advance Sorting of table items
        AdvanceSearch();

    }

    //Advance Search
    private void AdvanceSearch(){
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Members> filteredData = new FilteredList<>(list, b -> true);

        // Calculate the number of pages needed to display the data
        // Set the page count on the pagination object
        pagination.setPageCount((int) Math.ceil(list.size() / (double) rowsPerPage));
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            // Update the TableView with the appropriate data for the current page
            updateTableView(newValue.intValue(), filteredData);
        });


        // 2. Set the filter Predicate whenever the filter changes.
        searchEngineMember.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(search -> {

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();


//                int pageCount = (int) Math.ceil(list.size() / (double) rowsPerPage);
//                pagination.setPageCount(pageCount);


                if(sortName.isSelected()){
                    if(search.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortLastName.isSelected()) {
                    if (search.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                }else if(sortGender.isSelected()){
                    if(search.getGender().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortBirthDate.isSelected()){
                    if(search.getBirthDate().toString().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortBirthPlace.isSelected()){
                    if(search.getBirthPlace().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortEmail.isSelected()){
                    if(search.getEmail().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortPhone.isSelected()){
                    if(search.getPhone().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortPesel.isSelected()){
                    if(search.getPesel().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                }else if(sortAddress.isSelected()){
                    if (search.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    }
                }else if(sortDate.isSelected()){
                    if (search.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    }
                }else if(sortAll.isSelected()) {
                    if(search.getName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getGender().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getBirthDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if(search.getBirthPlace().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getEmail().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getPhone().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getAddress().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(search.getPesel().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if (search.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    }
                }
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                else
                    return false; // Does not match.
            });
            pagination.setPageCount((int) Math.ceil(filteredData.size() / (double) rowsPerPage));
            // Update the TableView with the appropriate data for the current page
            updateTableView(pagination.getCurrentPageIndex(), filteredData);

        });



        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Members> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(TableView.comparatorProperty());


        //  TableView.setItems(FXCollections.observableArrayList(sortedData));
        // 5. Add sorted (and filtered) data to the table.
        TableView.setItems(sortedData);

        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            // Update the TableView with the appropriate data for the current page
             updateTableView(newValue.intValue(), filteredData);
        });

    }


    private void updateTableView(int pageIndex, FilteredList<Members> filteredData) {
        int startIndex = pageIndex * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, filteredData.size());
        TableView.setItems(FXCollections.observableArrayList(filteredData.subList(startIndex, endIndex)));
    }


//    //Crud
    public void MemberEdit(ActionEvent actionEvent) {
        Members selectedForEdit = TableView.getSelectionModel().getSelectedItem();
        if(selectedForEdit == null){
            AlertMaker.showError("Błąd!","Proszę wybrać klubowicza");
            return;
        }
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/Member_Insertion.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Member controller = (Member) fxmlLoader.getController();
            controller.UpdateInformation(selectedForEdit);
            Stage stage = new Stage();
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest((e)->{
                RefreshMember(new ActionEvent());
            });
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void MemberDelete(ActionEvent actionEvent) {
        Members selectDeleteRow = TableView.getSelectionModel().getSelectedItem();
        if(selectDeleteRow == null){
            AlertMaker.showError("Błąd!","Proszę wybrać klubowicza do usunięcia");
            return;
        }
          boolean check = connect.IsMemberIsAlreadyIssued(selectDeleteRow);
        if(check){
            AlertMaker.showError("Błą!!","Nie można usunąć klubowicza "+selectDeleteRow.getName()+" \nKlubowicz pożyczył broń");
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuwanie klubowicza");
            alert.setContentText("Czy jesteś pewien że chcesz usunąc klubowicza " + selectDeleteRow.getName() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get().equals(ButtonType.OK)) {
                //Delete Member
                boolean result = connect.DeleteMembers(selectDeleteRow);
                if (result) {
                    AlertMaker.showAlert("Ok!!", "Użytkownik " + selectDeleteRow.getName() + " usunięty!!");
                    list.remove(selectDeleteRow);
                } else {
                    AlertMaker.showError("Błąd!!", "Użytkownik " + selectDeleteRow.getName() + " nie może być usunięty");
                }
            } else {
                AlertMaker.showAlert("Błąd przy usuwaniu!!", "Anulowano usuwanie");
            }
        }
    }

    public static String selectedPesel;

    public void handleRent(ActionEvent event) {
        Members selectedMember = TableView.getSelectionModel().getSelectedItem();
        if(selectedMember == null){
            AlertMaker.showError("Błąd!","Proszę wybrać klubowicza");
            return;
        }
        //pzypisujemy psel do statycznej zmiennej
        selectedPesel = selectedMember.getPesel();
        System.out.println("member list "+selectedPesel);
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Gun_collection.fxml"));
            rootpane.setPrefWidth(900);
            rootpane.getChildren().setAll(pane);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXML/Lending.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Użyczenia");
            stage.setScene(new Scene(root));
//            stage.setAlwaysOnTop(true);
            stage.show();


        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void RefreshMember(ActionEvent actionEvent) {
        loadData();
    }
}

