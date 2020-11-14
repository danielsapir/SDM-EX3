package SDM.DTO;

import SDM.Location;
import SDM.OneStoreOrder;
import SDM.OrderItem;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OneStoreOrderDTO
{
 private int id;
 private int storeId;
 private Date date;
 private String customerName;
 private Location destinationLocation;
 private double numOfItemsInThisOrder;
 private double priceOfItemsInThisOrder;
 private double deliveryPrice;

 private String storeName;
 private double distanceToStore;
 private double ppk;
 private int numTypesOfItemsInThisOrder;
 private Location storeLocation;

 //noy 12/11
 private List<OrderItemDTO> itemsInOrder=new LinkedList<>();
 private double totalPrice;

    public OneStoreOrderDTO(OneStoreOrder oneStoreOrder)
    {
        this.id=oneStoreOrder.getId();
        this.storeId=oneStoreOrder.getStoreOrderMadeFrom().getId();
        this.date=oneStoreOrder.getDate();
        this.customerName= oneStoreOrder.getCustomer().getName();
        this.destinationLocation = oneStoreOrder.getDestinationLocation();
        this.numOfItemsInThisOrder=oneStoreOrder.getTotalItemsInOrder();
        this.priceOfItemsInThisOrder=oneStoreOrder.getPriceOfAllItems();
        this.deliveryPrice=oneStoreOrder.getDeliveryPrice();

        this.storeName=oneStoreOrder.getStoreOrderMadeFrom().getName();
        this.distanceToStore=oneStoreOrder.distanceBetweenCostumerAndStore();
        this.ppk=oneStoreOrder.getStoreOrderMadeFrom().getDeliveryPPK();
        this.numTypesOfItemsInThisOrder=oneStoreOrder.getNumOfTypesOfItemsInOrder();
        this.storeLocation=oneStoreOrder.getStoreOrderMadeFrom().getLocation();

        //noy 12/11
        this.totalPrice=oneStoreOrder.getTotalPrice();
        for (OrderItem orderItem :oneStoreOrder.getItemsBoughtWithDiscount().values())
        {
            this.itemsInOrder.add(new OrderItemDTO(orderItem));
        }

        for (OrderItem orderItem:oneStoreOrder.getOrderItemCart().values())
        {

            this.itemsInOrder.add(new OrderItemDTO(orderItem));
        }

    }

}
