package Util.RestWebService;

import org.glassfish.jersey.server.ResourceConfig;


public class BookResourceConfig extends ResourceConfig {
    public BookResourceConfig() {
        packages("Util.RestWebService");
    }
}