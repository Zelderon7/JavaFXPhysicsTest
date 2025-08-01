module com.example.gametest1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.gametest1 to javafx.fxml;
    opens com.example.gametest1.GameObjects to javafx.fxml;
    opens com.example.gametest1.Physics;

    exports com.example.gametest1;
    exports com.example.gametest1.GameObjects;
    exports com.example.gametest1.Physics;
}