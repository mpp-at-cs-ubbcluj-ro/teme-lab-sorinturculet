package ro.mpp2024.restapi.client;

import ro.mpp2024.models.Participant;

public class Main {

    private static final ParticipantClient client = new ParticipantClient();

    public static void main(String[] args) {
        Participant p = new Participant("Smecherul5000");
        try {
            System.out.println("Creating participant: " + p.getName());
            Participant created = client.create(p);
            p.setId(created.getId());
            System.out.println("Created participant: " + p.getName() + " (id: " + p.getId() + ")");

            System.out.println("\nAll participants:");
            show(() -> {
                for (Participant participant : client.getAll())
                    System.out.println(participant.getName() + " (id: " + participant.getId() + ")");
            });

            System.out.println("\nGet participant by id:");
            show(() -> System.out.println(client.getById(p.getId())));

            System.out.println("\nUpdating participant name...");
            p.setName("Updated Dorel");
            show(() -> client.update(p));
            show(() -> System.out.println(client.getById(p.getId())));

            System.out.println("\nDeleting participant with id: " + p.getId());
            show(() -> client.delete(p.getId()));

            System.out.println("\nAll participants after deletion:");
            show(() -> {
                for (Participant participant : client.getAll())
                    System.out.println(participant.getName() + " (id: " + participant.getId() + ")");
            });

        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}
