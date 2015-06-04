package Servlets;


import Entity.BookEntity;
import ejb.DAO.BookMgrBean;
import ejb.Utils.Cart;
import ejb.DAO.SalesMgrBean;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Properties;

/**
 *
 * Created by pwwpche on 2014/5/4.
 */
public class AddCart extends HttpServlet {
    @EJB
    BookMgrBean bookManagement;

    @EJB
    SalesMgrBean salesMgrBean;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doProcess(request, response);
    }

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession httpSession = request.getSession();
        //String sessionId = request.getSession().getId();
        String username = httpSession.getAttribute("username") == null ? "" : httpSession.getAttribute("username").toString();
        String action = request.getParameter("actionType");
        if(username == null  || action == null){
            return ;
        }

        //Get cart content by EJB
        try{
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            final Context context = new InitialContext(props);
            //Context context = new InitialContext();
            String ejbStr = "java:global/BookStore_war_exploded/CartBean!ejb.Utils.Cart";
            Cart cart;
            if(httpSession.getAttribute("cartEJB") == null){
                cart = (Cart) context.lookup(ejbStr);
                httpSession.setAttribute("cartEJB",cart);
            }else{
                cart = (Cart)httpSession.getAttribute("cartEJB");
            }

            BookEntity bookEntity ;
            if(action.equals("add") || action.equals("remove")){
                //Get Book Entity
                String bidStr = request.getParameter("bid");
                if(bidStr != null) {
                    int bid = Integer.valueOf(bidStr);
                    bookEntity = bookManagement.getBookFromDatabase(bid);
                    double price = bookEntity.getPrice();
                    String bookName = bookEntity.getBookname();

                    //Add book to cart
                    if(action.equals("add")) {
                        cart.addBook(bid, bookName, price);
                    }else{
                        cart.removeBook(bid);
                    }
                }
                System.out.print(cart.toJSONStr());
                JsonObject succMsg = Json.createObjectBuilder().add("status", true).add("msg","success").build();
                out.write(succMsg.toString());
            }
            if(action.equals("getAll")){
                out.write(cart.toJSONStr());
                return ;
            }
            if(action.equals("saveOrder")){
                double totalPrice = cart.getTotalPrice();
                out.write(salesMgrBean.dealSale(username, totalPrice).toString());
                cart.clear();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            JsonObject errMsg = Json.createObjectBuilder().add("status", false).add("msg",e.getMessage()).build();
            out.write(errMsg.toString());
        }
        out.close();
    }
}
