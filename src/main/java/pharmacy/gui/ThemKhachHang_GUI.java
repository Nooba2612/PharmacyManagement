package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pharmacy.entity.KhachHang;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class ThemKhachHang_GUI {
	@FXML
	private HBox root;

	@FXML
	private TextField idField;
	
	@FXML
	private TextField nameField;

	@FXML
	private TextField phoneField;
	
	@FXML
	private DatePicker birthdayField;

	@FXML
	private ComboBox<String> genderSelect;

	@FXML
	private TextField noteField;
	
	@FXML
	private Text nameFieldAlert;

	@FXML
	private Text phoneFieldAlert;

	@FXML
	private Text genderFieldAlert;

	@FXML
	private Button submitBtn;

	@FXML
	private Button backBtn;
	
	@FXML
	private Button clearDataBtn;
	
	@FXML
	private TableView<KhachHang> customerTable;

	@FXML
	private Label nameAlert;
	
	@FXML
	private Label phoneAlert;
	
	@FXML
	private Label genderAlert;
	
	@FXML
	private Label birthDateAlert;
	
	@FXML
	private Label noteAlert;

	private ObservableList<KhachHang> addedCustomerList = FXCollections.observableArrayList();
	
	// methods
//	@FXML
//	public void initialize() throws SQLException {
//		handleBackBtnClick();
//		handleAddCustomer();
//		
//	}
	
	@FXML
	public void setUpForm() throws SQLException {
		genderSelect.getItems().addAll("Nam", "Nữ", "Khác");
		clearDataBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 1, 0.6, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 0.6, 1, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseClicked(event -> {
			clearForm();
		});

		// handle if table empty
		if (addedCustomerList.isEmpty()) {
			Label noMedicineLabel = new Label("Không có khách hàng nào trong bảng.");
			noMedicineLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			customerTable.setPlaceholder(noMedicineLabel);
		}

		//handleAddCustomer();
	}
	
//	@FXML
//	public void handleAddCustomer() throws SQLException {
//	    submitBtn.setOnMouseEntered(event -> {
//	        NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
//	        });
//	    });
//	    submitBtn.setOnMouseExited(event -> {
//	        NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
//	        });
//	    });
//	    submitBtn.setCursor(Cursor.HAND);
//
//	    // add customer
//	    submitBtn.setOnAction(event -> {
//
//	        String maKhachHang = idField.getText().trim();
//	        String hoTen = nameField.getText().trim();
//	        String soDienThoai = phoneField.getText().trim();
//	        LocalDate namSinh = birthdayField.getValue();
//	        String gioiTinh = genderSelect.getValue();
//	        String ghiChu = noteField.getText().trim();
//
//	        if (namSinh == null) {
//	            birthDateAlert.setText("Ngày sinh không được để trống.");
//	            birthDateAlert.setVisible(true);
//	        } else {
//	            birthDateAlert.setVisible(false);
//	        }
//
//	        KhachHang khachHang = new KhachHang(maKhachHang, hoTen, soDienThoai, diemTichLuy, namSinh, gioiTinh, ghiChu);
//	        try {
//	            if (validateForm()) {
//	                new KhachHang_BUS().createKhachHang(khachHang);
//	                showAddCustomerSuccessModal("Thêm khách hàng thành công");
//	                clearForm();
//	                addedCustomerList.add(khachHang);
//	                handleRenderAddedCustomersTable();
//	            }
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    });
//	}

	
	

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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void clearForm() {
		nameField.setText("");
		phoneField.setText("");
		nameField.setText("");
		genderSelect.setValue("");
		birthdayField.setValue(null);
		noteField.setText("");
	}
	
}