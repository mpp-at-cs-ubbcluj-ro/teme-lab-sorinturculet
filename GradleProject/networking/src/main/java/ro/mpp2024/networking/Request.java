package ro.mpp2024.networking;

import ro.mpp2024.models.Participant;
import ro.mpp2024.models.Result;
import ro.mpp2024.models.User;

public class Request {
    private RequestType type;

    private User user;              // for LOGIN
    private Participant participant; // for CREATE_PARTICIPANT
    private Result result;          // for ADD_RESULT
    private String eventName;       // for GET_RESULTS

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", user=" + user +
                ", participant=" + participant +
                ", result=" + result +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
