package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.utils.NodeUtil;

public class MainLayout_GUI extends Application {

    private Label username;
    private Text userRole;
    private ImageView userAvatar;
    private Parent root;
    private HBox headerRightBox;
    private Pane profilePane;
    private List<Node> categoryItemList;
    private Pane dashboardBtn;
    private Label logoLabel;
    private Button menuBtn;
    private VBox profilePopover;
    private Pane menuBtnPane;
    private ScrollPane mainContentPane;
    private VBox category;
    private BorderPane wrapperBorderPane;
    private Pane logoutBtn;
    private Pane settingBtn;
    private Pane categoryBtn;

    private Screen screen;
    private Rectangle2D bounds;
    private double screenWidth;
    private double screenHeight;
    private Parent mainContent;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        Scene scene;
        root = FXMLLoader.load(getClass().getResource("/fxml/MainLayout_GUI.fxml"));
        scene = new Scene(root);
        username = (Label) root.lookup("#username");
        userRole = (Text) root.lookup("#userRole");
        userAvatar = (ImageView) root.lookup("#userAvatar");
        headerRightBox = (HBox) root.lookup("#headerRightBox");
        profilePane = (Pane) root.lookup("#userInfo");
        categoryItemList = NodeUtil.findNodesWithClass(root, "categoryItem");
        dashboardBtn = (Pane) root.lookup("#dashboardBtn");
        logoutBtn = (Pane) root.lookup("#logoutBtn");
        settingBtn = (Pane) root.lookup("#settingBtn");
        menuBtnPane = (Pane) root.lookup("#menuBtnPane");
        logoLabel = (Label) root.lookup("#logoLabel");
        menuBtn = (Button) root.lookup("#menuBtn");
        profilePopover = (VBox) root.lookup("#profilePopover");
        mainContentPane = (ScrollPane) root.lookup("#mainContentPane");
        wrapperBorderPane = (BorderPane) root.lookup("#wrapper");
        category = (VBox) root.lookup("#category");
        categoryBtn = (Pane) root.lookup("#categoryBtn");
        screen = Screen.getPrimary();
        bounds = screen.getBounds();
        screenWidth = bounds.getWidth();
        screenHeight = bounds.getHeight();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        // set header layout
        headerRightBox.setMinWidth(screenWidth - (menuBtnPane.getPrefWidth() + logoLabel.getPrefWidth()));
        logoLabel.setMinWidth(screenWidth - (menuBtnPane.getPrefWidth() + headerRightBox.getPrefWidth()));

        // set inner root layout size
        wrapperBorderPane.setMinSize(screenWidth, screenHeight);

        // set main content, default is home frame
        mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
        mainContentPane.setContent(mainContent);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Medkit - Pharmacy Management System");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();

        handleCategoryAction();

        handleProfileBtnAction();

