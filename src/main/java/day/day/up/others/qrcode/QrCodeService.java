package day.day.up.others.qrcode;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface QrCodeService {
    void generateQrCode() throws IOException, WriterException;
}
