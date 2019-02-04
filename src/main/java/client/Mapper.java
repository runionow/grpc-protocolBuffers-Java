package client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class Mapper implements Runnable {

    public static void main(String[] args) {
        System.out.println("Hello I am a grpc client");

        Mapper main = new Mapper();
        main.run();
    }

    public void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",8989)
                .usePlaintext()
                .build();
//        doUnaryCall(channel);
        doServerStreamingCall(channel);
//        doClientStreamingCall(channel);
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        // Asynchronus Client
        GreetServiceGrpc.GreetServiceStub asyncStub = GreetServiceGrpc.newStub(channel);
        StreamObserver<LongGreetRequest> requestObserver =  asyncStub.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //Handle all the responses from the server here.
                System.out.println("New response has been received");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                // Handle upon completion
            }
        });

        // Streaming messages
        requestObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Varun")
                        .setLastName("Nekkalapudi")
                        .build())
                .build());

        requestObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Nirmala")
                        .setLastName("Nekkalapudi")
                        .build())
                .build());

        requestObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Gangadhara")
                        .setLastName("Nekkalapudi")
                        .build())
                .build());

        requestObserver.onCompleted();


    }

    private void doServerStreamingCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting
                .newBuilder()
                .setFirstName("Arun")
                .setLastName("Nekkalapudi").build();
        GreetManyTimesRequest request = GreetManyTimesRequest.newBuilder().setGreeting(greeting).build();

        greetClient.greetManyTimes(request).forEachRemaining(
                greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel) {
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting
                .newBuilder()
                .setFirstName("Arun")
                .setLastName("Nekkalapudi").build();

        GreetRequest request = GreetRequest
                .newBuilder()
                .setGreeting(greeting).build();

        GreetResponse greetResponse = greetClient.greet(request);
        System.out.println(greetResponse.getResult());
        channel.shutdown();
    }
}
