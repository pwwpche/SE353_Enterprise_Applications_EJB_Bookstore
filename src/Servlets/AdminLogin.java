package Servlets;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pwwpche on 2014/5/2.
 *
 */


public class AdminLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adminName = request.getParameter("username");
        String adminPass = request.getParameter("password");
        if (adminName == null || adminPass == null)
        {
            response.sendRedirect("adminLogin.jsp");
            return ;
        }
        if(adminName.equals("admin") && adminPass.equals("admin")){
            System.out.println("admin pass");
            HttpSession session = request.getSession();
            session.setAttribute("username", "admin");
            session.setAttribute("login", "true");
            response.sendRedirect("adminUser.jsp");
        }
        else {
            response.sendRedirect("adminLogin.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
