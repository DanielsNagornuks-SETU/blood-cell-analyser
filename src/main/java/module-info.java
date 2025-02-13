module daniels_nagornuks {
    requires javafx.controls;
    requires javafx.fxml;

    opens daniels_nagornuks to javafx.fxml;
    exports daniels_nagornuks;
}
