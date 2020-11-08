package SDM.DTO;


import SDM.Location;
import SDM.Order;

import java.util.Date;


public class OrderDTO
{
    private int id;
    private Date date;
    private Location orderDestination;
    private int numOfStoresInThisOrder;
    private double numOfItemsInThisOrder;
    private double priceOfAllItemsInThisOrder;
    private double priceOfAllDeliveriesInThisOrder;
    private double totalPrice;

    public void OrderDTO(Order order)
    {
        this.id=order.getId();
        this.date=order.getDate();
        this.orderDestination=order.getCustomer().getLocation();
        this.numOfStoresInThisOrder=order.getListOfOneStoreOrders().size();
        this.numOfItemsInThisOrder=order.getTotalItemsInOrder();
        this.priceOfAllItemsInThisOrder=order.getPriceOfAllItems();
        this.priceOfAllDeliveriesInThisOrder=order.getDeliveryPrice();
        this.totalPrice=order.getTotalPrice();
    }
}
