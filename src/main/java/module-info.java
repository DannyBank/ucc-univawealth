module com.dbank.uccunivawealth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires eu.hansolo.tilesfx;

    opens com.dbank.uccunivawealth to javafx.fxml;
    exports com.dbank.uccunivawealth;
}