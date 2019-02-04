package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MapperServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Main MapperServer");

       Server server = ServerBuilder.forPort(8989)
               .addService(new GreetServiceImpl())
               .build();

       server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

       server.awaitTermination();
    }
}
