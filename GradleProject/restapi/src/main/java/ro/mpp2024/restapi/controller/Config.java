package ro.mpp2024.restapi.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.mpp2024.repository.IParticipantRepository;
import ro.mpp2024.repository.ParticipantHibernateRepository;

@Configuration
public class Config {

    @Bean
    public IParticipantRepository participantRepository() {
        return new ParticipantHibernateRepository();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*");
            }
        };
    }
}
