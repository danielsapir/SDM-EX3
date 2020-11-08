package SDM.DTO;

import SDM.Item;
import SDM.Store;
import SDM.StoreItem;

public class StoreItemDTO
{

    //private String storeName;
    private int id;
    private String name;
    private Item.ItemType type;
    private double pricePerOne;
    private double totalAmountSoldInThisStore;

    public StoreItemDTO(StoreItem storeItem)
    {
        this.id=storeItem.getItem().getId();
        this.name=storeItem.getItem().getName();
        this.type=storeItem.getItem().getType();
        this.pricePerOne=storeItem.getPrice();
        this.totalAmountSoldInThisStore=storeItem.getTotalAmountSoldInThisStore();
    }
}
