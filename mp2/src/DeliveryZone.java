import java.util.Objects;

public class DeliveryZone {
    private final String zoneName;
    private ZipDirectory directory;
    private String postalCode;

    public DeliveryZone(String zoneName) {
        this.zoneName = Objects.requireNonNull(zoneName, "zoneName cant be null");
    }

    public void attachToDirectory(ZipDirectory directory, String postalCode) {
        Objects.requireNonNull(directory, "directory cant be null");
        Objects.requireNonNull(postalCode, "postalCode cant be null");

        if (this.directory == directory && postalCode.equals(this.postalCode)) {
            return;
        }

        if (this.directory != null) {
            throw new IllegalStateException("Zone is already assigned to a directory");
        }

        DeliveryZone existingZone = directory.findZone(postalCode);
        if (existingZone != null && existingZone != this) {
            throw new IllegalArgumentException("Postal code is already assigned: " + postalCode);
        }

        this.directory = directory;
        this.postalCode = postalCode;
        directory.registerZone(postalCode, this);
    }

    public void detachFromDirectory() {
        if (directory != null) {
            ZipDirectory oldDirectory = directory;
            String oldPostalCode = postalCode;

            directory = null;
            postalCode = null;

            oldDirectory.removeZone(oldPostalCode);
        }
    }

    public void changePostalCode(String newPostalCode) {
        Objects.requireNonNull(newPostalCode, "newPostalCode cant be null");

        if (directory == null) {
            throw new IllegalStateException("Zone is not assigned to any directory");
        }

        if (newPostalCode.equals(postalCode)) {
            return;
        }

        DeliveryZone existingZone = directory.findZone(newPostalCode);
        if (existingZone != null && existingZone != this) {
            throw new IllegalArgumentException("Postal code is already assigned: " + newPostalCode);
        }

        if (existingZone == this) {
            postalCode = newPostalCode;
            return;
        }

        directory.changePostalCode(this, newPostalCode);
    }

    public String getZoneName() {
        return zoneName;
    }

    public ZipDirectory getDirectory() {
        return directory;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
