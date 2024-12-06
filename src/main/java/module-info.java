module com.example.cw_ood {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires com.google.gson;
    requires json;
    requires java.desktop;


    opens com.example.cw_ood to javafx.fxml;
    exports com.example.cw_ood;
    exports News;
    opens News to javafx.fxml;
}