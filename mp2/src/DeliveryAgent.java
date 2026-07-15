import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DeliveryAgent {
    private final String agentId;
    private final Set<DeliveryRecord> records = new LinkedHashSet<>();

    public DeliveryAgent(String agentId) {
        this.agentId = Objects.requireNonNull(agentId, "agentId cant be null");
    }

    public DeliveryRecord deliver(DeliveryCustomer customer, Date when, String tracking) {
        return new DeliveryRecord(this, customer, when, tracking);
    }

    public void cancelDelivery(DeliveryRecord record) {
        if (record == null || !records.contains(record)) {
            return;
        }

        removeRecord(record);
    }

    public void addRecord(DeliveryRecord record) {
        Objects.requireNonNull(record, "record cant be null");

        if (records.contains(record)) {
            return;
        }

        if (record.getAgent() != this) {
            throw new IllegalArgumentException("Record belongs to a different agent");
        }

        DeliveryCustomer customer = Objects.requireNonNull(
                record.getCustomer(),
                "record customer cant be null"
        );

        if (hasRecordForCustomer(customer)) {
            throw new IllegalStateException("Delivery record already exists for this agent and customer");
        }

        records.add(record);
        customer.addDelivery(record);
    }

    public void removeRecord(DeliveryRecord record) {
        Objects.requireNonNull(record, "record cant be null");

        if (!records.contains(record)) {
            return;
        }

        records.remove(record);
        record.remove();
    }

    public boolean hasRecordForCustomer(DeliveryCustomer customer) {
        Objects.requireNonNull(customer, "customer cant be null");

        for (DeliveryRecord record : records) {
            if (record.getCustomer() == customer) {
                return true;
            }
        }
        return false;
    }

    public String getAgentId() {
        return agentId;
    }

    public Set<DeliveryRecord> getRecords() {
        return Collections.unmodifiableSet(records);
    }
}
