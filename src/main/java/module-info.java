module com.sherif.gettingthedifferenceelement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sherif.gettingthedifferenceelement to javafx.fxml;
    exports com.sherif.gettingthedifferenceelement;
}