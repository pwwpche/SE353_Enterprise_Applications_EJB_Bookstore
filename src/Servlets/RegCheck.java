package Servlets;

import Entity.UserEntity;

import ejb.Utils.JsonBookGenerator;
import ejb.DAO.UserMgrBean;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * Created by pwwpche on 2014/5/2.
 */
public class RegCheck extends HttpServlet {
    @EJB
    UserMgrBean userManagement;

    @EJB
    JsonBookGenerator jsonBookGenerator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isCheck = request.getParameter("isCheck");
        String username = request.getParameter("username");
        PrintWriter out = response.getWriter();

        if(username == null){
            return ;
        }

        if(isCheck !=  null) {
            if (isCheck.equals("true")) {
                checkValidName(username, out);
                return;
            }
        }
        //Get user information
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        //Create new User
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPasswd(password);
        newUser.setEmail(email);
        newUser.setPermission(0);
        Date nowday = new Date();
        java.sql.Date day = new java.sql.Date(nowday.getTime());
        newUser.setRegDate(day);


        JsonObject obj = userManagement.addUserToDatabase(newUser);
        out.write(obj.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doPost(request, response);
    }

    private void checkValidName(String username, PrintWriter out){
        //Check Username
        UserEntity userEntity = userManagement.getUserFromDatabase("username", username);
        if(userEntity != null)
        {
            System.out.println("User Exists");
            out.write(jsonBookGenerator.getErrorMessage("User Exists").toString());
        }
        else{
            System.out.println("OK");
            out.write(jsonBookGenerator.getOkMessage().toString());
        }
    }
}
