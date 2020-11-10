package SDM.DTO;

import SDM.Location;
import SDM.OneStoreOrder;
import SDM.OrderType;

import javax.management.ObjectName;
import java.util.Date;

public class OneStoreOrderDTO
{
    int id;
    Date date;
    String customerName;
    Location customerLocation;
    double numOfItemsInThisOrder;
    double priceOfItemsInThisOrder;
    double deliveryPrice;

    public OneStoreOrderDTO(OneStoreOrder oneStoreOrder)
    {
        this.id=oneStoreOrder.getId();
        this.date=oneStoreOrder.getDate();
        this.customerName= oneStoreOrder.getCustomer().getName();
        this.customerLocation= oneStoreOrder.getCustomer().getLocation();
        this.numOfItemsInThisOrder=oneStoreOrder.getTotalItemsInOrder();
        this.priceOfItemsInThisOrder=oneStoreOrder.getPriceOfAllItems();
        this.deliveryPrice=oneStoreOrder.getDeliveryPrice();
    }

}
