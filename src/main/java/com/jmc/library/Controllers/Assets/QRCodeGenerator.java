package com.jmc.library.Controllers.Assets;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jmc.library.Models.BookModel;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Objects;

public class QRCodeGenerator {
    public static void generateQRCodeImage(String text, int width, int height, String ISBN)
            throws WriterException, IOException {
        System.out.println("Generating QR code image for " + text);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        String filePath = "src/main/resources/IMAGES/QRCode" + ISBN + ".png";
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
    }

    public static Image getQRCodeImage() {
        String ISBN = BookModel.getInstance().getBookInfo().getISBN();
        try {
            String text = "https://books.google.com/books?isbn=" + ISBN;
            QRCodeGenerator.generateQRCodeImage(text, 350, 350, ISBN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("QR Code Generated");
        Image image = new Image("file:src/main/resources/IMAGES/QRCode" + ISBN + ".png");
        File file = new File("src/main/resources/IMAGES/QRCode" + ISBN + ".png");
        file.delete();
        return image;
    }
}