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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.HistoryLog_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SanPham;
import pharmacy.entity.SupplierHistoryLog;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.StringUtil;

public class CapNhatNCC_GUI {

    @FXML
    private TableColumn<SupplierHistoryLog, String> addressColumn;

    @FXML
    private TextField addressField;

    @FXML
    private Label addressAlert;

    @FXML
    private Button backBtn;

    @FXML
    private Label createDateField;

    @FXML
    private Label updatedAtAlert;

    @FXML
    private TableColumn<SupplierHistoryLog, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailAlert;

    @FXML
    private TableColumn<SupplierHistoryLog, String> employeeColumn;

    @FXML
    private TableView<SupplierHistoryLog> historyTable;

    @FXML
    private TableColumn<SupplierHistoryLog, String> idColumn;

    @FXML
    private Label idField;

    @FXML
    private Label idAlert;

    @FXML
    private TableColumn<SupplierHistoryLog, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameAlert;

    @FXML
    private TableColumn<SupplierHistoryLog, String> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private Label phoneAlert;

    @FXML
    private Button refreshDataBtn;

    @FXML
    private Button rejectBtn;

    @FXML
    private HBox root;

    @FXML
    private Button submitBtn;

    @FXML
    private TableColumn<SupplierHistoryLog, LocalDateTime> updatedAtColumn;

    private ObservableList<SupplierHistoryLog> historySupplierList = FXCollections.observableArrayList();

    private NhaCungCap currentSupplier;

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
    public void setUpForm(NhaCungCap supplier) throws SQLException {
        currentSupplier = supplier;
        historySupplierList.addAll(new HistoryLog_BUS().getSupplierHistoryById(supplier.getMaNCC()));

       
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createDateField.setText(LocalDateTime.now().format(formatter));

        refreshDataBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(refreshDataBtn, 1, 0.6, 200, () -> {
            });
        });
        refreshDataBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(refreshDataBtn, 0.6, 1, 200, () -> {
            });
        });
        refreshDataBtn.setOnMouseClicked(event -> {
            try {
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // handle if table empty
        if (historySupplierList.isEmpty()) {
            Label noNCCLabel = new Label("Không có lịch sử chỉnh sửa.");
            noNCCLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
            historyTable.setPlaceholder(noNCCLabel);
        }

        idField.setText(supplier.getMaNCC());
        nameField.setText(supplier.getTenNCC());
        addressField.setText(supplier.getDiaChi());
        phoneField.setText(supplier.getSoDienThoai());
        emailField.setText(supplier.getEmail());

        handleUpdateSupplier();
    }

    @FXML
    public void handleUpdateSupplier() throws SQLException {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
            });
        });
        submitBtn.setCursor(Cursor.HAND);

        // add product
        submitBtn.setOnAction(event -> {

            String maNCC = idField.getText().trim();
            String tenNCC = nameField.getText().trim();
            String diaChi = addressField.getText().trim();
            String sdt = phoneField.getText().trim();
            String email = emailField.getText().trim();

            NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, sdt, diaChi, email);
            
            LocalDateTime ngayCapNhat = LocalDateTime.now();
            try {
                if (validateForm()) {
                    new NhaCungCap_BUS().updateCustomer(ncc);
                    NhanVien nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
                    SupplierHistoryLog history = new SupplierHistoryLog(ncc, nhanVien, ngayCapNhat);
                    new HistoryLog_BUS().addSupplierHistory(history);
                    showUpdateSupplierSuccessModal("Cập nhật nhà cung cấp thành công");
                    submitBtn.getScene().getWindow().hide();
                    clearForm();
                    historySupplierList.add(history);
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
            addressAlert.setText("Địa chỉ nhà cung cấp không được rỗng.");
            addressAlert.setVisible(true);
            isValid = false;
        } else {
            nameAlert.setVisible(false);
        }

        if (phoneField.getText().trim().isEmpty()) {
            phoneAlert.setText("Số điện thoại nhân viên không được rỗng.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else if (!phoneField.getText().matches("^0\\d{9}$")) {
            phoneAlert.setText("Số điện thoại không đúng định dạng.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else {
            phoneAlert.setVisible(false);
        }

        if (emailField.getText().isEmpty()) {
            emailAlert.setText("Email nhân viên không được rỗng.");
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
    public void renderHistory() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));

        

        updatedAtColumn.setCellFactory(col -> new TableCell<SupplierHistoryLog, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        historyTable.setItems(historySupplierList);
    }

    @FXML
    private void showUpdateProductSuccessModal(String message) {
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
    private void showUpdateSupplierSuccessModal(String message) {
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
}
