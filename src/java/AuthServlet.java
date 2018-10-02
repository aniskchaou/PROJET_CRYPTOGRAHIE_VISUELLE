
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns = {"/AuthServlet"})
@MultipartConfig
public class AuthServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //retrieve parameters 
        String code = request.getParameter("captcha");
        String img = request.getParameter("img").toString();

        //retrieve the captcha code from database 
        MySQLAccess msa = new MySQLAccess();
        String captcha = msa.get_captcha(img);

        //prepare the writer in order to print the message 
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");

        //print the result message 
        if (captcha.equals(code)) {

            out.println("<br><br><div class=\"alert alert-success\" role=\"alert\">\n <h4> The Captcha Code <span class=\"badge badge-danger\"> " + code + "</span> you Entered is Valid . </h4>\n"
                    + "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");
        } else {
            out.println("<br><br><div class=\"alert alert-danger\" role=\"alert\">\n <h4> The Captcha Code you Entered is Invalid . </h4>\n"
                    + "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        //retrive ^parameters
        String user_pass = request.getParameter("user_pass");
        String user_login = request.getParameter("user_login");
        String code = request.getParameter("code");
         final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);
        
        
        //add code in session
        String code_user = request.getSession(false).getAttribute("code").toString();

        //seek the user and the password in the databse
        MySQLAccess msa = new MySQLAccess();
        boolean connexion = msa.check_user_connexion(user_login, user_pass);

        //prepare writer
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        
        out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");

       

        //check whether the the input data are true
        if (code_user.equals(code) && connexion) {

            String path = "C:\\Users\\user\\Documents\\NetBeansProjects\\ProjetSecurite\\web\\res\\key_generation\\key2_user\\";
            OutputStream outs = null;
            InputStream filecontent = null;
            final PrintWriter writer = response.getWriter();
            
            //upload the file 
            try {
                outs = new FileOutputStream(new File(path + File.separator + fileName));
                filecontent = filePart.getInputStream();
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = filecontent.read(bytes)) != -1) {
                    outs.write(bytes, 0, read);
                }
                
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
             
           //write form
            out.println("<br><br><div class=\"alert alert-primary\" role=\"alert\">\n <h4> Enter the Code Below :</h4>\n"
                    + "<h6> The User Code is : <span class=\"badge badge-success\">" + code_user + " </span></h6> </div>  ");
            out.println("<form class='form-horizontal' method='get' action='AuthServlet' >");
            out.println("<img src='res/key_generation/generated/gen_" + code_user + ".png'  width='700' height='700' /><br><br>");
            out.println(" <h4>code</h4> <br><input id='code' name='captcha' type='text' ><br><br>");
            out.println("  <br><input  class=\"form-control input-md\" id='code'value='gen_" + code_user + ".png' name='img' type='hidden' >");
            out.println("<input type='submit' id='connexion_bt' name='connexion_bt' class='btn btn-primary' value='validate'> ");
            out.println("</form>");
        } else {
            out.println(" <br><br><div class=\"alert alert-danger\" role=\"alert\">\n <h4> Invalid username or password .</h4>\n" +
              "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>");    
        }

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
