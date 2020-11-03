package SDM.Exception;

import SDM.Store;

public class DiscountWithItemNotSoldByStoreException extends Exception {

    private int idOfItemInDiscount;
    private Store storeWithDiscount;

    public DiscountWithItemNotSoldByStoreException(int idOfItemInDiscount, Store storeWithDiscount) {
        this.idOfItemInDiscount = idOfItemInDiscount;
        this.storeWithDiscount = storeWithDiscount;
    }

    public int getIdOfItemInDiscount() {
        return idOfItemInDiscount;
    }

    public Store getStoreWithDiscount() {
        return storeWithDiscount;
    }
}
