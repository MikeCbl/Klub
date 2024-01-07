package Klub;

import Klub.DbConnection.DbConn;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class dashboard implements Initializable {
    @FXML
    private Text MemberCount;
    @FXML
    private Text GunsCount;
    public Image settingImage;
    @FXML
    private Text GunsCountAvail;
    @FXML
    private Text GunsCountRent;
    @FXML
    private LineChart<String, Integer> klubowicze_chart;
    @FXML
    private PieChart bron_chart;
    @FXML
    private MFXButton buttonMonth;
    @FXML
    private MFXButton buttonThisYear;
    @FXML
    private MFXButton buttonYear;
    @FXML
    private MFXTextField yearField;

    DbConn connect;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = DbConn.getInstance();

        GunCounter();
        MemberCounter();
        GunCounterAvail();
        GunCounterRent();
        //uruchom wykres przy starcie
        updateChart("year");

        buttonMonth.setOnAction(event ->updateChart("month"));
        buttonYear.setOnAction(event -> updateChart("year"));
        buttonThisYear.setOnAction(event -> updateChart("thisYear"));
        yearField.setTextLimit(4);
        pieChartUpdate();
    }

    public void updateChart(String grouping) {
        klubowicze_chart.getData().clear();
        klubowicze_chart.setCreateSymbols(false);

        String year = yearField.getText();

        ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
        String CHART_COUNT;
        if (grouping.equals("month")) {
            CHART_COUNT = "SELECT CAST(DATE_FORMAT(data_wstapienia, '%M', 'pl_PL') AS CHAR(3)) AS group_name, COUNT(*) AS count FROM klubowicze GROUP BY MONTH(data_wstapienia)";
        } else if (grouping.equals("year")) {
            CHART_COUNT = "SELECT YEAR(data_wstapienia) AS group_name, COUNT(*) AS count FROM klubowicze GROUP BY YEAR(data_wstapienia)";
        } else if (grouping.equals("thisYear")) {
            CHART_COUNT = "SELECT CAST(DATE_FORMAT(data_wstapienia, '%M', 'pl_PL') AS CHAR(3)) AS group_name, COUNT(*) AS count FROM klubowicze WHERE YEAR(data_wstapienia) = " + year + " GROUP BY MONTH(data_wstapienia)";
        } else {
            // Invalid grouping value, do nothing
            return;
        }
        ResultSet rs = connect.execQuery(CHART_COUNT);

        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                XYChart.Data<String, Integer> dataPoint = new XYChart.Data<>(rs.getString("group_name"), rs.getInt("count"));
                HoveredThresholdNode node = new HoveredThresholdNode(dataPoint.getYValue());
                System.out.println("Setting node: " + node);
                dataPoint.setNode(node);
                data.add(dataPoint);

                System.out.println("y = "+dataPoint.getYValue()+" x = "+dataPoint.getXValue());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName(grouping);
        series.getData().addAll(data);

        // Add the series to the chart
        klubowicze_chart.getData().add(series);
        klubowicze_chart.setAnimated(false);
    }



    public void pieChartUpdate() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String AV_GUN_COUNT = "SELECT COUNT(*) FROM bron WHERE dostepnosc = '1'";
        String RENTED_GUN_COUNT = "SELECT COUNT(*) FROM bron WHERE dostepnosc = '0'";

        // retrieve the count of available guns
        ResultSet resultSet = connect.execQuery(AV_GUN_COUNT);
        int availableGunsCount = 0;
        try {
            if (resultSet.next()) {
                availableGunsCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE, null, e);
        }

        // retrieve the count of rented guns
        resultSet = connect.execQuery(RENTED_GUN_COUNT);
        int rentedGunsCount = 0;
        try {
            if (resultSet.next()) {
                rentedGunsCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE, null, e);
        }

        // add the data to the pie chart
        pieChartData.add(new PieChart.Data("Dostępna broń", availableGunsCount));
        pieChartData.add(new PieChart.Data("Użyczona broń", rentedGunsCount));
        bron_chart.setData(pieChartData);
    }
    public void MemberCounter(){
        String MEMBER_COUNT = "select pesel from klubowicze";
        ResultSet resultSet = connect.execQuery(MEMBER_COUNT);
        int COUNT = 0;
        try {
            while (resultSet.next()) {
                String Count = resultSet.getString("pesel");
                COUNT++;
            }
            MemberCount.setText(String.valueOf(COUNT));
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GunCounter(){
        String GUNS_COUNT = "select nr_fabryczny from bron";
        ResultSet resultSet = connect.execQuery(GUNS_COUNT);
        int COUNT = 0;
        try {
            while (resultSet.next()) {
                String Count = resultSet.getString("nr_fabryczny");
                COUNT++;
            }
            GunsCount.setText(String.valueOf(COUNT));
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GunCounterAvail(){
        String GUNS_COUNT = "select nr_fabryczny from bron WHERE dostepnosc = 1";
        ResultSet resultSet = connect.execQuery(GUNS_COUNT);
        int COUNT = 0;
        try {
            while (resultSet.next()) {
                String Count = resultSet.getString("nr_fabryczny");
                COUNT++;
            }
            GunsCountAvail.setText(String.valueOf(COUNT));
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    public void GunCounterRent(){
        String GUNS_COUNT = "select nr_fabryczny from bron WHERE dostepnosc = 0";
        ResultSet resultSet = connect.execQuery(GUNS_COUNT);
        int COUNT = 0;
        try {
            while (resultSet.next()) {
                String Count = resultSet.getString("nr_fabryczny");
                COUNT++;
            }
            GunsCountRent.setText(String.valueOf(COUNT));
        }catch (SQLException e){
            Logger.getLogger(GunsCollection.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
