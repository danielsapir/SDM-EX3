package SDM.DTO;

import SDM.*;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.util.LinkedList;
import java.util.List;

public class storeDTO
{
    private int id;
    private String name;
    private String ownerName;
    private Location location;
    private List<StoreItemDTO> itemsThatSellInThisStore=new LinkedList<>();
    private int numOfOrdersFromThisStore;
    private double totalCostOfSoldItems=0;
    private double ppk;
    private double totalCostOfDeliveries;
    //noy 12/11
    private List<FeedBack> storeFeedBack=new LinkedList<>();


    public storeDTO(Store store)
    {

        this.id=store.getId();
        this.name=store.getName();
        this.ownerName=store.getOwner().getName();
        this.location=store.getLocation();
        this.ppk=store.getDeliveryPPK();
        this.totalCostOfDeliveries=store.getTotalAmountForDeliveries();
        this.numOfOrdersFromThisStore=store.getOrders().size();

        for (StoreItem stItem:store.getItemsThatSellInThisStore().values())
        {
            StoreItemDTO stItemDTO=new StoreItemDTO(stItem);
            this.itemsThatSellInThisStore.add(stItemDTO);
        }

        for (Order order:store.getOrders())
        {
            this.totalCostOfSoldItems+=order.getPriceOfAllItems();

        }

        //noy 12/11
        for (OneStoreOrder orders:store.getOrders())
        {
            storeFeedBack.add(orders.getFeedBack());
        }
    }
}
