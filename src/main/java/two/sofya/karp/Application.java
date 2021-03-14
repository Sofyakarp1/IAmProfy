package two.sofya.karp;


import io.vertx.core.Vertx;
import two.sofya.karp.client.ClientVerticle;


public class Application {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
    vertx.deployVerticle(new ClientVerticle());
  }
}



