package SDM;

import SDM.Exception.NegativeAmountOfItemInException;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer extends User implements Locatable
{

    private Map<Integer, Order> historyOrders;
    private Location location;

    //noy 9/11
    private Order currentOrder;
    private Zone currentOrderZone;


    public Customer(String name)
    {
        super(name, Type.CUSTOMER);
        this.historyOrders = new HashMap<>();


    }






    public void addNewOrder(Order newOrder) {
        historyOrders.put(newOrder.getId(), newOrder);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public int getNumOfCostumerOrders() {
        return historyOrders.size();
    }

    public double getPriceOfCostumerOrders() {
        if(historyOrders.size() != 0) {
            return (historyOrders.values().stream().mapToDouble(Order::getPriceOfAllItems).sum()) / historyOrders.size();
        }else {
            return 0;
        }
    }

    public double getPriceOfCostumerOrdersDeliveries() {
        if(historyOrders.size() != 0) {
            return (historyOrders.values().stream().mapToDouble(Order::getDeliveryPrice).sum()) / historyOrders.size();
        }else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Id: %d      Name: %s",this.id, this.name);
    }


    ///&&&&&&&&&&&&&&&&&&&&&&&&&current Order&&&&&&&&&&&&&&&&&&&&&&&&&&

    public Order getCurrentOrder() {
        return currentOrder;
    }


    public void createNewDynamicOrder(Customer customerEX1, Date dateOrder,Zone zone) {

        currentOrderZone=zone;
    }

    public void createNewOneStoreOrder(Customer customerEX1, Date dateOrder, Store store,Zone zone) {
        currentOrder = Order.makeNewOrder(customerEX1, dateOrder, store, OrderType.ONE_STORE_ORDER);
        currentOrderZone=zone;
    }

    public void addItemToCurrentOrder(int choosedItemId, double choosedAmountOfItem) throws NegativeAmountOfItemInException
    {
        this.currentOrder.addItemToOrder(currentOrderZone.getAllItems().get(choosedItemId),choosedAmountOfItem);
    }


    public void continueCurrentOrderToDiscounts() throws NegativeAmountOfItemInException {
        currentOrder.continueToDiscounts();
    }

    public List<OneStoreOrder> getListOfOneStoreOrdersOfCurrentOrder() {
        return currentOrder.getListOfOneStoreOrders();
    }

    public List<Discount> getListOfDiscountsOfCurrentOrder() {
        return currentOrder.getDiscountsAvailable();
    }


    public boolean useDiscountOfCurrentOrder(Discount discountToUse) throws NegativeAmountOfItemInException {
        return currentOrder.useDiscount(discountToUse, null);
    }

    public boolean useDiscountOfCurrentOrder(Discount discountToUse, Offer offerChosen) throws NegativeAmountOfItemInException {
        return currentOrder.useDiscount(discountToUse, offerChosen);
    }

    public void completeCurrentOrder() throws NegativeAmountOfItemInException
    {
        currentOrder.completeOrder();

        //allOrders.add(currentOrder);שינוי נוי 9.11 מזה:
        historyOrders.put(currentOrder.id,currentOrder);
        currentOrderZone.addOrder(currentOrder);

        currentOrder = null;

    }

    public void cancelCurrentOrder() {
        currentOrder = null;
    }


    /*
    public boolean isAnyOrderMade() {
        return anyOrderMade.get();
    }

    public SimpleBooleanProperty anyOrderMadeProperty() {
        return anyOrderMade;
    }

     */









}
