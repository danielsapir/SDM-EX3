package SDM;

import SDM.Exception.NegativeAmountOfItemInException;

import java.util.*;

public class OneStoreOrder extends Order {

    private final Store storeOrderMadeFrom;
    private boolean partOfDynamicOrder = false;
    private DynamicOrder dynamicOrder;

    public DynamicOrder getDynamicOrder() {
        return dynamicOrder;
    }

    public void setDynamicOrder(DynamicOrder dynamicOrder) {
        this.dynamicOrder = dynamicOrder;
    }

    public OneStoreOrder(Customer customer, Date date, Store storeOrderMadeFrom1) {
        super(customer, date);
        this.storeOrderMadeFrom = storeOrderMadeFrom1;
        calculateDeliveryPrice();
    }

    protected void calculateDeliveryPrice() {
        deliveryPrice = storeOrderMadeFrom.getDeliveryPPK() * distanceBetweenCostumerAndStore();
    }

    public Store getStoreOrderMadeFrom() {
        return storeOrderMadeFrom;
    }

    public boolean isPartOfDynamicOrder() {
        return partOfDynamicOrder;
    }

    public void setPartOfDynamicOrder(boolean partOfDynamicOrder) {
        this.partOfDynamicOrder = partOfDynamicOrder;
    }

    @Override
    public void addItemToOrder(Item itemToAdd, double amountToAdd) throws NegativeAmountOfItemInException {
        addStoreItemToOrder(storeOrderMadeFrom.getItemsThatSellInThisStore().get(itemToAdd.getId()), amountToAdd);
    }

    private void addStoreItemToOrder(StoreItem itemToAdd, double amountToAdd) throws NegativeAmountOfItemInException {
        if(orderItemCart.containsKey(itemToAdd.getItem().getId())) {
            orderItemCart.get(itemToAdd.getItem().getId()).addAmount(amountToAdd);
        }
        else {
            OrderItem newItemInOrder = new OrderItem(itemToAdd,false, itemToAdd.getPrice() );
            newItemInOrder.addAmount(amountToAdd);
            orderItemCart.put(itemToAdd.getItem().getId(), newItemInOrder);
        }
    }

    public void completeOrder() {
        incIdCounter();
        priceOfAllItems = calculatePriceOfOrderItems();
        totalPrice = priceOfAllItems + deliveryPrice;
        orderItemCart.forEach((orderItemID, orderItem) -> {
            try {
                orderItem.updateItemAmountSold();
                orderItem.updateStoreItemAmountSold();
            } catch (NegativeAmountOfItemInException e) {
                orderItem.clearAmount();
            }
        });
        itemsBoughtWithDiscount.forEach((orderItemID, orderItem) -> {
            try {
                orderItem.updateItemAmountSold();
                orderItem.updateStoreItemAmountSold();
            } catch (NegativeAmountOfItemInException e) {
                orderItem.clearAmount();
            }
        });

        if(!partOfDynamicOrder) {
            customer.addNewOrder(this);
        }

        storeOrderMadeFrom.getOrders().add(this);
    }

    @Override
    protected double calculatePriceOfOrderItems() {
        priceOfAllItems = orderItemCart.values().stream().mapToDouble(OrderItem::getTotalPrice).sum();
        priceOfAllItems += itemsBoughtWithDiscount.values().stream().mapToDouble(OrderItem::getTotalPrice).sum();
        return priceOfAllItems;
    }

    public double distanceBetweenCostumerAndStore() {
        return Location.distanceBetweenLocations(customer.getLocation(), storeOrderMadeFrom.getLocation());
    }

    @Override
    public int getTotalItemsInOrder()
    {
        int totalItems=0;

        for (Map.Entry<Integer, OrderItem> iterator : orderItemCart.entrySet())
        {
            OrderItem orderItem = iterator.getValue();
            Item.ItemType type=orderItem.getItemInOrder().getItem().getType();

            switch (type)
            {
                case QUANTITY:
                    totalItems+=(orderItem).getAmount();
                    break;
                case WEIGHT:
                    totalItems+=1;
                    break;
            }
        }

        for (Map.Entry<Integer, OrderItem> iterator : itemsBoughtWithDiscount.entrySet())
        {
            OrderItem orderItem = iterator.getValue();
            Item.ItemType type=orderItem.getItemInOrder().getItem().getType();

            switch (type)
            {
                case QUANTITY:
                    totalItems+=(orderItem).getAmount();
                    break;
                case WEIGHT:
                    totalItems+=1;
                    break;
            }
        }
        return (totalItems);
    }

    @Override
    public void continueToDiscounts() {
        priceOfAllItems = calculatePriceOfOrderItems();
        calculateDiscounts();
    }

    protected void calculateDiscounts() {
        for(OrderItem item : orderItemCart.values()) {
            List<Discount> listOfDiscounts = getDiscountsForOrderItem(item);
            discountsAvailable.addAll(listOfDiscounts);
        }
    }

    private List<Discount> getDiscountsForOrderItem(OrderItem item) {
        List<Discount> discountList = new LinkedList<>();

        for(Discount discountOfStore : storeOrderMadeFrom.getDiscounts()) {
            if(discountOfStore.getIfBuy().getStoreItem().getItem().getId() == item.getItemInOrder().getItem().getId()) {
                for (int i = 0; i < (int) (item.getAmount() / discountOfStore.getIfBuy().getQuantity()); i++) {
                    discountList.add(discountOfStore);
                }
            }
        }

        return discountList;
    }

    @Override
    public boolean useDiscount(Discount discountToUse, Offer offerChosen) throws NegativeAmountOfItemInException {
        if(!discountsAvailable.contains(discountToUse)) {
            return false;
        }
        switch(discountToUse.getThenGet().getOperator()) {
            case "IRRELEVANT":
                addOfferItemToCart(discountToUse.getThenGet().getOffers().get(0));
                break;
            case "ALL-OR-NOTHING":
                for(Offer offerInDiscount : discountToUse.getThenGet().getOffers()) {
                    addOfferItemToCart(offerInDiscount);
                }
                break;
            case "ONE-OF":
                addOfferItemToCart(offerChosen);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public List<OneStoreOrder> getListOfOneStoreOrders() {
        List<OneStoreOrder> listOfOneStoreOrders = new LinkedList<>();
        listOfOneStoreOrders.add(this);
        return listOfOneStoreOrders;
    }

    private void addOfferItemToCart(Offer offerToAdd) throws NegativeAmountOfItemInException {
        StoreItem itemChosen = storeOrderMadeFrom.getItemsThatSellInThisStore().get((offerToAdd.itemId));
        if(itemsBoughtWithDiscount.containsKey(itemChosen.getItem().getId())) {
            itemsBoughtWithDiscount.get(itemChosen.getItem().getId()).addAmount(offerToAdd.Amount);
        }
        else {
            OrderItem newItemInOrder = new OrderItem(itemChosen,true, offerToAdd.forAdditionalPrice);
            newItemInOrder.addAmount(offerToAdd.Amount);
            itemsBoughtWithDiscount.put(itemChosen.getItem().getId(), newItemInOrder);
        }
    }
}
