package Servlets;

import Util.WebService.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Created by pwwpche on 2014/5/2.
 */
public class LoginCheck extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        username = username == null ? "" : username;
        String password = request.getParameter("password");
        password = password == null ? "" : password;
        try {
            request.login(username, password);
            String sessionId = httpSession.getId();
            System.out.println("http session id is " + sessionId);
            httpSession.setAttribute("username", username);
            if(username.equals("admin")){
                response.sendRedirect("adminUser.jsp");
            }else {
                response.sendRedirect("userInterface.jsp");
            }
        }catch (Exception e){
            out.write("Password incorrect");
            request.setAttribute("resp", e.getMessage());
            response.sendRedirect("index.jsp");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
