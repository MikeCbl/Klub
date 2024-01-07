package Klub;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Pannel_librarian_Controller {

    public Button dashbored;
    @FXML
    private MFXButton Member_list;
    @FXML
    private MFXButton Gun_list;
    public Button newMember;
    public AnchorPane pannel_check;

    public AnchorPane rootpane;

    public void DASHBORED_METHOD(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/dashboard.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void NEW_MEMBER_METHOD(ActionEvent actionEvent) {
        try{
          AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Member_Insertion.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void MEMBER_LIST_METHOD(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Member_list.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GUN_LIST_METHOD(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Gun_collection.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void NEW_GUN_METHOD(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Gun_Insertion.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GUN_LENDING(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Gun_lending.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void LENDING_HISTORY(ActionEvent actionEvent) {
        try{
            AnchorPane pane = FXMLLoader.load(getClass().getResource("FXML/Gun_lending_history.fxml"));
            rootpane.getChildren().setAll(pane);
        }
        catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
