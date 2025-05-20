package ro.mpp2024.restapi.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.mpp2024.repository.IParticipantRepository;
import ro.mpp2024.repository.ParticipantHibernateRepository;

@Configuration
public class Config {
    @Bean
    IParticipantRepository participantRepository() {
        return new ParticipantHibernateRepository();
    }
}
