package two.sofya.karp.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.RequestOptions;
import two.sofya.karp.model.User;


public class ClientVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    HttpClientOptions options = new HttpClientOptions();

    HttpClient client = vertx.createHttpClient(options);

     client.post(8443, "localhost", "/notes", requestPost ->{
       System.out.println("Post request finished with code: " + requestPost.statusCode());
     }).putHeader("content-type", "text/plain").end(createUser());


     client.delete(new RequestOptions().
       setHost("localhost")
       .setPort(8443)
       .setURI("/notes/2")
       , response -> {
         System.out.println("Delete request finished with code: " + response.statusCode());
       });


    client.getNow(new RequestOptions()
        .setHost("localhost")
        .setPort(8443)
        .setURI("/notes")
      , response -> {
        System.out.println("Get request finished with code: " + response.statusCode());
      });

    client.getNow(new RequestOptions()
        .setHost("localhost")
        .setPort(8443)
        .setURI("/notes/1")
      , response -> {
        System.out.println("Get with param id request finished with code: " + response.statusCode());
      });

    client.getNow(new RequestOptions()
        .setHost("localhost")
        .setPort(8443)
        .setURI("/notes/title/:Maria")
      , response -> {
        System.out.println("Get with param title request finished with code: " + response.statusCode());
      });



  }

  public String createUser(){
    String jsonString = "{\"id\":4,\"title\":\"Sofya\",\"context\":\"fghj\"}";
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();

    Gson gson = builder.create();
    User user = gson.fromJson(jsonString, User.class);

    jsonString = gson.toJson(user);
    return jsonString;
  }


}
