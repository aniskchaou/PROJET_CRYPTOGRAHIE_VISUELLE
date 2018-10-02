
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class CkeckKey extends Thread {

    String s = null;
    MySQLAccess dao;

    public static void main(String[] args) {
        CkeckKey hc = new CkeckKey();
        //start the thread
        hc.start();

    }

    public void run() {
        dao = new MySQLAccess();
        //load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        while (true) {
            //seek token 
            String token = dao.find_captcha_check();
            System.out.println("token :" + token);
            try {
                sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CkeckKey.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (token != null) {
                try {
                    //initialisation
                    Mat imageArray;
                    double alpha = 0.5;
                    double beta;

                    beta = (1.0 - alpha);

                    File f = new File("web\\res\\key_generation\\key2_user\\key2_" + token + ".png");
                    //check if url and file exist 
                    if (f.exists()) {
                        //retrive the images
                        Mat img_key1 = Imgcodecs.imread("web\\res\\key_generation\\key1\\key1_" + token + ".png");
                        Mat img_key2 = Imgcodecs.imread("web\\res\\key_generation\\key2_user\\key2_" + token + ".png");
                        Mat dstination_image = Imgcodecs.imread("web\\res\\key_generation\\key1\\key1_" + token + ".png");
                        //blend the images
                        System.out.println(" generating image in web\\res\\key_generation\\generated\\gen_" + token + ".png");
                        Core.addWeighted(img_key1, alpha, img_key2, beta, 0.0, dstination_image);
                        //save image 
                        Imgcodecs.imwrite("web\\res\\key_generation\\generated\\gen_" + token + ".png", dstination_image);
                    }

                    //wait 
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CkeckKey.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
}
