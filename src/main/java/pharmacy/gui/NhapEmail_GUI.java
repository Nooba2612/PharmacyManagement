package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.OTP_BUS;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.SendOTP;

public class NhapEmail_GUI {

	@FXML
	private Button backBtn;

	@FXML
	private Label emailAlert;

	@FXML
	private TextField emailField;

	@FXML
	private Button nextBtn;

	@FXML
	public void initialize() {
		handleEmailConfirmation();
		handleBackBtn();
	}

	@FXML
	public void handleEmailConfirmation() {

		nextBtn.setOnMouseEntered(e -> {
			NodeUtil.applyFadeTransition(nextBtn, 1, 0.7, 300, () -> {
			});
		});

		nextBtn.setOnMouseExited(e -> {
			NodeUtil.applyFadeTransition(nextBtn, 0.7, 1, 300, () -> {
			});
		});

		nextBtn.setOnAction(e -> {
			if (validateEmail()) {
				openNextWindow();
			} else {
				showErrorModal("Email chưa được đăng ký.");
			}
		});

		emailField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				if (validateEmail()) {
					openNextWindow();
				} else {
					showErrorModal("Email chưa được đăng ký.");
				}
			}
		});
	}

	private boolean validateEmail() {
		final String emailRegexString = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

		if (emailField.getText().length() == 0) {
			showErrorModal("Chưa nhập email.");
			return false;
		}

		if (!emailField.getText().matches(emailRegexString)) {
			showErrorModal("Email sai định dạng");
			return false;
		}

		// send OTP to email inputed
		String otp = SendOTP.generateOtp();
		System.out.println("OTP generated: " + otp);
		SendOTP.sendEmail(emailField.getText(), otp);

		NhanVien nhanVien = new NhanVien_BUS().getEmployeeByEmail(emailField.getText());
		
		if (nhanVien == null) {
			showErrorModal("Email chưa được đăng ký.");
			return false;
		}
		String username = nhanVien.getMaNhanVien();

		new OTP_BUS().addOTP(username, otp, LocalDateTime.now().plusMinutes(2));
		new OTP_BUS().deleteExpiredOTP();

		return true;
	}

	private void openNextWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NhapMaXacThuc_GUI.fxml"));
		BorderPane borderPane = null;

		try {
			borderPane = loader.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		NhapMaXacThuc_GUI nhapMaXacThucGUI = loader.getController();

		if (nhapMaXacThucGUI != null) {
			nhapMaXacThucGUI.setEmail(emailField.getText());
		} else {
			System.out.println("Controller is null");
		}

		Stage stage = new Stage();
		Scene scene = new Scene(borderPane);
		stage.setResizable(false);
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.setTitle("Medkit - Pharmacy Management System");
		stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
		stage.show();

		nextBtn.getScene().getWindow().hide();
	}

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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DangNhap_GUI.fxml"));
			BorderPane borderPane = new BorderPane();
			try {
				borderPane = loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			Stage stage = new Stage();
			Scene scene = new Scene(borderPane);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.setTitle("Medkit - Pharmacy Management System");
			stage.getIcons()
					.add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
			stage.show();
			backBtn.getScene().getWindow().hide();
		});

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

}
