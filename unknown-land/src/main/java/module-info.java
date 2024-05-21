module cz.cvut.fel.pjv {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;

    opens cz.cvut.fel.pjv to javafx.fxml;
    exports cz.cvut.fel.pjv;
}
