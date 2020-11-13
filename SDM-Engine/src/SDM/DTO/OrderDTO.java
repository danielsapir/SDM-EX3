package SDM.DTO;


import SDM.Location;
import SDM.Order;
import SDM.OrderItem;

import java.lang.ref.PhantomReference;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
    private List<OrderItemDTO> itemsInThisOrder=new LinkedList<>();

    public OrderDTO(Order order)
    {
        this.id=order.getId();
        this.date=order.getDate();
        this.orderDestination=order.getDestinationLocation();
        this.numOfStoresInThisOrder=order.getListOfOneStoreOrders().size();
        this.numOfItemsInThisOrder=order.getTotalItemsInOrder();
        this.priceOfAllItemsInThisOrder=order.getPriceOfAllItems();
        this.priceOfAllDeliveriesInThisOrder=order.getDeliveryPrice();
        this.totalPrice=order.getTotalPrice();

        for (OrderItem orderItem:order.getOrderItemCart().values())
        {
            OrderItemDTO orderItemDTO=new OrderItemDTO(orderItem);
            itemsInThisOrder.add(orderItemDTO);
        }

        for (OrderItem orderItem:order.getItemsBoughtWithDiscount().values())
        {
            OrderItemDTO orderItemDTO=new OrderItemDTO(orderItem);
            itemsInThisOrder.add(orderItemDTO);
        }
    }
}
