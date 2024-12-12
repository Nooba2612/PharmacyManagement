package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.utils.NodeUtil;

public class MainLayout_md_GUI {

    @FXML
    private Label username;

    @FXML
    private Text userRole;

    @FXML
    private ImageView userAvatar;

    @FXML
    private HBox root;

    @FXML
    private HBox headerRightBox;

    @FXML
    private Pane userInfo;

    @FXML
    private GridPane menu;

    @FXML
    private Pane dashboardBtn;

    @FXML
    private Label logoLabel;

    @FXML
    private VBox profilePopover;

    @FXML
    private Pane menuBtnPane;

    @FXML
    private ScrollPane mainContentPane;

    @FXML
    private HBox header;

    @FXML
    private Pane logoutBtn;

    @FXML
    private Pane settingBtn;

    @FXML
    private Pane aboutUsBtn;

    @FXML
    private VBox logo;

    private Parent mainContent;
    private final Screen screen = Screen.getPrimary();
    private final Rectangle2D bounds = screen.getBounds();
    private final double screenWidth = bounds.getWidth();
    private final double screenHeight = bounds.getHeight();

    @FXML
    public void initialize() throws IOException, SQLException {
        // responesiveLayout();
        mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
        mainContentPane.setContent(mainContent);

        handleMenuAction();

        handleProfileBtnAction();

        handleLogoutAccount();
    }

