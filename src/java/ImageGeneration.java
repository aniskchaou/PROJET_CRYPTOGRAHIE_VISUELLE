
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.Query.gt;
import javax.servlet.ServletContext;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageGeneration extends Thread {

    String s = null;
    MySQLAccess dao;

    public static void main(String[] args) {
        ImageGeneration hc = new ImageGeneration();
        //start the thread
        hc.start();

    }

    public void run() {
        dao = new MySQLAccess();

        while (true) {
            String token = dao.find_captcha();

            if (token != null) {
             
                //   initialisation
                Mat imageArray;
                double alpha = 0.5;
                double beta;
                beta = (1.0 - alpha);
               
                // Load images to compare
                String key1 = dao.find_key1(token);
                String key2 = dao.find_key2(token);
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

                File f = new File("web/res/key1/" + key1);
                File f2 = new File("web/res/key2/" + key2);
               
                //if the key1 and key 2 exist
                if (f.exists() && f2.exists()) {
                    //retrieve the images
                    Mat img_key1 = Imgcodecs.imread("web/res/key1/" + key1);
                    Mat img_key2 = Imgcodecs.imread("web/res/key2/" + key2);
                    Mat dstination_image = Imgcodecs.imread("web/res/key1/" + key1);

                    //blend the images
                    Core.addWeighted(img_key1, alpha, img_key2, beta, 0.0, dstination_image);
                    Imgcodecs.imwrite("web/res/generated/" + token + ".png", dstination_image);
                }

                //update the database
                try {
                    dao.finish_generation(token);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ImageGeneration.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ImageGeneration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                sleep(6000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ImageGeneration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
