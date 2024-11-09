package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.KhachHang;
import pharmacy.utils.NodeUtil;

public class ThanhToanTienMat_GUI {

    @FXML
    private TextField change;

    @FXML
    private TextField givenMoney;

    @FXML
    private Button rejectBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField amountPaidField;

    @FXML
    private Pane rootPane;

    @FXML
    private Label givenMoneyAlert;

    private boolean isSuccess = false;

    @FXML
    public void initialize() {
        rootPane.getChildren().forEach(child -> {
            if (child instanceof Button) {
                child.setOnMouseEntered(event -> {
                    NodeUtil.applyFadeTransition(child, 1, 0.7, 300, () -> {
                    });
                });

                child.setOnMouseExited(event -> {
                    NodeUtil.applyFadeTransition(child, 0.7, 1, 300, () -> {
                    });
                });
            }
        });

        rejectBtn.setOnAction(event -> {
            rejectBtn.getScene().getWindow().hide();
        });

    }

    @FXML
    public void setUpForm(double amountPaid, List<ChiTietHoaDon> cthd, KhachHang khachHang, double diemSuDung, LocalDateTime createDate) {
        amountPaidField.setText(String.valueOf(amountPaid).replace(".0", ""));

        TextFormatter<String> moneyFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        givenMoney.setTextFormatter(moneyFormatter);

        givenMoney.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    change.setText(String.valueOf(Double.parseDouble(givenMoney.getText()) - amountPaid).replace(".0", ""));
                }
            }
        });

        submitBtn.setOnAction(event -> {
            HoaDon hoaDon = new HoaDon();
            try {
                hoaDon = new HoaDon(generateId(), khachHang, new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap(), createDate, Double.parseDouble(givenMoney.getText()), diemSuDung, "Tiền mặt", cthd);
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }

            try {
                handleCheckoutConfirmation(hoaDon);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleCheckoutConfirmation(HoaDon hoaDon) throws SQLException {
        if (givenMoney.getText() == null) {
            givenMoneyAlert.setText("Chưa nhập số tiền khách đưa.");
            givenMoneyAlert.setVisible(true);
            return;
        }

        if (Double.parseDouble(givenMoney.getText()) < Double.parseDouble(amountPaidField.getText())) {
            givenMoneyAlert.setText("Số tiền khách đưa phải lớn hơn số tiền thanh toán.");
            givenMoneyAlert.setVisible(true);
            return;
        }

        if (new HoaDon_BUS().createHoaDon(hoaDon)) {
            showSuccessfulModal("Thanh toán thành công");
            isSuccess = true;
            submitBtn.getScene().getWindow().hide();
        }
        givenMoneyAlert.setVisible(false);
    }

    @FXML
    private void showSuccessfulModal(String message) {
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

    private String generateId() throws SQLException {
        int productNumber = new HoaDon_BUS().countHoaDon();
        String id = String.format("HD%04d", productNumber + 1);

        return id;
    }

    @FXML
    public boolean getStatus() {
        return isSuccess;
    }
}
