package ro.mpp2024.networking.proxy;

import ro.mpp2024.models.*;
import ro.mpp2024.networking.*;
import ro.mpp2024.service.ITriathlonService;
import ro.mpp2024.service.IUserObserver;

import java.util.List;
import java.util.Optional;

public class MasterJsonProxy extends AbstractJsonProxy implements ITriathlonService {

    private IUserObserver observer;

    public MasterJsonProxy(String host, int port) {
        super(host, port);
    }

    @Override
    public Optional<User> authenticate(String username, String password) throws Exception {
        initializeConnection();
        Request req = new Request();
        req.setUser(new User(username, password));
        req.setType(RequestType.LOGIN);
        sendRequest(req);
        Response response = getResponse();
        if (response.getType() == ResponseType.OK) {
            return Optional.ofNullable(response.getUser());
        }
        if (response.getType() == ResponseType.ERROR) {
            throw new Exception(response.getErrorMessage());
        }
        return Optional.empty();
    }

    @Override
    public void logout() throws Exception {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        sendRequest(req);
        closeConnection();
        observer = null;
    }

    @Override
    public void registerObserver(IUserObserver observer, String username) {
        this.observer = observer;
    }

    @Override
    public void removeObserver(String username) {
        this.observer = null;
    }

    @Override
    public Participant addParticipant(String name) throws Exception {
        Request req = new Request();
        req.setParticipant(new Participant(name));
        req.setType(RequestType.CREATE_PARTICIPANT);
        sendRequest(req);
        Response response = getResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new Exception(response.getErrorMessage());
        return req.getParticipant();
    }

    @Override
    public void addResult(Result result) throws Exception {
        Request req = new Request();
        req.setResult(result);
        req.setType(RequestType.ADD_RESULT);
        sendRequest(req);
        Response response = getResponse();
        if (response.getType() == ResponseType.ERROR)
            throw new Exception(response.getErrorMessage());
    }

    @Override
    public List<Participant> getAllParticipantsWithTotalPoints() throws Exception {
        Request req = new Request();
        req.setType(RequestType.GET_PARTICIPANTS);
        sendRequest(req);
        Response response = getResponse();
        if (response.getType() == ResponseType.PARTICIPANTS_LIST)
            return response.getParticipants();
        throw new Exception(response.getErrorMessage());
    }

    @Override
    public List<ParticipantEventPoints> getAggregatedResultsByEvent(String eventName) throws Exception {
        Request req = new Request();
        req.setEventName(eventName);
        req.setType(RequestType.GET_RESULTS);
        sendRequest(req);
        Response response = getResponse();
        if (response.getType() == ResponseType.EVENT_RESULTS)
            return response.getEventResults();
        throw new Exception(response.getErrorMessage());
    }

    @Override
    public List<Result> getResultsByEvent(String eventName) throws Exception {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    protected boolean isUpdate(Response response) {
        return response.getType() == ResponseType.DATA_UPDATED;
    }

    @Override
    protected void handleUpdate(Response response) {
        if (observer != null) {
            try {
                observer.notifyDataChanged();
            } catch (Exception e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
}
