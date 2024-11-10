package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import pharmacy.bus.ChiTietHoaDon_BUS;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.KhachHang;
import pharmacy.entity.SanPham;
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

        System.out.println(cthd);

        givenMoney.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    change.setText(String.valueOf(Double.parseDouble(givenMoney.getText()) - amountPaid).replace(".0", ""));
                }
            }
        });

        submitBtn.setOnAction(event -> {
            double tongTien = 0.0;
            if (cthd != null && !cthd.isEmpty()) {
                for (ChiTietHoaDon chiTietHoaDon : cthd) {
                    if (chiTietHoaDon == null || chiTietHoaDon.getMaSanPham() == null) {
                    } else {
                        SanPham sp = new SanPham();
                        try {
                            sp = new SanPham_BUS().getSanPhamByMaSanPham(chiTietHoaDon.getMaSanPham());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (sp != null) {
                            tongTien += (sp.getDonGiaBan() * chiTietHoaDon.getSoLuong())
                                    - (sp.getDonGiaBan() * chiTietHoaDon.getSoLuong() * chiTietHoaDon.getThue());
                        }
                    }
                }
            }

            HoaDon hoaDon = new HoaDon();
            try {
                hoaDon = new HoaDon(generateId(), khachHang, new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap(), createDate, Double.parseDouble(givenMoney.getText()), diemSuDung, "Tiền mặt", tongTien);
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
            }

            try {
                handleCheckoutConfirmation(hoaDon, cthd);

                int currentPoints = (int) (khachHang.getDiemTichLuy() - diemSuDung + (amountPaid / 100));
                KhachHang customer = new KhachHang(khachHang.getMaKhachHang(), khachHang.getHoTen(), khachHang.getSoDienThoai(), currentPoints, khachHang.getNamSinh(), khachHang.getGhiChu(), khachHang.getGioiTinh());
                new KhachHang_BUS().updateCustomer(customer);

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InHoaDon_GUI.fxml"));
                    Parent popupContent = loader.load();
                    InHoaDon_GUI controller = loader.getController();

                    controller.initialize(hoaDon);

                    Stage popupStage = new Stage();
                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.setTitle("Thêm khách hàng");
                    popupStage.getIcons().add(new Image(getClass().getResource("/images/money-icon.png").toExternalForm()));
                    popupStage.setScene(new Scene(popupContent));
                    popupStage.setResizable(false);

                    popupStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleCheckoutConfirmation(HoaDon hoaDon, List<ChiTietHoaDon> cthd) throws SQLException {
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
            if (cthd != null) {
                for (ChiTietHoaDon chiTietHoaDon : cthd) {
                    new ChiTietHoaDon_BUS().createChiTietHoaDon(chiTietHoaDon);
                }
            } else {
                System.out.println("Danh sách chi tiết hóa đơn trống!");
            }
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
