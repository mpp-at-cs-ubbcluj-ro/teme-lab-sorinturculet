package ro.mpp2024.networking.proxy;

import ro.mpp2024.networking.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractProxy {
    protected final String host;
    protected final int port;

    protected BufferedReader input;
    protected PrintWriter output;
    protected Socket connection;

    protected final BlockingQueue<Response> responses;
    protected volatile boolean finished;

    public AbstractProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.responses = new LinkedBlockingQueue<>();
    }

    protected void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Response getResponse() {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void initializeConnection() {
        if (connection != null) return;
        try {
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread readerThread = new Thread(new ReaderThread());
        readerThread.start();
    }

    protected abstract boolean isUpdate(Response response);

    protected abstract void handleUpdate(Response response);

    protected abstract Response readResponse() throws Exception;

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Response response = readResponse();
                    if (finished) break;
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        responses.put(response);
                    }
                } catch (Exception e) {
                    System.out.println("Reading error: " + e.getMessage());
                }
            }
        }
    }
}