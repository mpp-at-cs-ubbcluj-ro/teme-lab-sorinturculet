package ro.mpp2024.networking.worker;

import ro.mpp2024.networking.Request;
import ro.mpp2024.networking.Response;
import ro.mpp2024.networking.ResponseType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public abstract class AbstractWorker implements Runnable {
    protected final Socket connection;

    protected BufferedReader input;
    protected PrintWriter output;
    protected volatile boolean connected;

    protected static Response okResponse;

    public AbstractWorker(Socket connection) {
        this.connection = connection;
        okResponse = new Response();
        okResponse.setType(ResponseType.OK);
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Request request = readRequest();
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (Exception e) {
                if (connection.isClosed())
                    connected = false;
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }


    protected abstract Request readRequest() throws IOException;

    protected abstract Response handleRequest(Request request);

    protected abstract void sendResponse(Response response);
}
