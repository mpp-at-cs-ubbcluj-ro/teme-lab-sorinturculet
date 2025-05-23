package ro.mpp2024.networking;

import ro.mpp2024.models.Participant;
import ro.mpp2024.models.ParticipantEventPoints;
import ro.mpp2024.models.User;

import java.util.List;

public class Response {
    private User user;
    private ResponseType type;
    private String errorMessage;

    // Payload options (use depending on response type)
    private List<Participant> participants;
    private List<ParticipantEventPoints> eventResults;

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<ParticipantEventPoints> getEventResults() {
        return eventResults;
    }

    public void setEventResults(List<ParticipantEventPoints> eventResults) {
        this.eventResults = eventResults;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", participants=" + (participants != null ? participants.size() + " participants" : "none") +
                ", eventResults=" + (eventResults != null ? eventResults.size() + " results" : "none") +
                '}';
    }
}
