package Util.WebService;

import javax.xml.ws.Endpoint;

/**
 * Created by pwwpche on 2015/5/16.
 */
public class WSPublisher {
    private static boolean published = false;

    public static void publish(){
        if(!published) {
            try {
                Object bookService = new BookServiceImpl();
                String address = "http://localhost:8083/WebService/BookServiceImpl";
                Endpoint.publish(address, bookService);
            }catch(Exception e){
                System.out.println("WebService already published");
            }
            System.out.println("WebService published");
            published = true;
        }
    }
}
