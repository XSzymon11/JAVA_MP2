import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class MailAddresses {
    private final String name;
    private final Set<MailOriginator> originators = new LinkedHashSet<>();

    public MailAddresses(String name) {
        this.name = Objects.requireNonNull(name, "name cant be null");
    }

    public void addOriginator(MailOriginator originator) {
        Objects.requireNonNull(originator, "originator cant be null");

        if (originators.contains(originator)) {
            return;
        }

        originators.add(originator);
        originator.sendTo(this);
    }

    public void removeOriginator(MailOriginator originator) {
        Objects.requireNonNull(originator, "originator cant be null");

        if (!originators.contains(originator)) {
            return;
        }

        originators.remove(originator);
        originator.stopSendingTo(this);
    }

    public String getName() {
        return name;
    }

    public Set<MailOriginator> getOriginators() {
        return Collections.unmodifiableSet(originators);
    }
}
