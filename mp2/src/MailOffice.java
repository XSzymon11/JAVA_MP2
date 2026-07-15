import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class MailOffice {
    private final String officeName;
    private final Set<Clerk> clerks = new LinkedHashSet<>();
    private boolean active = true;

    public MailOffice(String officeName) {
        this.officeName = Objects.requireNonNull(officeName, "officeName cant be null");
    }

    public Clerk hireClerk(String clerkName) {
        ensureActive();

        Clerk clerk = new Clerk(this, clerkName);
        clerks.add(clerk);

        return clerk;
    }

    public void fireClerk(Clerk clerk) {
        ensureActive();
        Objects.requireNonNull(clerk, "clerk cant be null");

        if (clerks.remove(clerk)) {
            clerk.deactivateBy(this);
        }
    }

    public void closeOffice() {
        ensureActive();

        for (Clerk clerk : new LinkedHashSet<>(clerks)) {
            fireClerk(clerk);
        }

        active = false;
    }

    private void ensureActive() {
        if (!active) {
            throw new IllegalStateException("Office is closed");
        }
    }

    public String getOfficeName() {
        return officeName;
    }

    public Set<Clerk> getClerks() {
        return Collections.unmodifiableSet(clerks);
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("Office: " + officeName + "\n");

        for (Clerk clerk : clerks) {
            info.append("  Clerk: ").append(clerk.getName()).append("\n");
        }

        return info.toString();
    }
}
