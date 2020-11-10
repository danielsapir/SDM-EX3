package SDM;

import SDM.Exception.NegativeAmountOfItemInException;

import java.util.*;

public class DynamicOrder extends Order{

    private class DynamicOrderItem {
        Item itemInOrder;
        double amountOfItem;

        public DynamicOrderItem(Item itemInOrder, double amountOfItem) {
            this.itemInOrder = itemInOrder;
            this.amountOfItem = amountOfItem;
        }
    }



    private Map<Integer, OneStoreOrder> innerOneStoreOrderMap = new HashMap<>();
    private List<DynamicOrderItem> itemsInCart = new LinkedList<>();

    public DynamicOrder(Customer customer, Date date, Location destinationLocation) {
        super(customer, date, destinationLocation);
    }

    public Map<Integer, OneStoreOrder> getInnerOneStoreOrderMap() {
        return innerOneStoreOrderMap;
    }

    @Override
    protected void calculateDeliveryPrice() {
        deliveryPrice = 0;
        for(OneStoreOrder oneStoreOrder : innerOneStoreOrderMap.values()) {
            deliveryPrice += oneStoreOrder.getDeliveryPrice();
        }
    }

    @Override
    public void addItemToOrder(Item itemToAdd, double amountToAdd) throws NegativeAmountOfItemInException {
        itemsInCart.add(new DynamicOrderItem(itemToAdd,amountToAdd));
    }

    @Override
    public void completeOrder() throws NegativeAmountOfItemInException {
        incIdCounter();

        for(OneStoreOrder oneStoreOrder : innerOneStoreOrderMap.values()) {
            oneStoreOrder.completeOrder();
        }

        priceOfAllItems = calculatePriceOfOrderItems();
        totalPrice = priceOfAllItems + deliveryPrice;
        customer.addNewOrder(this);
    }

    //Greedy algorithm
    private void bestPriceAlgorithm() throws NegativeAmountOfItemInException {
        for (DynamicOrderItem dynamicOrderItem : itemsInCart) {
            Store cheapestStore = getCheapestSellerOfItem(dynamicOrderItem);
            if(!innerOneStoreOrderMap.containsKey(cheapestStore.getId())) {
                innerOneStoreOrderMap.put(cheapestStore.getId(), new OneStoreOrder(customer, date, cheapestStore, destinationLocation));
                innerOneStoreOrderMap.get(cheapestStore.getId()).setPartOfDynamicOrder(true);
                innerOneStoreOrderMap.get(cheapestStore.getId()).setDynamicOrder(this);
            }
            addItemToExistOneStoreOrder(dynamicOrderItem, cheapestStore);
        }
    }

    private void addItemToExistOneStoreOrder(DynamicOrderItem dynamicOrderItem, Store cheapestStore) throws NegativeAmountOfItemInException {
        innerOneStoreOrderMap.get(cheapestStore.getId()).addItemToOrder(dynamicOrderItem.itemInOrder,dynamicOrderItem.amountOfItem);
    }

    private Store getCheapestSellerOfItem(DynamicOrderItem dynamicOrderItem) {
        Store cheapestSeller = null;

        for (Store seller : dynamicOrderItem.itemInOrder.getStoresSellThisItem().values()) {
            if(cheapestSeller == null) {
                cheapestSeller = seller;
            }
            else {
                if(cheapestSeller.getPriceOfItemInStore(dynamicOrderItem.itemInOrder) > seller.getPriceOfItemInStore(dynamicOrderItem.itemInOrder)) {
                    cheapestSeller = seller;
                }
            }
        }

        return cheapestSeller;
    }

    @Override
    protected double calculatePriceOfOrderItems() {
        double totalPriceOfItemsInAllOrders = 0;

        for (OneStoreOrder oneStoreOrder : innerOneStoreOrderMap.values()) {
            totalPriceOfItemsInAllOrders += oneStoreOrder.calculatePriceOfOrderItems();
        }

        return  totalPriceOfItemsInAllOrders;
    }

    @Override
    public int getTotalItemsInOrder() {
        int totalItemInAllOrders = 0;

        for (OneStoreOrder oneStoreOrder : innerOneStoreOrderMap.values()) {
            totalItemInAllOrders += oneStoreOrder.getTotalItemsInOrder();
        }

        return totalItemInAllOrders;
    }

    @Override
    public void continueToDiscounts() throws NegativeAmountOfItemInException {
        bestPriceAlgorithm();

        for(OneStoreOrder oneStoreOrder : innerOneStoreOrderMap.values()) {
            oneStoreOrder.continueToDiscounts();
            discountsAvailable.addAll(oneStoreOrder.discountsAvailable);
        }
    }

    @Override
    public boolean useDiscount(Discount discountToUse, Offer offerChosen) throws NegativeAmountOfItemInException {
        if(!innerOneStoreOrderMap.containsKey(discountToUse.getStoreOfDiscount().getId())) {
            return false;
        }

        return innerOneStoreOrderMap.get(discountToUse.getStoreOfDiscount().getId()).useDiscount(discountToUse, offerChosen);
    }

    @Override
    public List<OneStoreOrder> getListOfOneStoreOrders() {
        return new LinkedList<OneStoreOrder>(innerOneStoreOrderMap.values());
    }
}
