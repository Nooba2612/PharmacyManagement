package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
import pharmacy.bus.HistoryLogCustomer_BUS;
import pharmacy.bus.HistoryLog_BUS;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.CustomerHistoryLog;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhanVien;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class CapNhatKhachHang_GUI {

    @FXML
    private Button backBtn;

    @FXML
    private Label birthDateAlert;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private Button clearDataBtn;

    @FXML
    private TableView<CustomerHistoryLog> customerHistory;

    @FXML
    private Label genderAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, String> genderColumn;

    @FXML
    private Text genderFieldAlert;

    @FXML
    private ComboBox<String> genderSelect;

    @FXML
    private Label idAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, String> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private Label nameAlert;

    @FXML
    private TextField pointField;

    @FXML
    private Label pointAlert;
    
    @FXML
    private Label createAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField createField;

    @FXML
    private Text nameFieldAlert;

    @FXML
    private Label noteAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, String> noteColumn;

    @FXML
    private TableColumn<CustomerHistoryLog, String> employeeColumn;

    @FXML
    private TableColumn<CustomerHistoryLog, LocalDate> updatedAtColumn;

    @FXML
    private TextField noteField;

    @FXML
    private Label phoneAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, String> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private Text phoneFieldAlert;

    @FXML
    private TableColumn<CustomerHistoryLog, Number> pointsColumn;

    @FXML
    private HBox root;

    @FXML
    private Button submitBtn;

    @FXML
    private Button rejectBtn;

    @FXML
    private TableColumn<CustomerHistoryLog, LocalDate> yearColumn;
    
    private ObservableList<CustomerHistoryLog> historyCustomerList = FXCollections.observableArrayList();

    private KhachHang currentCustomer;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() throws SQLException {
        handleReject();
        renderHistory();
    }
    
    @FXML
    public void handleReject() {
        rejectBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 1, 0.7, 300, () -> {
            });
        });

        rejectBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 0.7, 1, 300, () -> {
            });
        });

        rejectBtn.setOnAction(event -> {
            Stage stage = (Stage) rejectBtn.getScene().getWindow();
            stage.close();
        });
    }
    
    @FXML
    public void setUpForm(KhachHang customer) throws SQLException {
        currentCustomer = customer;

        genderSelect.getItems().addAll("Nam", "Nữ", "Khác");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createField.setText(LocalDateTime.now().format(formatter));

        TextFormatter<String> phoneFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        phoneField.setTextFormatter(phoneFormatter);

        clearDataBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(clearDataBtn, 1, 0.6, 200, () -> {
            });
        });
        clearDataBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(clearDataBtn, 0.6, 1, 200, () -> {
            });
        });
        clearDataBtn.setOnMouseClicked(event -> {
            try {
                refreshForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if (historyCustomerList.isEmpty()) {
            Label noCustomerLabel = new Label("Không có lịch sử khách hàng.");
            noCustomerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
            customerHistory.setPlaceholder(noCustomerLabel);
        }

        
        idField.setText(customer.getMaKhachHang());
        nameField.setText(customer.getHoTen());
        genderSelect.setValue(customer.getGioiTinh());
        phoneField.setText(customer.getSoDienThoai());
        genderSelect.setValue(customer.getGioiTinh());
        birthdayField.setValue(customer.getNamSinh());
        noteAlert.setText(customer.getGhiChu());
        pointField.setText(String.valueOf(customer.getDiemTichLuy()));

        handleUpdateCustomer();
    }

    
    @FXML
    public void renderHistory() throws SQLException {
      
    	historyCustomerList = FXCollections.observableArrayList(new HistoryLogCustomer_BUS().getAllCusHistory());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        
        
        yearColumn.setCellFactory(col -> new TableCell<CustomerHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(String.valueOf(item.getYear()));
                }
            }
        });

        updatedAtColumn.setCellFactory(col -> new TableCell<CustomerHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        customerHistory.setItems(historyCustomerList);
    }

    
    @FXML
    public void renderCustomerHistory() throws SQLException {
    	historyCustomerList = FXCollections.observableArrayList(new HistoryLogCustomer_BUS().getAllCusHistory());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));

        yearColumn.setCellFactory(col -> new TableCell<CustomerHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        updatedAtColumn.setCellFactory(col -> new TableCell<CustomerHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        customerHistory.setItems(historyCustomerList);
    }

    
    @FXML
    public void handleUpdateCustomer() throws SQLException {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
            });
        });
        submitBtn.setCursor(Cursor.HAND);

        // add customer
        submitBtn.setOnAction(event -> {

            String maKH = idField.getText().trim();
            String hoTen = nameField.getText().trim();
            String sdt = phoneField.getText().trim();
            String gioiTinh = genderSelect.getValue().trim();
            int diemTichLuy = Integer.parseInt(!pointField.getText().trim().isEmpty() ? pointField.getText().trim() : "0");
            
            LocalDate namSinh = birthdayField.getValue();
            String ghiChu = noteField.getText().trim();

            if (namSinh == null) {
                birthDateAlert.setText("Ngày sinh không được rỗng.");
                birthDateAlert.setVisible(true);
            } else {
                birthDateAlert.setVisible(false);
            }

            KhachHang khachHang = new KhachHang(maKH, hoTen, sdt, diemTichLuy, namSinh, ghiChu, gioiTinh);
            
            try {
                if (validateForm()) {
                    new KhachHang_BUS().updateKhachHang(khachHang);
     
                    NhanVien nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
                    CustomerHistoryLog history = new CustomerHistoryLog(khachHang, nhanVien);
                    new HistoryLogCustomer_BUS().addCustomerHistory(history);      
                    showAddCustomerSuccessModal("Cập nhật khách hàng thành công");
                    submitBtn.getScene().getWindow().hide();
                    refreshForm();
                    historyCustomerList.add(history);
                    renderHistory();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    
    @FXML
	public boolean validateForm() {
		boolean isValid = true;

		// Validate ID field
        if (idField.getText().trim().isEmpty()) {
            idAlert.setText("Mã khách hàng không được rỗng.");
            idAlert.setVisible(true);
            isValid = false;
        } else {
            idAlert.setVisible(false);
        }
		
		// Validate Name field
		if (nameField.getText().trim().isEmpty()) {
		    nameAlert.setText("Họ và tên không được rỗng.");
		    nameAlert.setVisible(true);
		    isValid = false;
		} else if (!nameField.getText().matches("^([A-Z][a-z]*\\s)+$")) {
		    nameAlert.setText("Họ và tên không hợp lệ. Vui lòng nhập đúng định dạng.");
		    nameAlert.setVisible(true);
		    isValid = false;
		} else {
		    nameAlert.setVisible(false);
		}

		// Validate Phone field
		String phoneText = phoneField.getText().trim();
		if (phoneText.isEmpty()) {
			phoneAlert.setText("Số điện thoại không được để trống.");
			phoneAlert.setVisible(true);
			isValid = false;
		} else if (!phoneText.matches("^09\\d{8}$")) { 
			phoneAlert.setText("Số điện thoại không hợp lệ.");
			phoneAlert.setVisible(true);
			isValid = false;
		} else {
			phoneAlert.setVisible(false);
		}

		// Validate Customer type field
        String[] VALID_TYPES = { "Nam", "Nữ", "Khác" };
        String customerTypeValue = (genderSelect.getValue() != null) ? genderSelect.getValue().trim()
                : genderSelect.getEditor().getText().trim();

        if (customerTypeValue.isEmpty()) {
        	genderAlert.setText("Giới tính chưa được chọn.");
        	genderAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_TYPES).contains(customerTypeValue)) {
        	genderAlert.setText("Giới tính không hợp lệ.");
        	genderAlert.setVisible(true);
            isValid = false;
        } else {
        	genderAlert.setVisible(false);
        }
		
		// Validate Birthday field
		if (birthdayField.getValue() == null) {
			birthDateAlert.setText("Ngày sinh không được rỗng.");
			birthDateAlert.setVisible(true);
			isValid = false;
		} else {
			birthDateAlert.setVisible(false);
		}

		// Optional Note field validation (if needed)
		if (noteField.getText().trim().isEmpty()) {
			noteAlert.setText("Ghi chú không được rỗng.");
			noteAlert.setVisible(true);
			isValid = false;
		} else {
			noteAlert.setVisible(false);
		}

		return isValid;
	}
    
    @FXML
    private void showAddCustomerSuccessModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.getIcons().addAll(new Image(
                getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));

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
    public void refreshForm() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createField.setText(LocalDateTime.now().format(formatter));
        idField.setText(currentCustomer.getMaKhachHang());
        nameField.setText(currentCustomer.getHoTen());
        phoneField.setText(currentCustomer.getSoDienThoai());
        genderSelect.setValue(currentCustomer.getGioiTinh());
        birthdayField.setValue(currentCustomer.getNamSinh());
        noteField.setText(currentCustomer.getGhiChu());
    }
}
