package ro.mpp2024.networking.server;

import ro.mpp2024.networking.worker.MasterJsonWorker;
import ro.mpp2024.service.ITriathlonService;

import java.net.Socket;

public class JsonConcurrentServer extends AbstractConcurrentServer {
    private final ITriathlonService service;

    public JsonConcurrentServer(int port, ITriathlonService service) {
        super(port);
        this.service = service;
        System.out.println("JsonConcurrentServer running...");
    }

    @Override
    protected Thread createWorker(Socket client) {
        MasterJsonWorker worker = new MasterJsonWorker(service, client);
        return new Thread(worker);
    }
}
