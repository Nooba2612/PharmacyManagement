package pharmacy.utils;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class FrameUtil {

	private FrameUtil() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static void switchScreen(Stage stage, String fxmlFilePath) throws IOException {
		Parent newRoot = FXMLLoader.load(FrameUtil.class.getResource(fxmlFilePath));

		Scene newScene = new Scene(newRoot);

		stage.setScene(newScene);

		stage.show();
	}
}
