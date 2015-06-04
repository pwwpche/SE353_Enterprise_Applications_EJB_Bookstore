package Servlets;

import Entity.BookEntity;
import ejb.DAO.BookMgrBean;
import ejb.Utils.JsonBookGenerator;


import javax.ejb.EJB;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



/**
 *
 * Created by pwwpche on 2014/5/3.
 */
public class AdminBook extends HttpServlet {
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

        String doType = request.getParameter("doType");
        System.out.println("doType = " + doType);

        JsonObject obj = Json.createObjectBuilder().build();
        if(doType != null) {
            if (doType.equals("new")) {
                obj = createBook(request);
            }
            if (doType.equals("edit"))
            {
                obj = editBook(request);
            }
            if (doType.equals("remove"))
            {
                obj = removeBook(request);
            }
            out.write(obj.toString());
        }else {
            String bookinfo = bookManagement.queryAllBook().toString();
            out.write(bookinfo);
        }
        out.close();
    }




    private JsonObject createBook(HttpServletRequest request){
        BookEntity book = new BookEntity();
        String bookName = request.getParameter("bookname");
        String catagory = request.getParameter("catagory");
        String price = request.getParameter("price");
        if(bookName == null || catagory == null || price == null)
        {
            return  bookManagement.addBookToDatabase(book);
        }
        book.setBookname(bookName);
        book.setCatagory(catagory);
        book.setPrice(Double.valueOf(price));
        JsonObject obj = bookManagement.addBookToDatabase(book);

        System.out.println(obj.toString());
        return obj;
    }

    private JsonObject editBook(HttpServletRequest request)
    {
        BookEntity book = new BookEntity();
        //TODO: Modify these params to JSON version using jsonParseBean
        //Seems we have to modify foreground pages...
        String bid = request.getParameter("bid");
        String bookName = request.getParameter("bookname");
        String catagory = request.getParameter("catagory");
        String price = request.getParameter("price");
        if(bid == null || bookName == null || catagory == null || price == null)
        {
            return  jsonBookGenerator.getErrorMessage("Book Information Error");
        }
        book.setBid(Long.valueOf(bid));
        book.setBookname(bookName);
        book.setCatagory(catagory);
        book.setPrice(Double.valueOf(price));


        JsonObject obj = bookManagement.modifyBookToDatabase(book);
        System.out.println(obj.toString());
        return obj;
    }

    private JsonObject removeBook(HttpServletRequest request){
        String bid_str = request.getParameter("bid");
        JsonObject obj = Json.createObjectBuilder().build();
        if(bid_str != null) {
            int bid = Integer.valueOf(bid_str);
            obj = bookManagement.removeBookFromDatabase(bid);
        }
        return obj;
    }
}
