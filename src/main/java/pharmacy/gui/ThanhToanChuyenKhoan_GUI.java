package pharmacy.gui;

import java.io.UnsupportedEncodingException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.connections.VNPay.PaymentController;

public class ThanhToanChuyenKhoan_GUI {

    @FXML
    private WebView webView;

    @FXML
    public void initialize(Double amount) {
        PaymentController vnpay = new PaymentController();

        try {
        	System.out.println("Payment url: " + vnpay.getPay(amount));
            webView.getEngine().load(vnpay.getPay(amount));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
