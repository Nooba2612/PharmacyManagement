package pharmacy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    private final Screen screen = Screen.getPrimary();
    private final Rectangle2D bounds = screen.getBounds();
    private final double screenWidth = bounds.getWidth();
    private final double screenHeight = bounds.getHeight();

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("/fxml/DangNhap_GUI.fxml"));


        Parent root = loader.load();

//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            root.prefWidth(newVal.doubleValue());
//        });
//
//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            root.prefHeight(newVal.doubleValue());
//        });

//        primaryStage.setWidth(screenWidth);
//        primaryStage.setHeight(screenHeight);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Medkit - Pharmacy Management System");
//        primaryStage.setMaximized(true);
        primaryStage.getIcons()
                .add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
