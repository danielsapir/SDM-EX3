package SDM;

import SDM.Exception.*;

import java.util.*;


public class Zone
{

    private String name;
    private Owner owner;
    private Map<Integer, Store> allStores = new HashMap<>();
    private Map<Integer, Item> allItems = new HashMap<>();
    private List<Order> allOrders = new LinkedList<>();
    private Map<Integer, StoreItem> allStoreItemsWithPriceForSpecificStore = new HashMap<>(); //private Map for storeItems to show to UI
    //private SimpleBooleanProperty anyOrderMade = new SimpleBooleanProperty(false);
    //private boolean xmlFileLoaded = false;
    //private Order currentOrder;
    //private Map<Integer, Customer> allCustomers = new HashMap<>();

    public void addOrder(Order order)
    {
        allOrders.add(order);
    }

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


/*
    public Order getCurrentOrder() {
        return currentOrder;
    }

 */

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

    public Map<Integer, StoreItem> getAllStoreItemsForSaleInCurrentStoreForOrder(Store store)
    {
        Map<Integer, StoreItem> allStoreItemsWithPriceForSpecificStore = new HashMap<>();

        //noy 11/11
        if(store==null)
        {
            for (Item item : allItems.values()) {
                int price=0;
                StoreItem storeItem = new StoreItem();
                storeItem.setItem(item);
                storeItem.setStore(null);
                storeItem.setPrice(price);
                allStoreItemsWithPriceForSpecificStore.put(item.getId(), storeItem);
            }
        }

        else
        {
            for (Item item : allItems.values()) {
                StoreItem storeItem = new StoreItem();
                storeItem.setItem(item);
                //currentOrder.setStoreOrderMadeFrom(store);
                storeItem.setStore(store);
                int priceOfItem = getPriceOfItemInThisStoreORZero(item.getId(), store);
                storeItem.setPrice(priceOfItem);
                allStoreItemsWithPriceForSpecificStore.put(item.getId(), storeItem);
            }
        }

        return allStoreItemsWithPriceForSpecificStore;
    }


    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ORDER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    public List<Order> getAllOrders() {
        return allOrders;
    }


/*
    public boolean isAnyOrderMade() {
        return anyOrderMade.get();
    }

    public SimpleBooleanProperty anyOrderMadeProperty() {
        return anyOrderMade;
    }

 */



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
    

    //13/11 NOY
    public Store openNewStore(String newStoreName,Location newStoreLocation, int ppk)
    {
        Store store=new Store();
        store.setId(this.getUniqueStoreId());
        store.setOwner(owner);
        store.setName(newStoreName);
        store.setLocation(newStoreLocation);
        store.setDeliveryPPK(ppk);
        return(store);
    }

    //13/11 NOY
    private int getUniqueStoreId()
    {
        int maxId=1;
        for (Store st:this.allStores.values())
        {
            if(st.getId()>=maxId)
            {
                maxId=st.getId();
            }
        }
        return (++maxId);
    }

    //13/11 NOY
    public void addItemToStoreHelper(int itemId,double price,Store store)
    {
        Item item=this.allItems.get(itemId);
        String priceSt= String.valueOf(price);
        store.addNewItem(item,priceSt);
    }

    //14/11 Noy
    public void finishOpenNewStore(Store st)
    {
        giveOpenNewStoreNotification(st);
    }




    //noy 14/11
    private void giveOpenNewStoreNotification(Store store)
    {
        String message= String.format
                ("%s opened a new store named: %s, in yor zone: %s. " +
                                "/n the store located in:(%d,%d),/n and the store sales %d of %d."
                        ,store.getOwner().getName(),store.getName(),this.name, store.getLocation().getXLocation(),store.getLocation().getYLocation(),store.getItemsThatSellInThisStore().size(),this.allItems.size() );

        Notification notification=new Notification(Notification.Type.NewStore,message);
        this.getOwner().addNotification(notification);
    }










}




