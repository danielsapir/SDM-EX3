package SDM;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Store implements Locatable
{


    private Owner owner;
    private int id;
    private String name;
    private int deliveryPPK;
    private Location location;
    private Map<Integer, StoreItem> itemsThatSellInThisStore=new HashMap<>();
    private List<OneStoreOrder> orders= new LinkedList<>();
    private List<Discount> discounts= new LinkedList<>();

    public Owner getOwner() {
        return owner;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getDeliveryPPK()
    {
        return deliveryPPK;
    }

    public void setDeliveryPPK(int deliveryPPK)
    {
        this.deliveryPPK = deliveryPPK;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public Map<Integer, StoreItem> getItemsThatSellInThisStore()
    {
        return itemsThatSellInThisStore;
    }

    public void setItemsThatSellInThisStore(Map<Integer, StoreItem> itemsThatSellInThisStore)
    {
        this.itemsThatSellInThisStore = itemsThatSellInThisStore;
    }

    public List<OneStoreOrder> getOrders()
    {
        return orders;
    }

    public void setOrders(List<OneStoreOrder> orders)
    {
        this.orders = orders;
    }

    public double getTotalAmountForDeliveries()
    {
        double retAmountOfDeliveries=0;
        for (Order order:this.getOrders())
        {
            retAmountOfDeliveries+=order.getDeliveryPrice();
        }
        return (retAmountOfDeliveries);
    }


    //NOY 26/9
    public void setDiscounts(List<Discount> discountsOfStore)
    {
        this.discounts=discountsOfStore;
    }

    public Integer getPriceOfItemInStore(Item itemToSearch) {
        Integer resultPrice = null;

        if(itemsThatSellInThisStore.containsKey(itemToSearch.getId())) {
            resultPrice = itemsThatSellInThisStore.get(itemToSearch.getId()).getPrice();
        }

        return resultPrice;
    }


    @Override
    public String toString() {
        return String.format("Id: %d      Name: %s      location: x= %d , y=%d", id, name,location.getXLocation(),location.getYLocation());
    }

    public void addNewItem(Item item, String priceSt)
    {
        StoreItem newStoreItem = new StoreItem();
        newStoreItem.setItem(item);
        newStoreItem.setPrice(Integer.parseInt(priceSt));
        newStoreItem.setStore(this);

        itemsThatSellInThisStore.put(item.getId(),newStoreItem);

    }

    public boolean removeItem(Item item) throws Exception {
        boolean discountBeenRemoved = false;

        if(itemsThatSellInThisStore.size() == 1) {
            throw new Exception("This store sell only this item.");
        }
        if(item.getStoresSellThisItem().size() == 1) {
            throw new Exception("This item is sold only by this store");
        }

        for(Discount discount : discounts) {
            if(discount.isItemInDiscount(item)) {
                discounts.remove(discount);
                discountBeenRemoved = true;
            }
        }

        itemsThatSellInThisStore.remove(item.getId());
        item.getStoresSellThisItem().remove(id);

        return discountBeenRemoved;
    }
}
