package SDM.DTO;

import SDM.Item;

public class ItemDTO
{


    private int id;
    private String name;
    private Item.ItemType type;
    private int numOfStoresSellThisItem;
    private double avgPrice;
    private double totalAmountSoldOnAllStores;

    public ItemDTO(Item item)
    {
        this.id=item.getId();
        this.name=item.getName();
        this.type=item.getType();
        this.numOfStoresSellThisItem=item.getStoresSellThisItem().size();
        this.avgPrice=item.getAveragePrice();
        this.totalAmountSoldOnAllStores=item.getTotalAmountSoldOnAllStores();
    }
}
