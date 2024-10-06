package pharmacy.gui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import pharmacy.utils.NodeUtil;

import java.io.IOException;
import java.sql.Wrapper;
import java.util.List;

import javax.lang.model.element.Element;

import org.springframework.expression.spel.ast.Identifier;

public class MainLayout_GUI extends Application {
	private Parent root;
	private HBox headerRightBox;
	private Pane profilePane;
	private List<Node> categoryItemList;
	private Pane dashboardBtn;
	private Label logoLabel;
	private Button categoryBtn;
	private VBox profilePopover;
	private Pane categoryBtnPane;
	private ScrollPane mainContentPane;
	private VBox category;
	private BorderPane wrapperBorderPane;

	private Screen screen;
	private Rectangle2D bounds;
	private double screenWidth;
	private double screenHeight;
	private Parent mainContent;

	@Override
	public void start(Stage primaryStage) throws IOException {
		Scene scene;
		root = FXMLLoader.load(getClass().getResource("/fxml/MainLayout_GUI.fxml"));
		scene = new Scene(root);
		headerRightBox = (HBox) root.lookup("#headerRightBox");
		profilePane = (Pane) root.lookup("#userInfo");
		categoryItemList = NodeUtil.findNodesWithClass(root, "categoryItem");
		dashboardBtn = (Pane) root.lookup("#dashboardBtn");
		categoryBtnPane = (Pane) root.lookup("#categoryBtnPane");
		logoLabel = (Label) root.lookup("#logoLabel");
		categoryBtn = (Button) root.lookup("#categoryBtn");
		profilePopover = (VBox) root.lookup("#profilePopover");
		mainContentPane = (ScrollPane) root.lookup("#mainContentPane");
		wrapperBorderPane = (BorderPane) root.lookup("#wrapper");
		category = (VBox) root.lookup("#category");
		screen = Screen.getPrimary();
		bounds = screen.getBounds();
		screenWidth = bounds.getWidth();
		screenHeight = bounds.getHeight();

		// set header layout
		headerRightBox.setMinWidth(screenWidth - (categoryBtnPane.getPrefWidth() + logoLabel.getPrefWidth()));
		logoLabel.setMinWidth(screenWidth - (categoryBtnPane.getPrefWidth() + headerRightBox.getPrefWidth()));

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
		categoryBtn.setOnMouseClicked(event -> {
			double categoryDefaultWidth = 265;

			if (category.isVisible()) {

				double newWidth = screenWidth + category.getPrefWidth();

				NodeUtil.applyFadeTransiton(category, 1, 0, 300, () -> {
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

				NodeUtil.applyFadeTransiton(category, 0, 1, 300, () -> {

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
		if ("homeBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("statisticBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThongKe_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("customersBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("employeesBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhanVien_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("workScheduleBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/LichLam_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("suppliersBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/NhaCungCap_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("medicinesBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/Thuoc_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("invoicesBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/HoaDon_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		} else if ("equipmentsBtn".equals(frameBtn.getId())) {
			mainContent = FXMLLoader.load(getClass().getResource("/fxml/ThietBiYTe_GUI.fxml"));
			mainContentPane.setContent(mainContent);
		}
		mainContentPane.setVvalue(0);
		mainContentPane.setHvalue(0);
	}

	public void handleHoverCategoryItem() {
		for (Node item : categoryItemList) {
			Pane pane = (Pane) item;
			Node icon = pane.getChildren().get(0);
			Node name = pane.getChildren().get(1);

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

	public void handleProfileBtnAction() {
		profilePopover.setVisible(false);
		profilePopover.setLayoutX(screenWidth - profilePopover.getPrefWidth() - 10);
		List<Node> profilePopoverItemList = profilePopover.getChildren();
		Node moreIcon = profilePane.getChildren().get(2);

		profilePane.setOnMouseClicked(event -> {
			if (profilePopover.isVisible()) {
				profilePane.setStyle("-fx-background-color: transparent;");
				NodeUtil.applyFadeTransiton(profilePopover, 1.0, 0.0, 300, () -> profilePopover.setVisible(false));
				NodeUtil.applyTranslateYTransition(profilePopover, 0, -20, 300, () -> {
				});
				NodeUtil.applyRotateTransiton(moreIcon, 180, 0, 200, () -> {
				});
			} else {
				profilePopover.setVisible(true);
				NodeUtil.applyFadeTransiton(profilePopover, 0.0, 1.0, 300, () -> {
				});
				NodeUtil.applyTranslateYTransition(profilePopover, -20, 0, 300, () -> {
				});
				profilePane.setStyle("-fx-background-color: #2DCB2D;");
				NodeUtil.applyRotateTransiton(moreIcon, 0, 180, 200, () -> {
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

}
