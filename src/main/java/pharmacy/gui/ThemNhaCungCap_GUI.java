package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhaCungCap;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.StringUtil;

public class ThemNhaCungCap_GUI {

	@FXML
	private TextField addressField;

	@FXML
	private Label idField;

	@FXML
	private Label idAlert;

	@FXML
	private Label addressAlert;

	@FXML
	private Button backBtn;

	@FXML
	private TextField emailField;

	@FXML
	private Label emailAlert;

	@FXML
	private TextField nameField;

	@FXML
	private Label nameAlert;

	@FXML
	private TextField phoneField;

	@FXML
	private Label phoneAlert;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	@FXML
	private Button clearBtn;

	@FXML
	private TableView<NhaCungCap> supplierTable;

	@FXML
	private TableColumn<NhaCungCap, String> idColumn;
	
	@FXML
	private TableColumn<NhaCungCap, String> nameColumn;
	
	@FXML
	private TableColumn<NhaCungCap, String> addressColumn;
	
	@FXML
	private TableColumn<NhaCungCap, String> phoneColumn;
	
	@FXML
	private TableColumn<NhaCungCap, String> emailColumn;

	private ObservableList<NhaCungCap> addedSupplierList = FXCollections.observableArrayList();

	// methods
	@FXML
	public void initialize() throws SQLException {
		handleBackBtnClick();
		setUpForm();
		clearForm();
	}

	@FXML
	public void setUpForm() throws SQLException {
		

		idField.setText(generateID());

		clearBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(clearBtn, 1, 0.6, 200, () -> {
			});
		});
		clearBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(clearBtn, 0.6, 1, 200, () -> {
			});
		});
		clearBtn.setOnMouseClicked(event -> {
			try {
				clearForm();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		// handle if table empty
		if (addedSupplierList.isEmpty()) {
			Label noNCCLabel = new Label("Không có nhà cung cấp nào trong bảng.");
			noNCCLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			supplierTable.setPlaceholder(noNCCLabel);
		}

		handleAddSupplier();
	}

	@FXML
	public void handleAddSupplier() {
		submitBtn.setOnMouseClicked(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.7, 200, () -> {
			});
		});
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.7, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.7, 1, 200, () -> {
			});
		});

		submitBtn.setOnAction(event -> {

			try {
				if (validateForm()) {

					String maNCC = idField.getText().trim();
					String tenNCC = nameField.getText().trim();
					String sdtNCC = phoneField.getText().trim();
					String diaChi = addressField.getText().trim();
					String emailNCC = emailField.getText().trim();
		
					NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, sdtNCC, diaChi, emailNCC);

					new NhaCungCap_BUS().createNhaCungCap(ncc);
					showAddNCCSuccessModal("Thêm nhà cung cấp thành công");
					clearForm();
					addedSupplierList.add(ncc);
					handleRenderAddedNCCTable();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		});
		

		
	}

	@FXML
	private void showAddNCCSuccessModal(String message) {
		Stage modalStage = new Stage();
		modalStage.setResizable(false);
		modalStage.initModality(Modality.APPLICATION_MODAL);

		ImageView icon = new ImageView(new Image(
				getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));
		icon.setFitWidth(40);
		icon.setFitHeight(40);

		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

		Button closeButton = new Button("Đóng");
		closeButton.setStyle(
				"-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
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
		layout.setStyle(
				"-fx-alignment: center; -fx-background-color: #ffffff; "
						+ "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

		Scene scene = new Scene(layout, 400, 150);
		modalStage.setScene(scene);

		modalStage.showAndWait();
	}

	@FXML
	public boolean validateForm() {
		boolean isValid = true;
		
		// Validate Name field
		if (nameField.getText().trim().isEmpty()) {
            nameAlert.setText("Tên nhà cung cấp không được rỗng.");
            nameAlert.setVisible(true);
            isValid = false;
        } else if (!nameField.getText().trim().matches("[a-zA-ZÀ-ỹ\\s]+")) {
            nameAlert.setText("Tên không được chứa số hoặc ký tự đặc biệt.");
            nameAlert.setVisible(true);
            isValid = false;
        } else {
            String name = StringUtil.capitalizeWords(nameField.getText().trim());
            nameField.setText(name);
            nameAlert.setVisible(false);
        }

		if (addressField.getText().trim().isEmpty()) {
			addressAlert.setText("Địa chỉ không được rỗng.");
			addressAlert.setVisible(true);
			isValid = false;
		} else {
			addressAlert.setVisible(false);
		}

		// Validate Phone field
		String phoneText = phoneField.getText().trim();
		if (phoneText.isEmpty()) {
			phoneAlert.setText("Số điện thoại không được để trống.");
			phoneAlert.setVisible(true);
			isValid = false;
		} else if (!phoneText.matches("^^0\\d{9}$")) { 
			phoneAlert.setText("Số điện thoại không hợp lệ.");
			phoneAlert.setVisible(true);
			isValid = false;
		} else {
			phoneAlert.setVisible(false);
		}

		// Validate Email field
        if (emailField.getText().isEmpty()) {
            emailAlert.setText("Email nhà cung cấp không được rỗng.");
            emailAlert.setVisible(true);
            isValid = false;
        } else if (!emailField.getText().matches("[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            emailAlert.setText("Không đúng định dạng email.");
            emailAlert.setVisible(true);
            isValid = false;
        } else {
            emailAlert.setVisible(false);
        }

		return isValid;
	}

	@FXML
	public void handleBackBtnClick() {
		backBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(backBtn, 1, 0.5, 200, () -> {
			});
		});

		backBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(backBtn, 0.5, 1, 200, () -> {
			});
		});

		backBtn.setOnMouseClicked(event -> {
			try {
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/NhaCungCap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public String generateID() throws SQLException{
		int nccNumber = new NhaCungCap_BUS().countNhaCungCap();
		String id = String.format("NCC%04d",nccNumber + 1);

		return id;
	}

	@FXML
	public void clearForm() throws SQLException {
		idField.setText(generateID());
		nameField.setText("");
		phoneField.setText("");
		addressField.setText("");
		emailField.setText("");
	}

	@FXML
	public void handleRenderAddedNCCTable(){
		supplierTable.setItems(addedSupplierList);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
	}	

}
