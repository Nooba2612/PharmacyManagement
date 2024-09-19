package pharmacy.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.*;

import java.io.IOException;
import java.sql.SQLException;

import pharmacy.controllers.AuthController;
import pharmacy.utils.FrameUtil;

public class LoginFrame extends Application {
	private Parent root;
	private Scene scene;
	private ImageView showPasswordIcon;
	private ImageView hidePasswordIcon;
	private TextField usernameField;
	private Text forgotPasswordBtnText;
	private PasswordField passwordField;
	private TextField passwordTextField;
	private Button submitBtn;
	private ProgressBar progressBar;
	private Text progressStatus;

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = FXMLLoader.load(getClass().getResource("/fxml/LoginFrame.fxml"));
		scene = new Scene(root);
		showPasswordIcon = (ImageView) root.lookup("#showPasswordBtn");
		hidePasswordIcon = (ImageView) root.lookup("#hidePasswordBtn");
		usernameField = (TextField) root.lookup("#usernameField");
		passwordField = (PasswordField) root.lookup("#passwordField");
		passwordTextField = (TextField) root.lookup("#passwordTextField");
		forgotPasswordBtnText = (Text) root.lookup("#forgotPasswordBtn");
		submitBtn = (Button) root.lookup("#submitBtn");
		progressBar = (ProgressBar) root.lookup("#progressBar");
		progressStatus = (Text) root.lookup("#progressStatus");

		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));
		primaryStage.setResizable(false);
		primaryStage.show();

		progressBar.setVisible(false);
		progressStatus.setVisible(false);

		handleShowAndHidePassword();

		handleEnterKeyPress();

		handleSubmitBtnAction();

	}

	public void showAndHidePasswordBtnClickAnimation(Node node) {
		TranslateTransition moveDownTransition = new TranslateTransition();
		moveDownTransition.setNode(node);
		moveDownTransition.setFromY(0);
		moveDownTransition.setToY(5);
		moveDownTransition.setDuration(Duration.millis(150));

		TranslateTransition moveUpTransition = new TranslateTransition();
		moveUpTransition.setNode(node);
		moveUpTransition.setFromY(5);
		moveUpTransition.setToY(0);
		moveUpTransition.setDuration(Duration.millis(150));

		SequentialTransition sequentialTransition = new SequentialTransition(moveDownTransition, moveUpTransition);

		sequentialTransition.setOnFinished(e -> {
			node.setVisible(false);
			if (node == hidePasswordIcon) {
				showPasswordIcon.setVisible(true);
			} else {
				hidePasswordIcon.setVisible(true);
			}
		});

		// Play the animations
		sequentialTransition.play();
	}

	public void handleShowAndHidePassword() {
		passwordTextField.setVisible(false);
		hidePasswordIcon.setVisible(false);

		showPasswordIcon.setOnMouseClicked(event -> {
			showAndHidePasswordBtnClickAnimation(showPasswordIcon);
			passwordTextField.setText(passwordField.getText());
			passwordField.setVisible(false);
			passwordTextField.setVisible(true);
		});

		hidePasswordIcon.setOnMouseClicked(event -> {
			showAndHidePasswordBtnClickAnimation(hidePasswordIcon);
			passwordField.setText(passwordTextField.getText());
			passwordField.setVisible(true);
			passwordTextField.setVisible(false);
		});
	}

	public void handleEnterKeyPress() {
		usernameField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (passwordField.getText().length() != 0 && usernameField.getText().length() != 0) {
					handleAuthenticate();
				} else if (passwordField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền mật khẩu.");
				} else if (usernameField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền tên người dùng.");
				}
			}
		});

		passwordField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (passwordField.getText().length() != 0 && usernameField.getText().length() != 0) {
					handleAuthenticate();
				} else if (passwordField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền mật khẩu.");
				} else if (usernameField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền tên người dùng.");
				}
			}
		});

		passwordTextField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				if (passwordField.getText().length() != 0 && usernameField.getText().length() != 0) {
					handleAuthenticate();
				} else if (passwordField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền mật khẩu.");
				} else if (usernameField.getText().length() == 0) {
					showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền tên người dùng.");
				}
			}
		});
	}

	public void handleSubmitBtnAction() {
		FadeTransition fadeIn = new FadeTransition(Duration.millis(300), submitBtn);
		fadeIn.setFromValue(1.0);
		fadeIn.setToValue(0.7);
		fadeIn.setCycleCount(1);

		FadeTransition fadeOut = new FadeTransition(Duration.millis(300), submitBtn);
		fadeOut.setFromValue(0.7);
		fadeOut.setToValue(1.0);
		fadeOut.setCycleCount(1);

		// button hover animation
		submitBtn.setOnMouseEntered(event -> {
			fadeIn.play();
		});

		submitBtn.setOnMouseExited(event -> {
			fadeOut.play();
		});

		submitBtn.setOnMouseClicked(event -> {
			if (passwordField.getText().length() != 0 && usernameField.getText().length() != 0) {
				handleAuthenticate();
			} else if (passwordField.getText().length() == 0) {
				showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền mật khẩu.");
			} else if (usernameField.getText().length() == 0) {
				showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền tên người dùng.");
			}
		});
	}

	public void handleAuthenticate() {
		try {
			handleProgressBarAction();
			AuthController authController = new AuthController();
			boolean isAuthencated = authController.authenticateUser(usernameField.getText(), passwordField.getText());
			PauseTransition pause = new PauseTransition(Duration.seconds(2));
			pause.setOnFinished(event -> {
				if (isAuthencated) {
					Stage stage = (Stage) usernameField.getScene().getWindow();
					stage.hide();
					try {
						FrameUtil.switchScreen(stage, "/fxml/DashboardFrame.fxml");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("\n\nLogin Successfully.");
				} else {
					System.out.println("\n\nLogin Failed.");
					showErrorAlert("Lỗi", "Đăng nhập thất bại.", "Kiểm tra lại thông tin đăng nhập.");
				}
			});
			pause.play();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void showErrorAlert(String title, String headerText, String alertContent) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle(title);
		errorAlert.setHeaderText(headerText);
		errorAlert.setContentText(alertContent);
		Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/images/alert-icon.png"));
		errorAlert.showAndWait();
	}

	public void handleProgressBarAction() {
		progressBar.setVisible(true);
		progressStatus.setVisible(true);

		progressBar.setStyle("-fx-accent: #339933;");

		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
				new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 1)));

		timeline.setOnFinished(event -> {
			progressStatus.setText("Đăng nhập thành công.");
			progressBar.setVisible(false);
		});

		timeline.play();
	}

}