package Servlets;


import ejb.DAO.BookMgrBean;
import ejb.Utils.JsonBookGenerator;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
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
 * Created by pwwpche on 2015/4/20.
 *
 */
public class UserBook extends HttpServlet {
    @EJB
    BookMgrBean bookManagement;

    @EJB
    JsonBookGenerator jsonBookGenerator;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String params = request.getParameter("data");
        HashMap<String, String> paraMap = parseJsonStr(params);
        String doAction = paraMap.get("doType");
        System.out.println("action = " + doAction);
        if(doAction == null) {
            String bookinfo = bookManagement.queryAllBook().toString();
            out.write(bookinfo);
        }else{
            if(doAction.equals("BookDetail")){
                    String bid = paraMap.get("bid");
                if(bid == null){
                    out.write(jsonBookGenerator.getErrorMessage("Bid is missing").toString());
                }else {
                    Integer bookid = Integer.valueOf(bid);
                    JsonObject bookDetailInJson = bookManagement.getBookDetailInJson(bookid);
                    out.write(bookDetailInJson.toString());
                }
            }
        }
        out.close();
    }

    public HashMap<String, String> parseJsonStr(String str){
        HashMap<String, String> messageMap = new HashMap<>();
        if(str == null){
            return messageMap;
        }
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
