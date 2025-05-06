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
        System.out.println("[Proxy] Raw response received: " + responseLine);
        Response response = gsonFormatter.fromJson(responseLine, Response.class);
        System.out.println("[Proxy] Deserialized response object: " + response);
        if (response != null) {
            System.out.println("[Proxy] Deserialized user in response: " + response.getUser());
            if (response.getUser() != null) {
                System.out.println("[Proxy] Deserialized user name: " + response.getUser().getName());
            }
        }
        return response;
    }
}