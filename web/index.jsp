

<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.Random"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> </title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    </head>
    <body style="background-image: url(res/img/back.png);">
        <br><br>
        <div class="container" style="background-color: ghostwhite;">
            <br><br>
            <div class="row">
                <div class="col">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item active" aria-current="page"><h1>I have a key </h1></li>
                        </ol>
                    </nav>
                    <form class="form-horizontal" method="post" action="CaptchaServlet" enctype="multipart/form-data">
                        <fieldset>
                            <!-- Text input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_login"><h4>Login</h4></label>  
                                <div class="col-md-6">
                                    <input id="user_login" name="user_login" type="text" placeholder="Login" class="form-control input-md">
                                </div>
                            </div>
                            <!-- Password input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_pass"><h4>Password</h4></label>
                                <div class="col-md-6">
                                    <input id="user_pass" name="user_pass" type="password" placeholder="Password" class="form-control input-md">
                                </div>
                            </div>
                            <%               Random rand = new Random();
                                int n = rand.nextInt(4) + 1;
                                if (n == 1) {
                                    out.println("<h4>Key 1 Code : <span class='badge badge-danger'>5UEB</span></h4>  <br>");
                                    out.println("<img src='res/key1/key1_5UEB.png'  width='200' height='200' />");
                                    out.println("<input type='hidden' name='img_code' value='res/key1_5UEB.png' />");
                                }

                                if (n == 2) {
                                    out.println("<h4>Key 1 Code : <span class='badge badge-danger'>47LK</span></h4> <br>");
                                    out.println("<img src='res/key1/key1_47LK.png'  width='200' height='200' />");
                                    out.println("<input type='hidden' name='img_code' value='res/key1_47LK.png' />");
                                }

                                if (n == 3) {
                                    out.println("<h4>Key 1 Code :<span class='badge badge-danger'>TCV8</span></h4> <br>");
                                    out.println("<img src='res/key1/key1_TCV8.png'  width='200' height='200' />");
                                    out.println("<input type='hidden' name='img_code' value='res/key1_TCV8.png' />");
                                }

                                if (n == 4) {
                                    out.println("<h4>Key 1 Code :<span class='badge badge-danger'>U8T1</span></h4> <br>");
                                    out.println("<img src='res/key1/key1_U8T1.png'  width='200' height='200' />");
                                    out.println("<input type='hidden' name='img_code' value='res/key1_U8T1.png' />");
                                }
                                int token = rand.nextInt(562525) + 1;
                                out.println("<input type='hidden' name='token' value='" + token + "' />");

                            %>

                            <!-- Password input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_pass"><h4>Key 2</h4></label>
                                <div class="col-md-6">
                                    <input type="file" name="file" id="fileToUpload">
                                </div>
                            </div>
                            <!-- Button -->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="connexion_bt"></label>
                                <div class="col-md-4">
                                    <input type="submit" id="connexion_bt" name="connexion_bt" class="btn btn-primary" value="connect">
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
                <div class="col">
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item active" aria-current="page"><h1>I don't have a key</h1> </li>
                        </ol>
                    </nav>
                    <a class="btn btn-success" href="KeyGenerationServlet">Generate a key </a>
                    <form class="form-horizontal" method="post" action="AuthServlet" enctype="multipart/form-data">
                        <fieldset>
                            <!-- Text input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_login"><h4>Login</h4></label>  
                                <div class="col-md-6">
                                    <input id="user_login" name="user_login" type="text" placeholder="Login" class="form-control input-md">
                                </div>
                            </div>
                            <!-- Password input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_pass"><h4>Password</h4></label>
                                <div class="col-md-6">
                                    <input id="user_pass" name="user_pass" type="password" placeholder="Password" class="form-control input-md">
                                </div>
                            </div>
                            <!-- Password input-->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_pass"><h4>My code</h4></label>
                                <div class="col-md-6">
                                    <input id="user_pass" name="code" type="text" placeholder="My code" class="form-control input-md">
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="user_pass"><h4>Key 2</h4></label>
                                <div class="col-md-6">
                                    <input type="file" name="file" id="fileToUpload">
                                </div>
                            </div>
                            <!-- Button -->
                            <div class="form-group">
                                <label class="col-md-4 control-label" for="connexion_bt"></label>
                                <div class="col-md-4">
                                    <input type="submit" id="connexion_bt" name="connexion_bt" class="btn btn-primary" value="connect">
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
    </body>
</html>
