import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DeliveryCustomer {
    private final String customerId;
    private final Set<DeliveryRecord> deliveries = new LinkedHashSet<>();

    public DeliveryCustomer(String customerId) {
        this.customerId = Objects.requireNonNull(customerId, "customerId cant be null");
    }

    public void cancelDelivery(DeliveryRecord record) {
        if (record == null || !deliveries.contains(record)) {
            return;
        }

        removeDelivery(record);
    }

    public void addDelivery(DeliveryRecord record) {
        Objects.requireNonNull(record, "record cant be null");

        if (deliveries.contains(record)) {
            return;
        }

        if (record.getCustomer() != this) {
            throw new IllegalArgumentException("Record belongs to a different customer");
        }

        DeliveryAgent agent = Objects.requireNonNull(
                record.getAgent(),
                "record agent cant be null"
        );

        for (DeliveryRecord delivery : deliveries) {
            if (delivery.getAgent() == agent) {
                throw new IllegalStateException("Delivery record already exists for this agent and customer");
            }
        }

        deliveries.add(record);
        agent.addRecord(record);
    }

    public void removeDelivery(DeliveryRecord record) {
        Objects.requireNonNull(record, "record cant be null");

        if (!deliveries.contains(record)) {
            return;
        }

        deliveries.remove(record);
        record.remove();
    }

    public String getCustomerId() {
        return customerId;
    }

    public Set<DeliveryRecord> getDeliveries() {
        return Collections.unmodifiableSet(deliveries);
    }
}
