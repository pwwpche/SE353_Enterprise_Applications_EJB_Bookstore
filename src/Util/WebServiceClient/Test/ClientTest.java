package Util.WebServiceClient.Test;

import Util.WebServiceClient.BookServiceImpl;
import Util.WebServiceClient.BookServiceImpl_Service;

/**
 * Created by pwwpche on 2015/5/16.
 */
public class ClientTest {
    public static void main(String[] args){
        BookServiceImpl_Service service=new BookServiceImpl_Service();
        BookServiceImpl bookService= service.getBookServiceImpl();
        System.out.println(bookService.getBookDetailJson(1));
    }
}
