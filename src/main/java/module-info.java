module daniels_nagornuks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens daniels_nagornuks to javafx.fxml;
    exports daniels_nagornuks;
}
