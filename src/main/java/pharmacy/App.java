package pharmacy;

import javafx.application.Application;
import javafx.stage.Stage;
import pharmacy.gui.MainLayout_GUI;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pharmacy Management");
        primaryStage.show();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        launch(MainLayout_GUI.class, args);
    }
}
