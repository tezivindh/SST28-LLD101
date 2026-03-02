import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;
import java.util.List;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        IncidentTicket t = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + t);

        IncidentTicket assigned = service.assign(t, "agent@example.com");
        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\nAfter service updates (new instances): " + escalated);
        System.out.println("\nOriginal ticket unchanged: " + t);

        List<String> tags = t.getTags();
        try {
            tags.add("HACKED_FROM_OUTSIDE");
            System.out.println("FAIL: tags were mutated!");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nGood: tags list is unmodifiable. External mutation blocked.");
        }

        // Starter compiles; after refactor, you should redesign updates to create new
        // objects instead.
    }
}
