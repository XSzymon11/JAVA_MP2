import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class MailOriginator {
    private final String name;
    private final Set<MailAddresses> addressees = new LinkedHashSet<>();

    public MailOriginator(String name) {
        this.name = Objects.requireNonNull(name, "name cant be null");
    }

    public void sendTo(MailAddresses address) {
        Objects.requireNonNull(address, "address cant be null");

        if (addressees.contains(address)) {
            return;
        }

        addressees.add(address);
        address.addOriginator(this);
    }

    public void stopSendingTo(MailAddresses address) {
        Objects.requireNonNull(address, "address cant be null");

        if (!addressees.contains(address)) {
            return;
        }

        addressees.remove(address);
        address.removeOriginator(this);
    }

    public String getName() {
        return name;
    }

    public Set<MailAddresses> getAddressees() {
        return Collections.unmodifiableSet(addressees);
    }
}
