package pharmacy.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFrame extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		// Load the FXML file
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashboardFrame.fxml"));

		Scene scene = new Scene(root, 400, 300);
		stage.setScene(scene);
		stage.setTitle("JavaFX with FXML");
		stage.show();
	}

}
