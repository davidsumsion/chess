package handlers;

import results.MessageOnlyResult;
import spark.Request;
import spark.Response;
import services.DeleteAllService;

public class DeleteAllHandler {

    public Object handle(Request request, Response response){
        DeleteAllService service = new DeleteAllService();
        service.deleteAll();
        response.status(200);
        MessageOnlyResult mess = new MessageOnlyResult();
        mess.setMessage("{}");
        return "{}";
    }
}
