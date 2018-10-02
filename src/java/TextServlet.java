
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.Query.gt;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@WebServlet(urlPatterns = {"/TextServlet"})
@MultipartConfig
public class TextServlet extends HttpServlet {

    public static boolean test = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //get parameters
        String user_login = request.getParameter("user_login");
        String user_pass = request.getParameter("user_pass");
        String token = request.getParameter("token").toString();
        String img_code = request.getParameter("img_code");
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        //get user and password from database
        MySQLAccess msa = new MySQLAccess();
        boolean connexion = msa.check_user_connexion(user_login, user_pass);

        //print writer
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");
          
        //if input data are true
        if (connexion) {

            if (filePart.getSize() != 0) {

                
                String path = "C:\\Users\\user\\Documents\\NetBeansProjects\\ProjetSecurite\\web\\res\\key2";
                OutputStream outs = null;
                InputStream filecontent = null;
                final PrintWriter writer = response.getWriter();
              
                //upload file
                try {
                    outs = new FileOutputStream(new File(path + File.separator + fileName));
                    filecontent = filePart.getInputStream();
                    int read = 0;
                    final byte[] bytes = new byte[1024];
                    while ((read = filecontent.read(bytes)) != -1) {
                        outs.write(bytes, 0, read);
                    }
                    writer.println(fileName + " created at " + path);
                } catch (FileNotFoundException fne) {
                    writer.println("Error in file upload ERROR:" + fne.getMessage());
                } finally {
                    if (outs != null) {
                        outs.close();
                    }
                    if (filecontent != null) {
                        filecontent.close();
                    }
                }

                //save code into session
                HttpSession ses = request.getSession(true);
                ses.setAttribute("token", token);
                ses.setAttribute("generated_image", token + ".png");

                String[] tab = img_code.split("/");

                //generate the  keys
                try {
                    msa.generate_images(token, fileName, tab[1]);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TextServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(TextServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
               
                //redirect tp new servlet 
                response.sendRedirect("http://localhost:8080/ProjetSecurite/NewServlet");
            } else {
                out.println("<br><br><div class=\"alert alert-danger\" role=\"alert\">\n <h4> There is a Problem Uploading Your Key .</h4>\n"
                        + "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");
            }

        } else {
            out.println(" <br><br><div class=\"alert alert-danger\" role=\"alert\">\n <h4> Invalid username or password .</h4>\n"
                    + "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
