package pharmacy.gui;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.OTP_BUS;
import pharmacy.entity.OTP;
import pharmacy.utils.NodeUtil;

public class NhapMaXacThuc_GUI {

	@FXML
	private Button backBtn;

	@FXML
	private Label email;

	@FXML
	private Button nextBtn;

	@FXML
	private TextField number1;

	@FXML
	private TextField number2;

	@FXML
	private TextField number3;

	@FXML
	private TextField number4;

	@FXML
	private TextField number5;

	@FXML
	private TextField number6;

	@FXML
	private Label resendBtn;

	private String otp;
	
	private String emailInputed;

	@FXML
	public void initialize() {
		handleBackBtn();
		setupOtpField(null, number1, number2);
		setupOtpField(number1, number2, number3);
		setupOtpField(number2, number3, number4);
		setupOtpField(number3, number4, number5);
		setupOtpField(number4, number5, number6);
		setupOtpField(number5, number6, null);

		// If number 6 is focused and all numbers are filled, the confirmation process
		// starts
		number6.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.matches("\\d") && areAllFieldsFilled()) {
				validateOTP();
			} 
		});
		handleConfirmationOTP();
	}

	private void setupOtpField(TextField prevField, TextField currentField, TextField nextField) {
		currentField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				currentField.setText(oldValue);
			} else if (newValue.length() == 1 && nextField != null) {
				nextField.requestFocus();
			}
		});

		currentField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.BACK_SPACE && prevField != null) {
				prevField.requestFocus();
			} else if (event.getCode() == KeyCode.RIGHT && nextField != null && currentField.isFocused()) {
				nextField.requestFocus();
			} else if (event.getCode() == KeyCode.LEFT && prevField != null && currentField.isFocused()) {
				prevField.requestFocus();
			}
		});

//		currentField.setOnKeyPressed(event -> {
//		    if (event.isControlDown() && event.getCode() == KeyCode.V) {
//		        String clipboardContent = Clipboard.getSystemClipboard().getString();
//
//		        if (clipboardContent != null && clipboardContent.length() == 6) {
//		            PauseTransition delay = new PauseTransition(Duration.millis(300)); 
//		            
//		            delay.setOnFinished(e -> {
//		                number1.setText(String.valueOf(clipboardContent.charAt(0)));
//		                number2.setText(String.valueOf(clipboardContent.charAt(1)));
//		                number3.setText(String.valueOf(clipboardContent.charAt(2)));
//		                number4.setText(String.valueOf(clipboardContent.charAt(3)));
//		                number5.setText(String.valueOf(clipboardContent.charAt(4)));
//		                number6.setText(String.valueOf(clipboardContent.charAt(5)));
//		            });
//		            
//		            delay.play();
//		        } else {
//		            System.out.println("Clipboard content does not have 6 characters.");
//		        }
//		    }
//		});

	}
	
	private void showErrorModal(String message) {
		Stage modalStage = new Stage();
		modalStage.setResizable(false);
		modalStage.initModality(Modality.APPLICATION_MODAL);
		modalStage.getIcons()
				.addAll(new Image(getClass().getClassLoader().getResource("images/x-icon.png").toString()));

		ImageView icon = new ImageView(
				new Image(getClass().getClassLoader().getResource("images/x-icon.png").toExternalForm()));
		icon.setFitWidth(40);
		icon.setFitHeight(40);

		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-font-size: 15px; -fx-padding: 10px;");

		Button closeButton = new Button("Đóng");
		closeButton.setStyle("-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
				+ "-fx-border-radius: 10px; -fx-cursor: hand; -fx-padding: 8px 20px;");
		closeButton.setOnAction(e -> modalStage.close());

		closeButton.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(closeButton, 1, 0.6, 300, () -> {
			});
		});
		closeButton.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(closeButton, 0.6, 1, 300, () -> {
			});
		});

		HBox contentLayout = new HBox(10, messageLabel, icon);
		contentLayout.setStyle("-fx-alignment: center; -fx-padding: 20px;");

		VBox layout = new VBox(15, contentLayout, closeButton);
		layout.setStyle("-fx-alignment: center; -fx-background-color: #ffffff; "
				+ "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

		Scene scene = new Scene(layout, 400, 150);
		modalStage.setScene(scene);

		modalStage.showAndWait();
	}

	private boolean areAllFieldsFilled() {
		return !number1.getText().isEmpty() && !number2.getText().isEmpty() && !number3.getText().isEmpty()
				&& !number4.getText().isEmpty() && !number5.getText().isEmpty() && !number6.getText().isEmpty();
	}

	@FXML
	private void validateOTP() {
		otp = number1.getText() + number2.getText() + number3.getText() + number4.getText() + number5.getText()
				+ number6.getText();
		
		String username = new NhanVien_BUS().getEmployeeByEmail(emailInputed).getMaNhanVien();
		
		OTP storedOTP = new OTP_BUS().getOTPByUsername(username);

		if (otp.equals(storedOTP.getOtpCode())) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/QuenMatKhau_GUI.fxml"));
			BorderPane borderPane = new BorderPane();
			try {
				borderPane = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			QuenMatKhau_GUI quenMatKhauGUI = loader.getController();
			quenMatKhauGUI.setUsername(username);

			backBtn.getScene().getWindow().hide();

			Stage stage = new Stage();
			Scene scene = new Scene(borderPane);
			stage.setResizable(false);
			stage.setMaximized(true);
			stage.setScene(scene);
			stage.setTitle("Medkit - Pharmacy Management System");
			stage.getIcons()
					.add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
			stage.show();
			System.out.println("OTP is correct");
		} else {
			showErrorModal("OTP không đúng.");
		}
	}

	@FXML
	public void handleConfirmationOTP() {
		nextBtn.setOnMouseEntered(e -> {
			NodeUtil.applyFadeTransition(nextBtn, 1, 0.7, 300, () -> {
			});
		});

		nextBtn.setOnMouseExited(e -> {
			NodeUtil.applyFadeTransition(nextBtn, 0.7, 1, 300, () -> {
			});
		});

		nextBtn.setOnAction(e -> {
			validateOTP();
		});
	}

	@FXML
	public void handleBackBtn() {
		backBtn.setOnMouseEntered(e -> {
			NodeUtil.applyFadeTransition(backBtn, 1, 0.7, 300, () -> {
			});
		});

		backBtn.setOnMouseExited(e -> {
			NodeUtil.applyFadeTransition(backBtn, 0.7, 1, 300, () -> {
			});
		});

		backBtn.setOnAction(e -> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NhapEmail_GUI.fxml"));
			BorderPane borderPane = new BorderPane();
			try {
				borderPane = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			backBtn.getScene().getWindow().hide();

			Stage stage = new Stage();
			Scene scene = new Scene(borderPane);
			stage.setResizable(false);
			stage.setMaximized(true);
			stage.setScene(scene);
			stage.setTitle("Medkit - Pharmacy Management System");
			stage.getIcons()
					.add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
			stage.show();
		});
	}
	
	@FXML
	public void setEmail(String emailInputed) {
		System.out.println(emailInputed);
		this.emailInputed = emailInputed;
	}

}
