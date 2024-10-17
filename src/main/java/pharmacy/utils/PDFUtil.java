package pharmacy.utils;

import java.io.File;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.io.exceptions.IOException;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PDFUtil {

    public static void showPdfPreview(File pdfFile) throws IOException, java.io.IOException {
        Stage pdfStage = new Stage();
        pdfStage.initModality(Modality.APPLICATION_MODAL);
        pdfStage.initStyle(StageStyle.UNDECORATED);
        pdfStage.setTitle("Bản xem trước");
        pdfStage.setResizable(false);

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        String pdfJsPath = PDFUtil.class.getResource("/html/pdf_viewer.html").toExternalForm() + "?file=";
        String pdfUrl = pdfFile.toURI().toString();
        webEngine.load(pdfJsPath + pdfUrl);

        Button btnCancel = new Button("Hủy");
        Button btnSave = new Button("Lưu");
        Button btnPrint = new Button("In");
        HBox btnBox = new HBox(3);

        Image cancelIcon = new Image(PDFUtil.class.getResourceAsStream("/images/x-icon.png"));
        ImageView cancelImageView = new ImageView(cancelIcon);
        cancelImageView.setFitWidth(20);
        cancelImageView.setFitHeight(20);
        btnCancel.setGraphic(cancelImageView);

        Image saveIcon = new Image(PDFUtil.class.getResourceAsStream("/images/save-icon.png"));
        ImageView saveImageView = new ImageView(saveIcon);
        saveImageView.setFitWidth(20);
        saveImageView.setFitHeight(20);
        btnSave.setGraphic(saveImageView);

        Image printIcon = new Image(PDFUtil.class.getResourceAsStream("/images/printer-icon.png"));
        ImageView printImageView = new ImageView(printIcon);
        printImageView.setFitWidth(20);
        printImageView.setFitHeight(20);
        btnPrint.setGraphic(printImageView);

        btnCancel.setStyle(
                "-fx-background-color: #d24343; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5px 15px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-cursor: hand;");
        btnSave.setStyle(
                "-fx-background-color: #339933; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5px 15px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-cursor: hand;");
        btnPrint.setStyle(
                "-fx-background-color: #47d1ff; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5px 15px; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-cursor: hand;");
        btnCancel.setMinWidth(100);
        btnSave.setMinWidth(100);
        btnPrint.setMinWidth(100);

        btnCancel.setOnMouseEntered(event -> NodeUtil.applyFadeTransition(btnCancel, 1, 0.6, 300, () -> {
        }));
        btnCancel.setOnMouseExited(event -> NodeUtil.applyFadeTransition(btnCancel, 0.6, 1, 300, () -> {
        }));

        btnSave.setOnMouseEntered(event -> NodeUtil.applyFadeTransition(btnSave, 1, 0.6, 300, () -> {
        }));
        btnSave.setOnMouseExited(event -> NodeUtil.applyFadeTransition(btnSave, 0.6, 1, 300, () -> {
        }));

        btnPrint.setOnMouseEntered(event -> NodeUtil.applyFadeTransition(btnPrint, 1, 0.6, 300, () -> {
        }));
        btnPrint.setOnMouseExited(event -> NodeUtil.applyFadeTransition(btnPrint, 0.6, 1, 300, () -> {
        }));

        btnBox.getChildren().addAll(btnCancel, btnSave, btnPrint);
        btnBox.setSpacing(10);
        btnBox.setStyle("-fx-background-color: #929292; -fx-padding: 10px; -fx-border-width: 0px;");
        btnBox.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(webView);
        root.setBottom(btnBox);

        btnCancel.setOnAction(e -> pdfStage.close());

        btnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu PDF");
            fileChooser.setInitialFileName("data.pdf");
            File saveFile = fileChooser.showSaveDialog(pdfStage);
            if (saveFile != null) {
                try {
                    java.nio.file.Files.copy(pdfFile.toPath(), saveFile.toPath());
                    pdfStage.close();
                    System.out.println("\n\nFile Saved.\n\n");
                } catch (IOException | java.io.IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnPrint.setOnAction(e -> {
            try {
                showPrintTool(pdfFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Scene scene = new Scene(root, 850, 720);
        pdfStage.setScene(scene);
        pdfStage.show();
    }

    public static void showPrintTool(File pdfFile) throws IOException {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(null)) {
            job.endJob();
        }
    }

}