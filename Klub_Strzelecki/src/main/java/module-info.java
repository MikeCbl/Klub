module klub_strzelecki {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql;
    requires javafx.graphics;

    //Materialfx
    requires VirtualizedFX;
    requires MaterialFX;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    exports Klub;
    opens Klub to javafx.fxml;
}