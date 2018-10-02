
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class KeyGeneration extends Thread {

    MySQLAccess dao;

    public static void main(String[] args) {
        KeyGeneration hc = new KeyGeneration();
        //start the thread
        hc.start();

    }

    public void run() {
        dao = new MySQLAccess();
        String code = null;

        while (true) {
            try {
                code = dao.find_key_generation();
                if (code != null) {

                    //generate a random number
                    Random rand = new Random();
                    int n = rand.nextInt(2) + 1;

                    //load the library
                    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

                    //read iamge
                    String captcha = dao.get_captcha_from_code(code);
                    Mat img_grey = Imgcodecs.imread("web\\res\\key_generation\\original\\" + captcha + ".png", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
                    System.out.println("web\\res\\key_generation\\original\\" + captcha + ".png");

                    //create an empty  images key1 and key2
                    Mat img = Mat.zeros(500, 1000, CvType.CV_8U);
                    Mat img2 = Mat.zeros(500, 1000, CvType.CV_8U);

                    //iterate the original image pixel by pixel grey image
                    int col_img2 = 0, col_img1 = 0;

                    for (int r = 0; r < img_grey.rows(); r++) {
                        col_img1 = 0;
                        col_img2 = 0;
                        for (int c = 0; c < img_grey.cols(); c++) {
                            double[] data = img_grey.get(r, c);

                            if ((n % 2) > 0) {
                                if (data[0] == 255) {
                                    //pixel is white

                                    //key 1
                                    img.put(r, col_img1, 0);
                                    img.put(r, ++col_img1, 255);
                                    //key 2
                                    img2.put(r, col_img1, 0);
                                    img2.put(r, ++col_img1, 255);
                                } else {
                                    //pixel is black

                                    //key 1
                                    img.put(r, col_img1, 0);
                                    img.put(r, ++col_img1, 255);
                                    //key 2
                                    img2.put(r, col_img2, 255);
                                    img2.put(r, ++col_img2, 0);

                                }
                            } else if (data[0] == 0) {
                                //pixel is white

                                //key 1
                                img.put(r, col_img1, 255);
                                img.put(r, ++col_img1, 0);
                                //key 2
                                img2.put(r, col_img2, 255);
                                img2.put(r, ++col_img2, 0);
                            } else {
                                //pixel is black

                                //key 1
                                img.put(r, col_img1, 255);
                                img.put(r, ++col_img1, 0);
                                //key 2
                                img2.put(r, col_img2, 0);
                                img2.put(r, ++col_img2, 255);

                            }
                            ++col_img1;
                            ++col_img2;

                        }
                    }
                    //save the image
                    Imgcodecs.imwrite("web\\res\\key_generation\\key1\\key1_" + code + ".png", img);
                    Imgcodecs.imwrite("web\\res\\key_generation\\key2\\key2_" + code + ".png", img2);

                    //update the database
                    try {
                        dao.finish_key_generation(code);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(KeyGeneration.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(KeyGeneration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //wait
                sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(KeyGeneration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
