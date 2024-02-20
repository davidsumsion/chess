package handlers;

import com.google.gson.Gson;
import results.MessageOnlyResult;
import spark.Request;
import spark.Response;
import services.DeleteAllService;

public class DeleteAllHandler {

    public Object handle(Request request, Response response){
//        Gson gson = new Gson();
        DeleteAllService service = new DeleteAllService();
        service.DeleteAll();
        response.status(200);
        MessageOnlyResult mess = new MessageOnlyResult();
        mess.setMessage("");
        return mess;
    }
}
