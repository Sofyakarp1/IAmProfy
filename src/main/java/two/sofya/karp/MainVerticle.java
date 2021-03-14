package two.sofya.karp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.*;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import two.sofya.karp.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

private Map<Integer, User> map = new LinkedHashMap<>();
private AsyncFile file;

private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.fileSystem().open("log.txt", new OpenOptions(), result -> {
      if (result.succeeded()) {
        file = result.result();
        Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + "            Start working!\n\n");
        file.write(buff);
    createUsers();

        HttpServer server = vertx.createHttpServer(new HttpServerOptions());



    Router router = Router.router(vertx);

    router.route("/router").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.setChunked(true)
        .putHeader("content-type", "text/html")
        .end("<h1>Database of users</h1>");
    });

    router.route().handler(BodyHandler.create());

    //Заголовки
    router.get("/notes").handler(this::handleGetAllUsers);

    //Запрос с body
    router.post("/notes").handler(this::handleAddUser);

    // Path
    router.get("/notes/:id").handler(this::handleGetUserById);

    router.get("/notes/title/:title").handler(this::handleGetUserByTitle);

    router.delete("/notes/:id").handler(this::handleDeleteUser);


    server.requestHandler(router::accept);
    server.listen(8443);
    System.out.println("listening on: 8443");
      }
    });

  }

  private void createUsers(){
    User firstUser = new User(1, "Dmitry",  "ddd");
    map.put(firstUser.getId(), firstUser);
    User secondUser = new User(2, "Maria",  "kkk");
    map.put(secondUser.getId(), secondUser);
    User thirdUser = new User(3, "Sasha",  "ttt");
    map.put(thirdUser.getId(), thirdUser);

  }

  private void handleGetAllUsers(RoutingContext routingContext) {
    routingContext.response().
      putHeader("content-type", "application/json").
      end(Json.encodePrettily(map.values()));
    Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + " - Get all users\n");
    file.write(buff);
  }

  private void handleAddUser(RoutingContext routingContext){
    final User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
    HttpServerResponse response = routingContext.response();
      map.put(user.getId(), user);
      response.end();
    Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + " - Add user with id = " + user.getId() + "\n");
    file.write(buff);
  }

  private void handleDeleteUser(RoutingContext routingContext) {
    String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Integer isAsInteger = Integer.valueOf(id);
      map.remove(isAsInteger);
      routingContext.response().setStatusCode(204).end("Successfully deleted");
      Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + " - Delete user with id = " + id + "\n");
      file.write(buff);
    }
  }

  private void handleGetUserById(RoutingContext routingContext){
    String id = routingContext.request().getParam("id");
    Integer isAsInteger = Integer.valueOf(id);
    routingContext.response().
      putHeader("content-type", "application/json").
      end(Json.encodePrettily(map.get(isAsInteger)));
    Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + " - Get user by id =" + isAsInteger + "\n");
    file.write(buff);
  }

  private void handleGetUserByTitle(RoutingContext routingContext){
    String title = routingContext.request().getParam("title");

    routingContext.response().
      putHeader("content-type", "application/json").
      end(Json.encodePrettily(map.get(title)));
    Buffer buff = Buffer.buffer(LocalDateTime.now().format(formatter) + " - Get user by id =" + title + "\n");
    file.write(buff);
  }


}
