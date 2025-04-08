package ro.mpp2024.networking.proxy;

import com.google.gson.Gson;
import ro.mpp2024.networking.Request;
import ro.mpp2024.networking.Response;

public abstract class AbstractJsonProxy extends AbstractProxy {
    private final Gson gsonFormatter;

    public AbstractJsonProxy(String host, int port) {
        super(host, port);
        gsonFormatter = new Gson();
    }

    protected void sendRequest(Request request) throws Exception {
        String reqLine = gsonFormatter.toJson(request);
        System.out.println("Request sent: " + reqLine);
        output.println(reqLine);
        output.flush();
    }

    @Override
    protected Response readResponse() throws Exception {
        String responseLine = input.readLine();
        System.out.println("Response received: " + responseLine);
        return gsonFormatter.fromJson(responseLine, Response.class);
    }
}