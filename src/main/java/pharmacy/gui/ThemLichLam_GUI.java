package pharmacy.gui;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;

public class ThemLichLam_GUI {
	@FXML
	private Button addScheduleBtn;

	@FXML
	private TextField employeeField;

	@FXML
	private ScrollPane suggestBox;

	@FXML
	private ListView<NhanVien> employeeList;

	private String employee;

	@FXML
	public void initialize() {
		handleAddScheduleBtnAction();
		handleSuggestEmployees();
	}

	@FXML
	public void handleAddScheduleBtnAction() {
		addScheduleBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(addScheduleBtn, 1, 0.7, 300, () -> {
			});
		});

		addScheduleBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(addScheduleBtn, 0.7, 1, 300, () -> {
			});
		});

		addScheduleBtn.setOnAction(event -> {
			employee = employeeField.getText();
			Stage stage = (Stage) addScheduleBtn.getScene().getWindow();
			stage.close();
		});

		addScheduleBtn.setDisable(true);

	}

	@FXML
	public void handleSuggestEmployees() {
		ObservableList<NhanVien> suggestedEmployees = FXCollections.observableArrayList(
				new NhanVien("MK0001", "Nguyễn Văn A", "a@example.com", "0123456789","ccc@getEmail.com", LocalDate.of(2020, 1, 1),
						"Hoạt động", "Cấp 1", "Nam", LocalDate.of(1990, 1, 1), 0, ""),
				new NhanVien("MK0002", "Nguyễn Văn B", "b@example.com", "0123456788","ccc@getEmail.com", LocalDate.of(2020, 1, 1),
						"Hoạt động", "Cấp 1", "Nam", LocalDate.of(1991, 1, 1), 0, ""),
				new NhanVien("MK0003", "Nguyễn Văn C", "c@example.com", "0123456787","ccc@getEmail.com", LocalDate.of(2020, 1, 1),
						"Hoạt động", "Cấp 1", "Nam", LocalDate.of(1992, 1, 1), 0, ""),
				new NhanVien("MK0004", "Nguyễn Văn D", "d@example.com", "0123456786","ccc@getEmail.com", LocalDate.of(2020, 1, 1),
						"Hoạt động", "Cấp 1", "Nam", LocalDate.of(1993, 1, 1), 0, ""));

		employeeList.getItems().addAll(suggestedEmployees);

		employeeList.setCellFactory(listView -> new ListCell<NhanVien>() {
			@Override
			protected void updateItem(NhanVien item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(item.getMaNhanVien() + " - " + item.getHoTen());
				}
			}
		});

		// handle select action
		employeeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				employeeField.setText(newValue.getMaNhanVien() + " - " + newValue.getHoTen());
				NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
				});
				NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
					suggestBox.setVisible(false);
					employeeList.getSelectionModel().clearSelection();
				});
			}
		});

		// handle input employee info and show suggestion box
		suggestBox.setVisible(false);
		employeeField.setOnKeyTyped(event -> {
			Boolean hasEmployee = false;

			if ("ccc".equals(employeeField.getText())) {
				suggestBox.setVisible(true);
				NodeUtil.applyFadeTransition(suggestBox, 0, 1, 200, () -> {
				});
				NodeUtil.applyTranslateYTransition(suggestBox, -15, 0, 200, () -> {
				});
			} else {
				NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
				});
				NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
					suggestBox.setVisible(false);
				});
			}

			// will be replace by hasEmployee
			if (employeeField.getText().equals("")) {
				NodeUtil.applyFadeTransition(addScheduleBtn, 1, 0.7, 300, () -> {
					addScheduleBtn.setDisable(true);
				});
			} else {
				NodeUtil.applyFadeTransition(addScheduleBtn, 0.7, 1, 300, () -> {
					addScheduleBtn.setDisable(false);
				});
			}
		});
	}

	@FXML
	public String getEmployee() {
		return employeeField.getText();
	}
}