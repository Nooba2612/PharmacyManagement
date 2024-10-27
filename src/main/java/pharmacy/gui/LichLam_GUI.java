package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pharmacy.entity.LichLamViec;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;

public class LichLam_GUI {

	@FXML
	private Button currentTimeBtn;

	@FXML
	private Button nextBtn;

	@FXML
	private Button prevBtn;

	@FXML
	private DatePicker dateSelect;

	@FXML
	private HBox root;

	@FXML
	private TableColumn<LichLamViec, String> shiftColumn;

	@FXML
	private TableColumn<LichLamViec, Void> mondayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> tuesdayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> wednesdayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> thursdayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> fridayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> saturdayColumn;

	@FXML
	private TableColumn<LichLamViec, Void> sundayColumn;

	@FXML
	private TableView<LichLamViec> workScheduleTable;

	@FXML
	private void initialize() {
		setupScheduleTable();
	}

	@FXML
	public void setupScheduleTable() {
		shiftColumn.setCellValueFactory(new PropertyValueFactory<>("caLam"));

		Callback<TableColumn<LichLamViec, Void>, TableCell<LichLamViec, Void>> cellFactory = new Callback<TableColumn<LichLamViec, Void>, TableCell<LichLamViec, Void>>() {
			@Override
			public TableCell<LichLamViec, Void> call(final TableColumn<LichLamViec, Void> param) {
				return new TableCell<LichLamViec, Void>() {
					Image addIcon = new Image(getClass().getResourceAsStream("/images/plus-icon.png"));
					ImageView addIconImage = new ImageView(addIcon);
					Button addButton = new Button("Thêm Lịch", addIconImage);

					{
						addIconImage.setFitWidth(20);
						addIconImage.setFitHeight(20);

						addButton.setVisible(false);
						addButton.setStyle(
								"-fx-background-color:  linear-gradient(to right, rgb(63, 188, 63), rgb(0, 230, 0)); -fx-text-fill: white; -fx-font-weight: bold;");

						addButton.setOnMouseEntered(event -> {
							NodeUtil.applyFadeTransition(addButton, 1, 0.6, 300, () -> {
							});
						});

						addButton.setOnMouseExited(event -> {
							NodeUtil.applyFadeTransition(addButton, 0.6, 1, 300, () -> {
							});
						});

					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							VBox box = new VBox(addButton);
							box.setStyle("-fx-alignment: center;");
							setGraphic(box);
							setStyle("-fx-alignment: center;");

							box.setOnMouseEntered(event -> {
								addButton.setVisible(true);
								NodeUtil.applyFadeTransition(addButton, 0, 1, 300, () -> {
								});
							});

							box.setOnMouseExited(event -> {
								addButton.setVisible(false);
							});

							// add schedule btn click
							addButton.setOnAction(event -> {
								LichLamViec data = getTableView().getItems().get(getIndex());
								String shift = data.getCaLam();
								try {
									openAddSchedulePopup(box, shift, getIndex(), param);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
						}
					}
				};
			}
		};

		mondayColumn.setCellFactory(cellFactory);
		tuesdayColumn.setCellFactory(cellFactory);
		wednesdayColumn.setCellFactory(cellFactory);
		thursdayColumn.setCellFactory(cellFactory);
		fridayColumn.setCellFactory(cellFactory);
		saturdayColumn.setCellFactory(cellFactory);
		sundayColumn.setCellFactory(cellFactory);

		ObservableList<LichLamViec> shifts = FXCollections.observableArrayList(
				new LichLamViec("123", "Ca 1 (7h-15h)",
						new NhanVien("MK0001", "John Doe", "Nhân viên", "0123456789", "ccc@getEmail.com",
								LocalDate.of(2022, 5, 10),
								"Đã nghỉ", "Đại học", "Nam", LocalDate.of(1990, 1, 1), 0, ""),
						LocalDate.now()),
				new LichLamViec(
						"456", "Ca 2 (15h-22h)",
						new NhanVien("MK0002", "Jane Smith", "Người quản lý", "0987654321", "ccc@getEmail.com",
								LocalDate.of(2021, 11, 15), "Còn làm", "Đại học", "Nữ", LocalDate.of(1992, 2, 2), 0,
								""),
						LocalDate.now()));

		workScheduleTable.setItems(shifts);
	}

	private void openAddSchedulePopup(VBox box, String shift, int rowIndex, TableColumn<LichLamViec, Void> column)
			throws IOException {

		String dateOfWeek;
		switch (column.getId()) {
			case "mondayColumn":
				dateOfWeek = "Thứ hai";
				break;
			case "tuesdayColumn":
				dateOfWeek = "Thứ ba";
				break;
			case "wednesdayColumn":
				dateOfWeek = "Thứ tư";
				break;
			case "thursdayColumn":
				dateOfWeek = "Thứ năm";
				break;
			case "fridayColumn":
				dateOfWeek = "Thứ sáu";
				break;
			case "saturdayColumn":
				dateOfWeek = "Thứ bảy";
				break;
			case "sundayColumn":
				dateOfWeek = "Chủ nhật";
				break;
			default:
				throw new IllegalArgumentException("Unexpected column: " + column.getId());
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemLichLam_GUI.fxml"));
		Parent addSchedulePopup = loader.load();

		Stage popupStage = new Stage();
		Scene popupScene = new Scene(addSchedulePopup);
		ThemLichLam_GUI controller = loader.getController();

		popupStage.setScene(popupScene);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));
		popupStage.setTitle(shift.substring(0, 5) + " - " + dateOfWeek);
		popupStage.setResizable(false);

		popupStage.showAndWait();

		String employee = controller.getEmployee();

		if (employee != null) {
			updateSchedule(box, employee);
		}

	}

	private void updateSchedule(VBox box, String employee) {
		Label employeeText = new Label(employee);

		employeeText.setTextFill(Color.web("#339933"));
		employeeText.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
		employeeText.setWrapText(true);

		box.getChildren().clear();
		box.getChildren().add(employeeText);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(10));
		box.setStyle("-fx-background-color: #f0f0f0; -fx-border-width: 2px; -fx-border-color: #339933;");
	}

}
