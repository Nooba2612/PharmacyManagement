package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import pharmacy.bus.TaiKhoan_BUS;

public class DangNhap_GUI extends Application {
	@FXML
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
		root = FXMLLoader.load(getClass().getResource("/fxml/DangNhap_GUI.fxml"));
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

		handlePasswordInput();

		handleEnterKeyPress();

		handleSubmitBtnAction();

	}

	public void handlePasswordInput() {
		passwordField.setOnKeyTyped(event -> {
			passwordTextField.setText(passwordField.getText());
		});

		passwordTextField.setOnKeyTyped(event -> {
			passwordField.setText(passwordTextField.getText());
		});

		handleShowAndHidePassword();
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
					try {
						handleAuthenticate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
					try {
						handleAuthenticate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
					try {
						handleAuthenticate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
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
				try {
					handleAuthenticate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (passwordField.getText().length() == 0 || passwordTextField.getText().length() == 0) {
				showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền mật khẩu.");
			} else if (usernameField.getText().length() == 0) {
				showErrorAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.", "Bạn chưa điền tên người dùng.");
			}
		});
	}

	public void handleAuthenticate() throws SQLException {
		TaiKhoan_BUS taiKhoanBUS = new TaiKhoan_BUS();
		String password = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();
		boolean isAuthencated = taiKhoanBUS.authenticate(usernameField.getText(), password);
		handleProgressBarAction(isAuthencated);
		PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
		pause.setOnFinished(event -> {
			if (isAuthencated) {
				Stage stage = (Stage) usernameField.getScene().getWindow();
				stage.hide();

				MainLayout_GUI mainLayout = new MainLayout_GUI();
				try {
					mainLayout.start(stage);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				System.out.println("\n\nLogin Successfully.");
			} else {
				System.out.println("\n\nLogin Failed.");
			}
		});
		pause.play();
	}

	public void showErrorAlert(String title, String headerText, String alertContent) {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle(title);
		errorAlert.setHeaderText(headerText != null ? headerText : "Error");
		errorAlert.setContentText(alertContent);

		Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
		try {
			stage.getIcons().add(new Image("/images/alert-icon.png"));
		} catch (Exception e) {
			System.err.println("Icon not found: " + e.getMessage());
		}

		errorAlert.show();
	}

	public void handleProgressBarAction(boolean isAuthenticated) {
		progressBar.setVisible(true);
		progressBar.setProgress(0);
		progressBar.setStyle("-fx-accent: #339933;");
		progressStatus.setVisible(true);
		progressStatus.setText("Đang tải...");

		Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
				new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 1)));

		timeline.setOnFinished(event -> {
			if (isAuthenticated) {
				progressStatus.setText("Đăng nhập thành công.");
			} else {
				progressStatus.setText("Đăng nhập thất bại.");
				showErrorAlert("Lỗi", "Đăng nhập thất bại.", "Kiểm tra lại thông tin đăng nhập.");
			}

			Timeline hideStatusTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), eventHide -> {
				progressStatus.setVisible(false);
				progressBar.setVisible(false);
			}));
			hideStatusTimeline.play();
		});

		timeline.playFromStart();
	}

}