
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/NewServlet"})
public class NewServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Refresh", "10");
       
        
        try (PrintWriter out = response.getWriter()) {
           
            out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");
            
            //retrive code from session
            String session = request.getSession(false).getAttribute("token").toString();
            String img = request.getSession(false).getAttribute("generated_image").toString();
            
            
            //print html
            out.println("<br><br><div class=\"alert alert-primary\" role=\"alert\">\n <h4> Enter the Code Below :</h4>\n"
                    + "<h6> The User Code is : <span class=\"badge badge-success\">" + session + " </span></h6> </div>  ");

            out.println("<form class='form-horizontal' method='post' action='NewServlet' >");
            out.println("<img src='res/generated/" + session + ".png'  width='700' height='700' /><br>");
 
            out.println("<div class=\"form-group\">\n"
                    + "                    <label class=\"col-md-4 control-label\" for=\"user_login\"><h4>Captcha</h4></label>  \n"
                    + "                    <div class=\"col-md-6\">\n"
                    + "                        <input id=\"user_login\" name=\"captcha\" type=\"text\" placeholder=\"Captcha\" class=\"form-control input-md\">\n"
                    + "\n"
                    + "                    </div>\n"
                    + "                </div>");
            out.println("<input type='submit' id='connexion_bt' name='connexion_bt' class='btn btn-primary' value='validate'> ");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        }
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
        String captcha = request.getParameter("captcha");
        
        //seek the captcha from database
        MySQLAccess msa = new MySQLAccess();
        String code = msa.check_capcha(captcha);
       
        //print html
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Servlet TextServlet</title><link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\" crossorigin=\"anonymous\"></head><body>");
          
        // check if captcha code is true
        if (captcha.equals(code)) {
            
            out.println("<br><br><div class=\"alert alert-success\" role=\"alert\">\n <h4> The Captcha Code <span class=\"badge badge-danger\"> " + code + "</span> you Entered is Valid . </h4>\n" +
              "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");
        } else {
           
            out.println("<br><br><div class=\"alert alert-danger\" role=\"alert\">\n <h4> The Captcha Code you Entered is Invalid . </h4>\n" +
              "<h6>Please  <a href='index.jsp'><span class=\"badge badge-success\">Return </span></a>to the Home Page .</h6> </div>  ");
        }

        out.println("</body>");
        out.println("</html>");

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
