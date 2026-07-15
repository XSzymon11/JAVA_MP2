import java.util.Date;
import java.util.Objects;

public final class DeliveryRecord {
    private DeliveryAgent agent;
    private DeliveryCustomer customer;
    private final Date dateDelivered;
    private final String trackingNumber;

    public DeliveryRecord(
            DeliveryAgent agent,
            DeliveryCustomer customer,
            Date dateDelivered,
            String trackingNumber
    ) {
        DeliveryAgent checkedAgent = Objects.requireNonNull(agent, "agent cant be null");
        DeliveryCustomer checkedCustomer = Objects.requireNonNull(customer, "customer cant be null");
        Date checkedDate = Objects.requireNonNull(dateDelivered, "dateDelivered cant be null");
        String checkedTracking = Objects.requireNonNull(trackingNumber, "trackingNumber cant be null");

        if (checkedAgent.hasRecordForCustomer(checkedCustomer)) {
            throw new IllegalStateException("Delivery record already exists for this agent and customer");
        }

        this.agent = checkedAgent;
        this.customer = checkedCustomer;
        this.dateDelivered = new Date(checkedDate.getTime());
        this.trackingNumber = checkedTracking;

        this.agent.addRecord(this);
    }

    public void remove() {
        DeliveryAgent oldAgent = agent;
        DeliveryCustomer oldCustomer = customer;

        if (oldAgent == null && oldCustomer == null) {
            return;
        }

        agent = null;
        customer = null;

        if (oldAgent != null) {
            oldAgent.removeRecord(this);
        }

        if (oldCustomer != null) {
            oldCustomer.removeDelivery(this);
        }
    }

    public DeliveryAgent getAgent() {
        return agent;
    }

    public DeliveryCustomer getCustomer() {
        return customer;
    }

    public Date getDateDelivered() {
        return new Date(dateDelivered.getTime());
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public boolean isActive() {
        return agent != null && customer != null;
    }
}
