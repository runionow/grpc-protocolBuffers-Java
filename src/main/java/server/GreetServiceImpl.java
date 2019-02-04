package server;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.stream.Stream;


public class GreetServiceImpl extends GreetServiceImplBase {
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        // Read the response
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String result = "Hello " + firstName;

        // Upon recieving the new message
        System.out.println("New Message recieved");
        System.out.println(greeting.getFirstName());

        // Create a response
        GreetResponse response = GreetResponse.newBuilder().setResult(result).build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC Call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) throws InterruptedException {
        Greeting data = request.getGreeting();
        String firstName = data.getFirstName();

        for (int i = 0; i < 10 ; i++) {
            String result = i + ". Hello " + firstName;
            GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder().setResult(result).build();
            responseObserver.onNext(response);
//            Thread.sleep(1000L);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> streamObserverOfRequest = new StreamObserver<LongGreetRequest>() {

            String result = "";
            @Override
            public void onNext(LongGreetRequest value) {
                // When client sends a message
                // The paradigm is very much similar to the reactive programming
                result  += "Hello" + value.getGreeting().getFirstName()+ "!";
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext( LongGreetResponse.newBuilder().setResult("result").build());
                responseObserver.onCompleted();
            }
        };

        streamObserverOfRequest.onCompleted();

        return super.longGreet(responseObserver);
    }
}
