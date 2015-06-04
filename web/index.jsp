<%@ page import="Locale.LangBundle" %>
<%@ page import="Util.WebService.WSPublisher" %>
<%--
  Created by IntelliJ IDEA.
  User: pwwpche
  Date: 2014/5/2
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    HttpSession httpSession = request.getSession();
    LangBundle bundle = new LangBundle();
    WSPublisher.publish();

    if(request.getParameter("changeLang") != null){
        bundle.changeLanguage(session);
        response.sendRedirect("index.jsp");
    }

    if(httpSession.getAttribute("username") != null){
        if(httpSession.getAttribute("username").toString().equals("admin")){
            response.sendRedirect("adminUser.jsp");
        }else {
            response.sendRedirect("userInterface.jsp");
        }
        return ;
    }


%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Book Market</title>

    <!-- Bootstrap core CSS -->
    <link href="dist/css/bootstrap.min.css" rel="stylesheet">


    <!-- Custom styles for this template -->
    <link href="dist/css/signin.css" rel="stylesheet">
    <script>
        function reg(){
            window.location.href="Register.jsp";
        }
        function changeLang(){
            window.location.href="index.jsp?changeLang=1"
        }
    </script>

</head>

<body>

<div class="container">
    <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
            <form class="form-signin" role="form" method="POST" action="loginCheck">
                <h2 class="form-signin-heading"><%=bundle.getStr("LoginTitle", session)%></h2>
                <input name="username" type="text" class="form-control" placeholder="Username" required autofocus>
                <input name="password" type="password" class="form-control" placeholder="Password" required >
                <!--
                <label class="checkbox">
                    <input type="checkbox" value="remember-me"> Remember me
                </label>
                -->
                <button class="btn btn-lg btn-primary btn-block" type="submit"><%=bundle.getStr("Login", session)%></button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-4"></div>
        <div class="col-sm-4">
            <button class="btn btn-lg" onclick="reg()" style="float: right"><%=bundle.getStr("Register", session)%></button>
            <button class="btn btn-lg" onclick="changeLang()" style="float: right"><%=bundle.getStr("ChangeLang", session)%></button>
        </div>

    </div>
</div> <!-- /container -->


<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
</body>
</html>


