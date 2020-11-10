package SDM;

import SDM.Exception.NegativeAmountOfItemInException;

import java.util.*;

public abstract class Order
{
    private static int idCounter = 1;
    protected final int id;
    protected final Map<Integer, OrderItem> orderItemCart;
    protected final Map<Integer, OrderItem> itemsBoughtWithDiscount;
    protected final LinkedList<Discount> discountsAvailable;
    protected final Customer customer;
    protected final Date date;
    protected double deliveryPrice;
    protected double priceOfAllItems;
    protected double totalPrice;
    protected Location destinationLocation;


    public Order(Customer customer, Date date, Location destinationLocation) {
        this.customer = customer;
        this.date = date;
        this.id = idCounter;
        this.destinationLocation = destinationLocation;
        orderItemCart = new HashMap<>();
        itemsBoughtWithDiscount = new HashMap<>();
        discountsAvailable = new LinkedList<>();
    }

    //Might be better in an order factory class
    public static Order makeNewOrder(Customer customer, Date date, Store storeOrderMadeFrom, OrderType orderType, Location destinationLocation) {
        if(orderType == OrderType.ONE_STORE_ORDER) {
            return new OneStoreOrder(customer, date, storeOrderMadeFrom, destinationLocation);
        }
        else if (orderType == OrderType.DYNAMIC_ORDER) {
            return new DynamicOrder(customer, date, destinationLocation);
        }
        else {
            return null;
        }
    }

    public Map<Integer, OrderItem> getItemsBoughtWithDiscount() {
        return itemsBoughtWithDiscount;
    }

    public int getId() {
        return id;
    }

    public Map<Integer, OrderItem> getOrderItemCart() {
        return orderItemCart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Date getDate() {
        return date;
    }

    public double getDeliveryPrice() {
        calculateDeliveryPrice();
        return deliveryPrice;
    }

    public double getPriceOfAllItems() {
        priceOfAllItems = calculatePriceOfOrderItems();
        return priceOfAllItems;
    }

    public double getTotalPrice() {
        totalPrice = getPriceOfAllItems() + getDeliveryPrice();
        return totalPrice;
    }

    public boolean isEmpty() {
        return orderItemCart.size() == 0;
    }

    public LinkedList<Discount> getDiscountsAvailable() {
        return discountsAvailable;
    }

    protected void incIdCounter() {
        idCounter++;
    }

    protected abstract void calculateDeliveryPrice();
    public abstract void addItemToOrder(Item itemToAdd, double amountToAdd) throws NegativeAmountOfItemInException;
    public abstract void completeOrder() throws NegativeAmountOfItemInException;
    protected abstract double calculatePriceOfOrderItems();
    public abstract int getTotalItemsInOrder();
    public abstract void continueToDiscounts() throws NegativeAmountOfItemInException;
    public abstract boolean useDiscount(Discount discountToUse, Offer offerChosen) throws NegativeAmountOfItemInException;
    public abstract List<OneStoreOrder> getListOfOneStoreOrders();
}
