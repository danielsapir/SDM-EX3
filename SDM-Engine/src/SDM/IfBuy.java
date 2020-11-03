package SDM;

import javax.xml.bind.annotation.XmlAttribute;

public class IfBuy {

    private StoreItem storeItem;
    private double quantity;


    public IfBuy(StoreItem storeItem, double quantity) {
        this.storeItem=storeItem;
        this.quantity=quantity;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public double getQuantity() {
        return quantity;
    }
}
