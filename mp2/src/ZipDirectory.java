import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ZipDirectory {
    private final String name;
    private final Map<String, DeliveryZone> zonesByCode = new LinkedHashMap<>();

    public ZipDirectory(String name) {
        this.name = Objects.requireNonNull(name, "name cant be null");
    }

    public void registerZone(String postalCode, DeliveryZone zone) {
        Objects.requireNonNull(postalCode, "postalCode cant be null");
        Objects.requireNonNull(zone, "zone cant be null");

        DeliveryZone existingZone = zonesByCode.get(postalCode);
        if (existingZone == zone) {
            if (zone.getDirectory() != this || !postalCode.equals(zone.getPostalCode())) {
                zone.attachToDirectory(this, postalCode);
            }
            return;
        }

        if (existingZone != null) {
            throw new IllegalArgumentException("Postal code is already assigned: " + postalCode);
        }

        if (zone.getDirectory() != null && zone.getDirectory() != this) {
            throw new IllegalStateException("Zone is already assigned to a directory");
        }

        if (zone.getDirectory() == this && !postalCode.equals(zone.getPostalCode())) {
            throw new IllegalStateException("Zone is already assigned with a different postal code");
        }

        zonesByCode.put(postalCode, zone);
        zone.attachToDirectory(this, postalCode);
    }

    public DeliveryZone findZone(String postalCode) {
        return zonesByCode.get(postalCode);
    }

    public void removeZone(String postalCode) {
        Objects.requireNonNull(postalCode, "postalCode cant be null");

        DeliveryZone removedZone = zonesByCode.remove(postalCode);

        if (removedZone != null) {
            removedZone.detachFromDirectory();
        }
    }

    public void changePostalCode(DeliveryZone zone, String newPostalCode) {
        Objects.requireNonNull(zone, "zone cant be null");
        Objects.requireNonNull(newPostalCode, "newPostalCode cant be null");

        String oldPostalCode = zone.getPostalCode();

        if (oldPostalCode == null || zonesByCode.get(oldPostalCode) != zone) {
            throw new IllegalStateException("Zone is not registered in this directory");
        }

        if (oldPostalCode.equals(newPostalCode)) {
            return;
        }

        if (zonesByCode.containsKey(newPostalCode)) {
            throw new IllegalArgumentException("Postal code is already assigned: " + newPostalCode);
        }

        zonesByCode.remove(oldPostalCode);
        zonesByCode.put(newPostalCode, zone);
        zone.changePostalCode(newPostalCode);
    }

    public String getName() {
        return name;
    }

    public Map<String, DeliveryZone> getZonesByCode() {
        return Collections.unmodifiableMap(zonesByCode);
    }
}
