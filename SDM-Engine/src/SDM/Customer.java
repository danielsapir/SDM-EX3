package SDM;

import java.util.HashMap;
import java.util.Map;

public class Customer extends User implements Locatable
{

    private Map<Integer, Order> historyOrders;
    private Location location;

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
}
