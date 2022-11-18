module com.serdarcubuk.spellingbee {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.serdarcubuk.spellingbee to javafx.fxml;
    exports com.serdarcubuk.spellingbee;
}