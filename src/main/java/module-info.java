module com.example.audioplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.ufrn.audioplayer to javafx.fxml;
    exports com.ufrn.audioplayer;
    exports com.ufrn.audioplayer.controller;
    opens com.ufrn.audioplayer.controller to javafx.fxml;
}