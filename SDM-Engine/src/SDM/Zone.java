package SDM;

import SDM.Exception.*;
import SDM.jaxb.schema.XMLHandlerBaseOnSchema;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverSpi;
import javafx.beans.property.SimpleBooleanProperty;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;




public class Zone
{

    private String name;
    private Owner owner;

    private Map<Integer, Store> allStores = new HashMap<>();
    private Map<Integer, Item> allItems = new HashMap<>();
    //private Map<Integer, Customer> allCustomers = new HashMap<>();
    private List<Order> allOrders;
    //private boolean xmlFileLoaded = false;
    private Order currentOrder;
    private Map<Integer, StoreItem> allStoreItemsWithPriceForSpecificStore = new HashMap<>(); //private Map for storeItems to show to UI
    private SimpleBooleanProperty anyOrderMade = new SimpleBooleanProperty(false);


    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setAllStores(Map<Integer, Store> allStores) {
        this.allStores = allStores;
    }

    public void setAllItems(Map<Integer, Item> allItems) {
        this.allItems = allItems;
    }

    public String getName() {
        return name;
    }

    /*
    public List<Customer> getAllCustomers() {
        return (new ArrayList<>(allCustomers.values()));
    }



    public boolean isXMLFileLoaded() {
        return xmlFileLoaded;
    }


     */

    public Map<Integer, Store> getAllStoresMap() {
        return this.allStores;
    }

    public List<Store> getAllStores() {
        return new ArrayList<>(allStores.values());
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(allItems.values());
    }



    public Order getCurrentOrder() {
        return currentOrder;
    }

    public List<StoreItem> getAllStoreItemsWithPriceForSpecificStore() {
        return new ArrayList<>(allStoreItemsWithPriceForSpecificStore.values());
    }


    public Item.ItemType getItemTypeByID(int itemID) {
        return allItems.get(itemID).getType();
    }



    private void updateAllItemWithTheStoresWhoSellThem(Map<Integer, Item> tempAllItems, Map<Integer, Store> tempAllStores) {
        for (Item item : tempAllItems.values()) {
            for (Store st : tempAllStores.values()) {
                if (st.getItemsThatSellInThisStore().containsKey(item.getId())) {
                    item.setStoresSellThisItem(st);
                }
            }
        }
    }


    private void verifyEveryItemSoldByAtLeastOneStore(Map<Integer, Item> tempAllItems) throws ItemNoOneSellException {
        for(Item item : tempAllItems.values()) {
            if(item.getStoresSellThisItem().size() == 0) {
                throw new ItemNoOneSellException(item.getId());
            }
        }
    }

    private void verifyEveryStoreSellAtLeastOneItem(Map<Integer, Store> tempAllStores) throws StoreWithNoItemException {
        for(Store store : tempAllStores.values()) {
            if(store.getItemsThatSellInThisStore().size() == 0) {
                throw new StoreWithNoItemException(store.getId());
            }
        }
    }

    public void CheckIfIsValidStoreId(int storeId) throws InvalidIdStoreChooseException {
        boolean flaIsValidIdStore = allStores.containsKey(storeId);
        if (!flaIsValidIdStore) {
            throw (new InvalidIdStoreChooseException(storeId));
        }
    }

    public boolean checkIfThisLocationInUsedOfStore(Location costumerLocationToCheck) {
        boolean flagIsValidCostumerLocation = true;

        for (Store st : this.getAllStores()) {
            if (st.getLocation().equals(costumerLocationToCheck)) {
                flagIsValidCostumerLocation = false;
                break;
            }
        }
        return flagIsValidCostumerLocation;


    }

    public void updateAllStoreItemsForSaleInCurrentStoreOrder(Store store) {
        allStoreItemsWithPriceForSpecificStore = new HashMap<>();

        for (Item item : allItems.values()) {
            StoreItem storeItem = new StoreItem();
            storeItem.setItem(item);
            //currentOrder.setStoreOrderMadeFrom(store);
            storeItem.setStore(store);
            int priceOfItem = getPriceOfItemInThisStoreORZero(item.getId(),store);
            storeItem.setPrice(priceOfItem);
            allStoreItemsWithPriceForSpecificStore.put(item.getId(), storeItem);
        }
    }


    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ORDER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    public List<Order> getAllOrders() {
        return allOrders;
    }