    @FXML
    public void responesiveLayout() throws IOException {
        root.setMinSize(screenWidth, screenHeight);
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            mainContentPane.setPrefWidth(newVal.doubleValue() - header.getPrefWidth());
            logo.setMinWidth(newVal.doubleValue() - 550);
        });

        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            mainContentPane.setPrefHeight(newVal.doubleValue() - header.getPrefHeight());
        });

        // set header layout
        headerRightBox
                .setMinWidth(screen.getBounds().getWidth() - (menuBtnPane.getPrefWidth() + logoLabel.getPrefWidth()));
        logoLabel.setMinWidth(
                screen.getBounds().getWidth() - (menuBtnPane.getPrefWidth() + headerRightBox.getPrefWidth()));

        // set main content, default is home frame
        mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
        mainContentPane.setContent(mainContent);
    }

    @FXML
    public void handleMenuAction() {
        VBox firstMenuItem = (VBox) menu.getChildren().getFirst();

        Node icon = firstMenuItem.getChildren().get(0);
        Node name = firstMenuItem.getChildren().get(1);

        firstMenuItem.setStyle("-fx-background-color: #F0F0F0;");
        name.setStyle("-fx-fill: #339933;");

        handleHoverMenuItem();

        for (Node item : menu.getChildren()) {
            item.setOnMouseClicked(event -> {
                handleActiveMenuItem(event);
                try {
                    handeChangeFrame(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    @FXML
    public void handeChangeFrame(Node frameBtn) throws IOException {
        String buttonId = frameBtn.getId();

        switch (buttonId) {
            case "homeBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
            case "statisticBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThongKe_GUI.fxml"));
            case "customersBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
            case "employeesBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhanVien_GUI.fxml"));
            case "workScheduleBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/LichLam_GUI.fxml"));
            case "suppliersBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhaCungCap_GUI.fxml"));
            case "medicinesBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/SanPham_GUI.fxml"));
            case "invoicesBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/HoaDon_GUI.fxml"));
            case "equipmentsBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThietBiYTe_GUI.fxml"));
            case "goodsReceiptBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
            case "categoryBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/DanhMuc_GUI.fxml"));
            case "aboutUsBtn" ->
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/GioiThieuChung_GUI.fxml"));
            case "settingBtn" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CaiDat_GUI.fxml"));
                mainContent = loader.load();
                CaiDat_GUI caiDatGUI = loader.getController(); 
                caiDatGUI.setStatusOfMenuBar("horizontal");
            }
            default ->
                throw new IllegalArgumentException("Không có nút nào tương ứng với ID: " + buttonId);
        }

        mainContentPane.setContent(mainContent);
        mainContentPane.setVvalue(0);
        mainContentPane.setHvalue(0);
    }

    @FXML
    public void handleHoverMenuItem() {
        for (Node item : menu.getChildren()) {

            item.setOnMouseEntered(event -> {
                if (!NodeUtil.hasClass(item, "active-category-item")) {
                    item.setStyle("-fx-background-color: #F0F0F0;");
                }
            });

            item.setOnMouseExited(event -> {
                if (!NodeUtil.hasClass(item, "active-category-item")) {
                    item.setStyle("-fx-background-color: #FFFFFF;");
                }
            });

            item.setOnMouseClicked(event -> {
                handleActiveMenuItem(event);
            });
        }
    }

    @FXML
    public void handleActiveMenuItem(MouseEvent event) {

        Node categoryItem = (Node) event.getSource();

        for (Node item : menu.getChildren()) {
            VBox pane = (VBox) item;
            Node icon = pane.getChildren().get(0);
            Node name = pane.getChildren().get(1);

            if (!NodeUtil.hasClass(item, "active-category-item") && (item == categoryItem)) {
                NodeUtil.addClass(item, "active-category-item");
                item.setStyle("-fx-background-color: #F0F0F0;");
                name.setStyle("-fx-fill: #339933;");

            } else if (NodeUtil.hasClass(item, "active-category-item") && (item != categoryItem)) {
                NodeUtil.removeClass(item, "active-category-item");
                item.setStyle("-fx-background-color: #FFFFFF;");
                name.setStyle("-fx-fill: #616961;");

            }
        }

    }

    @FXML
    public void handleProfileBtnAction() throws IOException, SQLException {
        profilePopover.setVisible(false);
        profilePopover.setLayoutX(screen.getBounds().getWidth() - profilePopover.getPrefWidth() - 10);
        List<Node> profilePopoverItemList = profilePopover.getChildren();
        Node moreIcon = userInfo.getChildren().get(2);

        username.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getHoTen());
        userRole.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getChucVu());

        settingBtn.setOnMouseClicked(event -> {
            try {
                handeChangeFrame(settingBtn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleProfilePopoverShowHide();
        });

        userInfo.setOnMouseClicked(event -> {
            if (profilePopover.isVisible()) {
                userInfo.setStyle("-fx-background-color: transparent;");
                NodeUtil.applyFadeTransition(profilePopover, 1.0, 0.0, 300, () -> profilePopover.setVisible(false));
                NodeUtil.applyTranslateYTransition(profilePopover, 0, -20, 300, () -> {
                });
                NodeUtil.applyRotateTransition(moreIcon, 180, 0, 200, () -> {
                });
            } else {
                profilePopover.setVisible(true);
                NodeUtil.applyFadeTransition(profilePopover, 0.0, 1.0, 300, () -> {
                });
                NodeUtil.applyTranslateYTransition(profilePopover, -20, 0, 300, () -> {
                });
                userInfo.setStyle("-fx-background-color: #2DCB2D;");
                NodeUtil.applyRotateTransition(moreIcon, 0, 180, 200, () -> {
                });
            }
        });

        for (Node item : profilePopoverItemList) {
            item.setOnMouseEntered(event -> {
                item.setStyle("-fx-background-color: #F0F0F0;");
            });

            item.setOnMouseExited(event -> {
                item.setStyle("-fx-background-color: #FFF;");
            });
        }
    }

    @FXML
    public void handleProfilePopoverShowHide() {
        Node moreIcon = userInfo.getChildren().get(2);

        if (profilePopover.isVisible()) {
            userInfo.setStyle("-fx-background-color: transparent;");
            NodeUtil.applyFadeTransition(profilePopover, 1.0, 0.0, 300, () -> profilePopover.setVisible(false));
            NodeUtil.applyTranslateYTransition(profilePopover, 0, -20, 300, () -> {
            });
            NodeUtil.applyRotateTransition(moreIcon, 180, 0, 200, () -> {
            });
        } else {
            profilePopover.setVisible(true);
            NodeUtil.applyFadeTransition(profilePopover, 0.0, 1.0, 300, () -> {
            });
            NodeUtil.applyTranslateYTransition(profilePopover, -20, 0, 300, () -> {
            });
            userInfo.setStyle("-fx-background-color: #2DCB2D;");
            NodeUtil.applyRotateTransition(moreIcon, 0, 180, 200, () -> {
            });
        }
    }

    @FXML
    public void handleLogoutAccount() {
        logoutBtn.setOnMouseClicked(event -> {
            try {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.hide();
                Stage loginStage = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DangNhap_GUI.fxml"));
                Parent loginRoot = loader.load();

                TaiKhoan_BUS taiKhoanBUS = new TaiKhoan_BUS();
                taiKhoanBUS.logoutAccount(taiKhoanBUS.getCurrentAccount().getTenDangNhap().getMaNhanVien());

                Scene scene = new Scene(loginRoot, 468, 567);
                loginStage.setScene(scene);
                loginStage.setMaximized(false);
                loginStage.sizeToScene();
                loginStage.setTitle("Medkit - Pharmacy Management System");
                loginStage.getIcons()
                        .add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
                loginStage.show();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
