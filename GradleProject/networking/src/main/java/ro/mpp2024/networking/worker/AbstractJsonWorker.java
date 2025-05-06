package ro.mpp2024.networking.worker;

import com.google.gson.Gson;
import ro.mpp2024.networking.Request;
import ro.mpp2024.networking.Response;

import java.io.IOException;
import java.net.Socket;

public abstract class AbstractJsonWorker extends AbstractWorker {

    private final Gson gsonFormatter;

    public AbstractJsonWorker(Socket connection) {
        super(connection);
        gsonFormatter = new Gson();
    }

    @Override
    protected Request readRequest() throws IOException {
        String requestLine = input.readLine();
        System.out.println("Request received: " + requestLine);
        return gsonFormatter.fromJson(requestLine, Request.class);
    }

    @Override
    protected void sendResponse(Response response) {
        String responseLine = gsonFormatter.toJson(response);
        System.out.println("Sending response: " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}
