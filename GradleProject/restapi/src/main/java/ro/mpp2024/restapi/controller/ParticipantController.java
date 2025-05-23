package ro.mpp2024.restapi.controller;

import ro.mpp2024.models.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.repository.IParticipantRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import java.util.Collection;
import java.util.Optional;
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    IParticipantRepository participantRepository;
    @GetMapping
    public Collection<Participant> get(){
        return participantRepository.findAll();
    }
    @GetMapping(value="/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        Optional<Participant> participant = participantRepository.read(id);
        if(participant.isEmpty())
            return new ResponseEntity<>("Participant not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(participant.get(),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Participant participant){
       try {
           participant.setId(null);
           Participant created = participantRepository.create(participant);
           System.out.println("Sending refresh notification to /topic/participants");
           messagingTemplate.convertAndSend("/topic/participants", "refresh");
           return new ResponseEntity<>(created, HttpStatus.CREATED);
       }
       catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Participant participant){
        try {
            participant.setId(id);
            Participant updated = participantRepository.update(participant);
            System.out.println("Sending refresh notification to /topic/participants");
            messagingTemplate.convertAndSend("/topic/participants", "refresh");
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete(@PathVariable Integer id){
        participantRepository.delete(id);
        System.out.println("Sending refresh notification to /topic/participants");
        messagingTemplate.convertAndSend("/topic/participants", "refresh");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
