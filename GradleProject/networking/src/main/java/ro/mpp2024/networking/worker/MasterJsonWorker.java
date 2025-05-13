package ro.mpp2024.networking.worker;

import ro.mpp2024.models.Participant;
import ro.mpp2024.models.ParticipantEventPoints;
import ro.mpp2024.models.Result;
import ro.mpp2024.models.User;
import ro.mpp2024.networking.Request;
import ro.mpp2024.networking.Response;
import ro.mpp2024.networking.ResponseType;
import ro.mpp2024.service.ITriathlonService;
import ro.mpp2024.service.IUserObserver;

import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class MasterJsonWorker extends AbstractJsonWorker implements IUserObserver {

    private final ITriathlonService service;
    private String currentUsername = null;

    public MasterJsonWorker(ITriathlonService service, Socket connection) {
        super(connection);
        this.service = service;
    }

    @Override
    protected Response handleRequest(Request request) {
        Response response = new Response();
        try {
            switch (request.getType()) {
                case LOGIN -> {
                    User user = request.getUser();
                    Optional<User> authenticated = service.authenticate(user.getName(), user.getPassword());
                    if (authenticated.isPresent()) {
                        currentUsername = user.getName();
                        service.registerObserver(this, currentUsername);
                        response.setType(ResponseType.OK);
                        response.setUser(authenticated.get());
                    } else {
                        response.setType(ResponseType.ERROR);
                        response.setErrorMessage("Invalid login");
                    }
                }

                case GET_PARTICIPANTS -> {
                    List<Participant> all = service.getAllParticipantsWithTotalPoints();
                    response.setType(ResponseType.PARTICIPANTS_LIST);
                    response.setParticipants(all);
                }

                case ADD_RESULT -> {
                    Result result = request.getResult();
                    service.addResult(result);
                    response.setType(ResponseType.OK);
                }

                case CREATE_PARTICIPANT -> {
                    Participant p = request.getParticipant();
                    service.addParticipant(p.getName());
                    response.setType(ResponseType.OK);
                }

                case GET_RESULTS -> {
                    String eventName = request.getEventName();
                    List<ParticipantEventPoints> results = service.getAggregatedResultsByEvent(eventName);
                    response.setType(ResponseType.EVENT_RESULTS);
                    response.setEventResults(results);
                }

                case LOGOUT -> {
                    if (currentUsername != null) {
                        service.removeObserver(currentUsername);
                        currentUsername = null;
                    }
                    connected = false;
                    response.setType(ResponseType.OK);
                }

                default -> {
                    response.setType(ResponseType.ERROR);
                    response.setErrorMessage("Unknown request type.");
                }
            }
        } catch (Exception e) {
            response.setType(ResponseType.ERROR);
            response.setErrorMessage("Server error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    // Called by the server to push updates to this client
    @Override
    public void notifyDataChanged() {
        Response update = new Response();
        update.setType(ResponseType.DATA_UPDATED);
        sendResponse(update);
    }
}
