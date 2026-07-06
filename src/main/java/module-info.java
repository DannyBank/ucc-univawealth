module com.dbank.uccunivawealth {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires MaterialFX;
    requires bcrypt;

    opens com.dbank.uccunivawealth to javafx.fxml;
    opens com.dbank.uccunivawealth.controller to javafx.fxml;
    opens com.dbank.uccunivawealth.model to javafx.base;
    exports com.dbank.uccunivawealth;
    exports com.dbank.uccunivawealth.app to javafx.graphics;
}
