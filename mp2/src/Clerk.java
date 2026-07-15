import java.util.Objects;

public class Clerk {
    private final String name;
    private MailOffice office;
    private boolean active = true;

    Clerk(MailOffice office, String name) {
        this.office = Objects.requireNonNull(office, "office cant be null");
        this.name = Objects.requireNonNull(name, "name cant be null");
    }

    void deactivateBy(MailOffice office) {
        if (this.office != office) {
            throw new IllegalArgumentException("Clerk belongs to a different office");
        }

        this.office = null;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public MailOffice getOffice() {
        if (!active) {
            throw new IllegalStateException("Clerk is no longer active");
        }

        return office;
    }

    public boolean isActive() {
        return active;
    }
}
