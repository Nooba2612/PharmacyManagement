package pharmacy.utils;

import java.io.File;

import com.itextpdf.io.exceptions.IOException;

import java.awt.*;

public class PDFUtil {

    public static void showPdfPreview(File pdfFile) throws IOException, java.io.IOException {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(pdfFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}