        handleLogoutAccount();
    }

    public void handleCategoryAction() {
        Pane firstCategoryItem = (Pane) categoryItemList.get(0);
        Node icon = firstCategoryItem.getChildren().get(0);
        Node name = firstCategoryItem.getChildren().get(1);

        firstCategoryItem.setStyle("-fx-background-color: #F0F0F0;");
        name.setStyle("-fx-fill: #339933;");

        NodeUtil.applyTranslateXTransition(icon, 0, 30, 300, () -> {
        });
        NodeUtil.applyTranslateXTransition(name, 0, 30, 300, () -> {
        });

        handleHoverCategoryItem();

        for (Node item : categoryItemList) {
            item.setOnMouseClicked(event -> {
                handleActiveCategoryItem(event);
                try {
                    handeChangeFrame(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // handle open and close category bar
        menuBtn.setOnMouseClicked(event -> {
            double categoryDefaultWidth = 265;

            if (category.isVisible()) {

                double newWidth = screenWidth + category.getPrefWidth();

                NodeUtil.applyFadeTransition(category, 1, 0, 300, () -> {
                    category.setVisible(false);
                    category.setPrefWidth(0);
                });

                NodeUtil.applyTranslateXTransition(mainContentPane, 0, -(category.getPrefWidth()), 200, () -> {
                    mainContentPane.setTranslateX(0);

                });

                NodeUtil.applyTranslateXTransition(category, 0, -(category.getPrefWidth()), 200, () -> {
                    mainContentPane.setMinWidth(newWidth);
                });
            } else {
                category.setPrefWidth(categoryDefaultWidth);
                category.setVisible(true);

                double newWidth = screenWidth - category.getPrefWidth();

                NodeUtil.applyFadeTransition(category, 0, 1, 300, () -> {

                });

                NodeUtil.applyTranslateXTransition(category, -(category.getPrefWidth()), 0, 200, () -> {

                });
                NodeUtil.applyTranslateXTransition(mainContentPane, 0, category.getPrefWidth(), 200, () -> {
                    mainContentPane.setMinWidth(newWidth);
                    mainContentPane.setTranslateX(0);
                });

            }
        });

    }

    public void handeChangeFrame(Node frameBtn) throws IOException {
        String buttonId = frameBtn.getId();

        switch (buttonId) {
            case "homeBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
                break;
            case "statisticBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThongKe_GUI.fxml"));
                break;
            case "customersBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
                break;
            case "employeesBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhanVien_GUI.fxml"));
                break;
            case "workScheduleBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/LichLam_GUI.fxml"));
                break;
            case "suppliersBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhaCungCap_GUI.fxml"));
                break;
            case "medicinesBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/Thuoc_GUI.fxml"));
                break;
            case "invoicesBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/HoaDon_GUI.fxml"));
                break;
            case "equipmentsBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThietBiYTe_GUI.fxml"));
                break;
            case "goodsReceiptBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
                break;
            case "categoryBtn":
                mainContent = FXMLLoader.load(getClass().getResource("/fxml/DanhMuc_GUI.fxml"));
                break;
            default:
                throw new IllegalArgumentException("Không có nút nào tương ứng với ID: " + buttonId);
        }

        mainContentPane.setContent(mainContent);
        mainContentPane.setVvalue(0);
        mainContentPane.setHvalue(0);
    }

    public void handleHoverCategoryItem() {
        for (Node item : categoryItemList) {
            Pane pane = (Pane) item;

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
                handleActiveCategoryItem(event);
            });
        }
    }

    public void handleActiveCategoryItem(MouseEvent event) {

        Node categoryItem = (Node) event.getSource();

        for (Node item : categoryItemList) {
            Pane pane = (Pane) item;
            Node icon = pane.getChildren().get(0);
            Node name = pane.getChildren().get(1);

            if (!NodeUtil.hasClass(item, "active-category-item") && (item == categoryItem)) {
                NodeUtil.addClass(item, "active-category-item");
                item.setStyle("-fx-background-color: #F0F0F0;");
                name.setStyle("-fx-fill: #339933;");
                NodeUtil.applyTranslateXTransition(icon, 0, 30, 300, () -> {
                });
                NodeUtil.applyTranslateXTransition(name, 0, 30, 300, () -> {
                });
            } else if (NodeUtil.hasClass(item, "active-category-item") && (item != categoryItem)) {
                NodeUtil.removeClass(item, "active-category-item");
                item.setStyle("-fx-background-color: #FFFFFF;");
                name.setStyle("-fx-fill: #616961;");
                NodeUtil.applyTranslateXTransition(icon, 30, 0, 300, () -> {
                });
                NodeUtil.applyTranslateXTransition(name, 30, 0, 300, () -> {
                });
            }
        }

    }

    public void handleProfileBtnAction() throws IOException, SQLException {
        profilePopover.setVisible(false);
        profilePopover.setLayoutX(screenWidth - profilePopover.getPrefWidth() - 10);
        List<Node> profilePopoverItemList = profilePopover.getChildren();
        Node moreIcon = profilePane.getChildren().get(2);

        username.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getHoTen());
        userRole.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getChucVu());

        profilePane.setOnMouseClicked(event -> {
            if (profilePopover.isVisible()) {
                profilePane.setStyle("-fx-background-color: transparent;");
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
                profilePane.setStyle("-fx-background-color: #2DCB2D;");
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

    public void handleLogoutAccount() {
        logoutBtn.setOnMouseClicked(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.hide();

            try {
                DangNhap_GUI dangNhapGUI = new DangNhap_GUI();
                stage.setMaximized(false);
                new TaiKhoan_BUS()
                        .logoutAccount(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getMaNhanVien());
                dangNhapGUI.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
