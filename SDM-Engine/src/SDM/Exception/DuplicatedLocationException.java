package SDM.Exception;

import SDM.Location;

public class DuplicatedLocationException extends Exception {
    private Location duplicateLocation;

    public DuplicatedLocationException(Location duplicateLocation) {
        this.duplicateLocation = duplicateLocation;
    }

    public Location getDuplicateLocation() {
        return duplicateLocation;
    }
}
