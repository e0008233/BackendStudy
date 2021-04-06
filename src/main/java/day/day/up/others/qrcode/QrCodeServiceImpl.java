package day.day.up.others.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class QrCodeServiceImpl implements QrCodeService{
    @Override
    public void generateQrCode() throws IOException, WriterException {
        String content = "https:www.google.com?uid=1232131";//二维码内容
        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToPath(bitMatrix, format, new File("/Users/zhang/Desktop/qr/zxing.png").toPath());// 输出原图片
        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
        /*
            问题：生成二维码正常,生成带logo的二维码logo变成黑白
            原因：MatrixToImageConfig默认黑白，需要设置BLACK、WHITE
            解决：https://ququjioulai.iteye.com/blog/2254382
         */
        bitMatrix = changeColor(bitMatrix,width,height);
        BufferedImage bufferedImage = logoMatrix(MatrixToImageWriter.toBufferedImage(bitMatrix,matrixToImageConfig), new File("/Users/zhang/Desktop/qr/logo.png"));
//        BufferedImage bufferedImage = LogoMatrix(toBufferedImage(bitMatrix), new File("D:\\logo.png"));
//        int[] data = new int[bitMatrix.getWidth() * bitMatrix.getHeight()];
//
//        bufferedImage.getRaster().setDataElements(0, 0, width, height, data);

        ImageIO.write(bufferedImage, "png", new File("/Users/zhang/Desktop/qr/zxing2.png"));//输出带logo图片
        System.out.println("输出成功.");
    }


    public BitMatrix changeColor(BitMatrix matrix, int width, int height) throws IOException {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] data = new int[w * h];
        boolean flag1=true;
        int stopx=0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if(matrix.get(x, y)){
                    if(flag1){
                        flag1=false;
                    }
                }else{
                    if(flag1==false){
                        stopx =x;
                        break;
                    }
                }
            }
            if(flag1==false)
                break;
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if(matrix.get(x, y)){
                    if((x<stopx)&&(y<stopx)){
                        Color color = new Color(231, 144, 56);
                        int colorInt = color.getRGB();
                        data[y * width + x] =colorInt;
                    }else{
                        int num1 = (int) (50 - (50.0 - 13.0)/ matrix.getHeight()* (y + 1));
                        int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight()* (y + 1));
                        int num3 = (int) (162 - (162.0 - 107.0)/ matrix.getHeight() * (y + 1));
                        Color color = new Color(num1, num2, num3);
                        int colorInt = color.getRGB();
                        data[y * w + x] = colorInt;
                    }
                }else{
                    data[y * w + x] = -1;//白色
                }
            }
        }
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        image.getRaster().setDataElements(0, 0, width, height, data);
        ImageIO.write(image, "png", new File("/Users/zhang/Desktop/qr/zxing1.png"));//输出带logo图片

        BufferedImage bufferedImage = logoMatrix(image, new File("/Users/zhang/Desktop/qr/logo.png"));
        ImageIO.write(bufferedImage, "png", new File("/Users/zhang/Desktop/qr/zxing3.png"));//输出带logo图片

        return matrix;
    }

    public BufferedImage logoMatrix(BufferedImage matrixImage, File logoFile) throws IOException{
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(logoFile);

        //开始绘制图片
        g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
        BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
        g2.setColor(Color.white);
        g2.draw(round);// 绘制圆弧矩形

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
        g2.setColor(new Color(128,128,128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush() ;
        return matrixImage ;
    }

    public void beautify(BufferedImage image){
        int width = 200;
        int height = 200;

        Graphics2D gs = image.createGraphics();
        gs.setBackground(Color.WHITE);
        gs.clearRect(0, 0, width, height);
    }
}
