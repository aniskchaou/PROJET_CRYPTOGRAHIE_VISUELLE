
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/KeyGenerationServlet"})
public class KeyGenerationServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        
        //initialisation
        int code = 0;
        HttpSession ses = request.getSession(true);
        MySQLAccess msa = new MySQLAccess();

        //generation the keys 
        try {
            code = msa.key_generation();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KeyGenerationServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(KeyGenerationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        //save the code in session
        ses.setAttribute("code", code);
        //print  code    
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");
        out.println("<br><br><div class=\"alert alert-primary\" role=\"alert\"><h1>\n Your Code is : " + code + "<a href='res/key_generation/key2/key2_" + code + ".png'> <span class=\"badge badge-warning\">Download  Image  Key </span></a></h1>"
                + "\n"
                + "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
