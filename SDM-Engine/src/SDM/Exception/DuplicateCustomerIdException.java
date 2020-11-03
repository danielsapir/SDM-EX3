package SDM.Exception;

public class DuplicateCustomerIdException extends Exception {

    private final int id;

    public DuplicateCustomerIdException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
