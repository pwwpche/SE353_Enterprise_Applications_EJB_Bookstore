package Servlets;

import Entity.UserEntity;
import ejb.DAO.UserMgrBean;
import javax.ejb.EJB;
import javax.json.*;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by pwwpche on 2014/5/3.
 *
 */

public class AdminUser extends HttpServlet {
    @EJB
    UserMgrBean userManagement;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String userinfo = queryAllUser().toString();
        String doType = request.getParameter("doType");
        System.out.println("doType = " + doType);

        JsonObject obj = Json.createObjectBuilder().build();
        if(doType != null) {
            if (doType.equals("new")) {
                obj = createUser(request);
            }
            if (doType.equals("edit"))
            {
                obj = editUser(request);
            }
            if (doType.equals("remove"))
            {
                obj = removeUser(request);
            }
            out.write(obj.toString());
            return ;
        }
        out.write(userinfo);
        out.close();
    }

    public JsonArray queryAllUser(){
        return userManagement.getAllUsers();
    }

    private JsonObject createUser(HttpServletRequest request){
        //String str = request.getParameter("data");
        //UserEntity user = getUserEntityFromJsonStr(str);
        UserEntity user = new UserEntity();
        user.setUsername(request.getParameter("username").toString());
        user.setPasswd(request.getParameter("password").toString());
        user.setEmail(request.getParameter("email").toString());
        user.setPermission(Integer.valueOf(request.getParameter("permission")));
        java.util.Date nowday = new java.util.Date();
        java.sql.Date day = new java.sql.Date(nowday.getTime());
        user.setRegDate(day);

        JsonObject obj = userManagement.addUserToDatabase(user);
        System.out.println(obj.toString());
        return obj;
    }

    private JsonObject editUser(HttpServletRequest request)
    {
        //TODO:Change this to JSON
        //String str = request.getParameter("data");
        //UserEntity user = getUserEntityFromJsonStr(str);
        UserEntity user = new UserEntity();
        user.setUsername(request.getParameter("username").toString());
        user.setPasswd(request.getParameter("password").toString());
        user.setEmail(request.getParameter("email").toString());
        user.setPermission(Integer.valueOf(request.getParameter("permission")));
        java.util.Date nowday = new java.util.Date();
        java.sql.Date day = new java.sql.Date(nowday.getTime());
        user.setRegDate(day);

        JsonObject obj = userManagement.modifyUserToDatabase(user);
        System.out.println(obj.toString());
        return obj;
    }

    private JsonObject removeUser(HttpServletRequest request){
        String data = request.getParameter("data");
        String username = null;
        HashMap<String, String> params = null;
        if(data != null){
            params = parseJsonStr(data);
            username = params.get("username");
        }
        JsonObject obj = Json.createObjectBuilder().build();
        if(username != null) {
            obj = userManagement.removeUserFromDatabase(username);
        }
        return obj;
    }

    private UserEntity getUserEntityFromJsonStr(String data){
        UserEntity user = null;
        HashMap<String, String> dataMap = parseJsonStr(data);
        if(dataMap.containsKey("username") &&
                dataMap.containsKey("password") &&
                dataMap.containsKey("permission") &&
                dataMap.containsKey("regDate") &&
                dataMap.containsKey("email")){
            user = new UserEntity();
            user.setUsername(dataMap.get("username"));
            user.setPasswd(dataMap.get("password"));
            user.setEmail(dataMap.get("email"));
            user.setPermission(Integer.valueOf(dataMap.get("permission")));
            java.util.Date nowday = new java.util.Date();
            java.sql.Date day = new java.sql.Date(nowday.getTime());
            user.setRegDate(day);
        }
        return user;
    }

    public HashMap<String, String> parseJsonStr(String str){
        HashMap<String, String> messageMap = new HashMap<String, String>();
        JsonParser parser = Json.createParser(new StringReader(str));
        while (parser.hasNext()) {
            if (parser.next() == JsonParser.Event.KEY_NAME) {
                String key = parser.getString();
                parser.next();
                String value = parser.getString();
                messageMap.put(key, value);
            }
        }
        return messageMap;
    }
}