    public void createNewDynamicOrder(Customer customerEX1, Date dateOrder) {
        currentOrder = Order.makeNewOrder(customerEX1, dateOrder, null, OrderType.DYNAMIC_ORDER);
    }

    public void createNewOneStoreOrder(Customer customerEX1, Date dateOrder, Store store) {
        currentOrder = Order.makeNewOrder(customerEX1, dateOrder, store, OrderType.ONE_STORE_ORDER);
    }

    public void addItemToCurrentOrder(int choosedItemId, double choosedAmountOfItem) throws NegativeAmountOfItemInException
    {
        this.currentOrder.addItemToOrder(allItems.get(choosedItemId),choosedAmountOfItem);
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

    public boolean isAnyOrderMade() {
        return anyOrderMade.get();
    }

    public SimpleBooleanProperty anyOrderMadeProperty() {
        return anyOrderMade;
    }

    public boolean useDiscountOfCurrentOrder(Discount discountToUse) throws NegativeAmountOfItemInException {
        return currentOrder.useDiscount(discountToUse, null);
    }

    public boolean useDiscountOfCurrentOrder(Discount discountToUse, Offer offerChosen) throws NegativeAmountOfItemInException {
        return currentOrder.useDiscount(discountToUse, offerChosen);
    }

    public void completeCurrentOrder() throws NegativeAmountOfItemInException {
        currentOrder.completeOrder();
        allOrders.add(currentOrder);
        currentOrder = null;

        if(!anyOrderMade.get()) {
            anyOrderMade.set(true);
        }
    }

    public void cancelCurrentOrder() {
        currentOrder = null;
    }


    private int getPriceOfItemInThisStoreORZero(int itemId, Store store) {
        int resPrice = 0;

        if (store.getItemsThatSellInThisStore().containsKey(itemId)) {
            resPrice = store.getItemsThatSellInThisStore().get(itemId).getPrice();
        }

        return resPrice;
    }

    public boolean checkIfThisValidItemId(int choosedItemNumber) {
        return allStoreItemsWithPriceForSpecificStore.containsKey(choosedItemNumber);
    }

    public boolean checkIfItemPriceIsNotZero(int choosedItemNumber) {
        return (allStoreItemsWithPriceForSpecificStore.get(choosedItemNumber).getPrice()) != 0;
    }

    /*
    public void addNewItemToStore(int storeID, Item itemToAdd, int priceOfItem) {
        //Store storeToAddItem = allStores.get(storeID).addNewItem();
        //storeToAddItem.getItemsThatSellInThisStore().
    }

     */



    public Item getItemById(int itemId) {
        return allItems.get(itemId);
    }


    public void addNewItemToStore(Store st, Item item, String priceSt)
    {
        st.addNewItem(item,priceSt);
    }


    //update item price
    public void updatePriceOfItem(Store st, Item item, String priceSt) {
        st.getItemsThatSellInThisStore().get(item.getId()).setPrice(Integer.parseInt(priceSt));
    }


    //remove item from store
    public boolean removeItemFromStore(Store st, Item item) throws Exception {
        return st.removeItem(item);
    }

   ///noy 6/11
    //move on allStore in zone and update store owner
    public void addOwnerToAllStores(Owner owner)
    {
        for (Store st:this.allStores.values())
        {
            st.setOwner(owner);
        }
    }

    public double orderPriceAvg()
    {
        double avgPrice=0;
        for (Order order:this.allOrders)
        {
            avgPrice+=order.totalPrice;
        }

        if(this.allOrders.size()>0)
        {
            avgPrice = avgPrice / this.allOrders.size();
        }

        return avgPrice;
    }
}




