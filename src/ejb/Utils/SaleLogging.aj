package ejb.Utils;

/**
 * Created by pwwpche on 2015/5/12.
 */
public aspect SaleLogging {
    pointcut logging(String username, double price):
            (execution ( String ejb.DAO.SalesMgrBean.dealSale(String, double)) && args(username, price));

    before(String username, double price): logging(username, price) {
        System.out.println("Creating Order...");
        System.out.println("Username: " + username);
        System.out.println("Price: " + price);
    }

    after(String username, double price) returning : logging(username, price){
        System.out.println("Order Saved");
    }

}