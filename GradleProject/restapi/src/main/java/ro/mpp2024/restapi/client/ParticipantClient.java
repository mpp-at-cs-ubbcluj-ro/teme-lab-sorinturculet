package ro.mpp2024.restapi.client;

import ro.mpp2024.models.Participant;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ParticipantClient {

    private final RestClient restClient = RestClient.builder()
            .requestInterceptor(new CustomRestClientInterceptor())
            .build();

    public static final String URL = "http://localhost:8080/api/participants";

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Participant[] getAll() {
        return execute(() -> restClient.get().uri(URL).retrieve().body(Participant[].class));
    }

    public Participant getById(Integer id) {
        return execute(() -> restClient.get().uri(String.format("%s/%d", URL, id)).retrieve().body(Participant.class));
    }

    public Participant create(Participant participant) {
        return execute(() -> restClient.post().uri(URL).contentType(APPLICATION_JSON).body(participant).retrieve().body(Participant.class));
    }

    public Participant update(Integer id,Participant participant) {
        return execute(() -> restClient.put().uri(String.format("%s/%d", URL, id)).contentType(APPLICATION_JSON).body(participant).retrieve().body(Participant.class));
    }

    public void delete(Integer id) {
        execute(() -> restClient.delete().uri(String.format("%s/%d", URL, id)).retrieve().toBodilessEntity());
    }

    public static class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending " + request.getMethod() + " to " + request.getURI() + " with body [" + new String(body) + "]");
            ClientHttpResponse response = execution.execute(request, body);
            System.out.println("Received response code: " + response.getStatusCode());
            return response;
        }
    }
}
