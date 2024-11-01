package pharmacy.gui;

import java.sql.SQLException;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class CapNhatLoaiSanPham_GUI {

    @FXML
    private Label categoryAlert;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private ListView<String> nameSuggestionBox;

    @FXML
    private Label productTypeAlert;

    @FXML
    private ComboBox<String> productTypeField;

    @FXML
    private Button rejectBtn;

    @FXML
    private HBox root;

    @FXML
    private Button submitBtn;

    @FXML
    private Label unitAlert;

    @FXML
    private ComboBox<String> unitField;

    private SanPham currentProduct;

    @FXML
    public void initialize() {
        handleSubmitBtnAction();
        handleRejectBtnAction();
    }

    @FXML
    public void setUpForm(SanPham product) {
        currentProduct = product;
        submitBtn.setDisable(true);
        productTypeField.getItems().addAll("Thuốc", "Thiết bị y tế");

        productTypeField.setValue(product.getLoaiSanPham());
        categoryField.setValue(product.getDanhMuc());
        unitField.setValue(product.getDonViTinh());

        if (productTypeField != null) {
            if (productTypeField.getValue().equals("Thuốc")) {
                categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
                        "An thần", "Siro", "Khác");
                unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống",
                        "Gói");
            } else if (productTypeField.getValue().equals("Thiết bị y tế")) {
                categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
                        "Khác");
                unitField.getItems().addAll("Cái", "Chiếc", "Hộp", "Bộ");
            }
        }

        productTypeField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            categoryField.getItems().clear();
            if (newValue != null) {
                categoryField.setValue(null);
                unitField.setValue(null);
                categoryField.getItems().clear();
                unitField.getItems().clear();
                if (newValue.equals("Thuốc")) {
                    categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
                            "An thần", "Siro", "Khác");
                    unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống",
                            "Gói");
                } else if (newValue.equals("Thiết bị y tế")) {
                    categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
                            "Khác");
                    unitField.getItems().addAll("Cái", "Chiếc", "Hộp", "Bộ");
                }

                if (newValue.equals(currentProduct.getLoaiSanPham())
                        && categoryField.getValue().equals(currentProduct.getDanhMuc())
                        && unitField.getValue().equals(currentProduct.getDonViTinh())) {
                    submitBtn.setDisable(true);
                } else {
                    submitBtn.setDisable(false);
                }
            }
        });
    }

    @FXML
    private void showAddProductSuccessModal(String message) {
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
    public void handleSubmitBtnAction() {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.7, 300, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.7, 1, 300, () -> {
            });
        });

        submitBtn.setOnAction(event -> {
            String newProductType = productTypeField.getValue();
            String newCategory = categoryField.getValue();
            String newUnit = unitField.getValue();
            SanPham newProduct = new SanPham(currentProduct.getMaSanPham(), currentProduct.getTenSanPham(),
                    newCategory, currentProduct.getNgaySX(), currentProduct.getNhaSX(),
                    currentProduct.getNgayTao(), currentProduct.getNgayCapNhat(), currentProduct.getSoLuongTon(),
                    currentProduct.getDonGiaBan(), currentProduct.getThue(), currentProduct.getHanSuDung(),
                    currentProduct.getMoTa(), newUnit, currentProduct.getTrangThai(),
                    newProductType);
            try {
                if (validate()) {

                    if (new SanPham_BUS().updateSanPham(newProduct)) {
                        System.out.println("Product updated!");
                        showAddProductSuccessModal("Cập nhật sản phẩm thành công.");
                        rejectBtn.getScene().getWindow().hide();
                    } else {
                        System.out.println("Product update failed!");
                    }
                }
            } catch (SQLException ex) {
            }
        });
    }

    @FXML
    public void handleRejectBtnAction() {
        rejectBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 1, 0.7, 300, () -> {
            });
        });
        rejectBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 0.7, 1, 300, () -> {
            });
        });

        rejectBtn.setOnAction(event -> {
            rejectBtn.getScene().getWindow().hide();
        });
    }

    @FXML
    public boolean validate() {
        boolean isValid = true;

        // Validate Unit field
        String[] VALID_UNITS = { "Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói", "Cái", "Chiếc", "Bộ" };
        String unitValue = (unitField.getValue() != null) ? unitField.getValue().trim()
                : unitField.getEditor().getText().trim();

        if (unitValue.isEmpty()) {
            unitAlert.setText("Đơn vị tính chưa được chọn.");
            unitAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_UNITS).contains(unitValue)) {
            unitAlert.setText("Đơn vị tính không hợp lệ.");
            unitAlert.setVisible(true);
            isValid = false;
        } else {
            unitAlert.setVisible(false);
        }

        // Validate Category field
        String[] VALID_CATEGORIES = { "giảm đau", "hạ sốt", "kháng sinh", "chống viêm",
                "vitamin", "an thần", "siro", "dụng cụ y tế", "sản phẩm bảo vệ cá nhân", "dung dịch vệ sinh", "khác" };
        if (categoryField.getValue() == null || categoryField.getEditor().getText().trim().isEmpty()) {
            categoryAlert.setText("Danh mục chưa được chọn.");
            categoryAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_CATEGORIES).contains(categoryField.getValue().toLowerCase())) {
            unitAlert.setText("Đơn vị tính không hợp lệ.");
            unitAlert.setVisible(true);
            isValid = false;
        } else {
            categoryAlert.setVisible(false);
        }

        // Validate Product type field
        String[] VALID_TYPES = { "Thuốc", "Thiết bị y tế" };
        String productTypeValue = (productTypeField.getValue() != null) ? productTypeField.getValue().trim()
                : productTypeField.getEditor().getText().trim();

        if (productTypeValue.isEmpty()) {
            productTypeAlert.setText("Loại sản phẩm chưa được chọn.");
            productTypeAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_TYPES).contains(productTypeValue)) {
            productTypeAlert.setText("Loại sản phẩm không hợp lệ.");
            productTypeAlert.setVisible(true);
            isValid = false;
        } else {
            productTypeAlert.setVisible(false);
        }

        return isValid;
    }

}
