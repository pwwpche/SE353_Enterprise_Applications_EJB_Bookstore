package ejb.Utils;

import Entity.BookEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by pwwpche on 2015/4/20.
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class JsonBookGenerator {
    public JsonObject getOkMessage(){
        JsonObjectBuilder build = Json.createObjectBuilder();
        build.add("status", "OK");
        build.add("message", "Done");
        build.add("msg", "Done");
        build.add("success", true);
        return build.build();
    }

    public JsonObject getErrorMessage(String errorMsg){
        JsonObjectBuilder build = Json.createObjectBuilder();
        build.add("status", "Error");
        build.add("message", errorMsg);
        build.add("msg", errorMsg);
        build.add("success", false);
        return build.build();
    }

    public JsonObject buildBookJson(BookEntity bookEntity){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        //TODO: Add author to list
        builder.add("bid", bookEntity.getBid())
                .add("bookname", bookEntity.getBookname())
                .add("catagory", bookEntity.getCatagory())
                .add("price", bookEntity.getPrice());
//                .add("author", bookEntity.getAuthor());
        return builder.build();
    }
}
