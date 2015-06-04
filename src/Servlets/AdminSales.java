package Servlets;


import ejb.DAO.SalesMgrBean;
import ejb.DAO.UserMgrBean;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Created by pwwpche on 2014/5/4.
 */
public class AdminSales extends HttpServlet {
    @EJB
    UserMgrBean userManagement;

    @EJB
    SalesMgrBean salesMgrBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRequest(request, response);
    }

    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String reqType = request.getParameter("type");
        String reqData = request.getParameter("dataString");
        String reqItem = request.getParameter("item");

        if(reqType == null || reqData == null)
            return ;
        JsonObject result = salesMgrBean.addSaleToDatabase(reqType, reqData, reqItem);
        out.write(result.toString());
    }


}
