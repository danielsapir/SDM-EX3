package SDM;

import SDM.Exception.NegativeAmountOfItemInException;

public class OrderItem {

    private StoreItem itemInOrder;
    private double amount = 0;
    private boolean isBoughtInDiscount;
    private double pricePaid;

    public boolean isBoughtInDiscount() {
        return isBoughtInDiscount;
    }

    public OrderItem(StoreItem itemInOrder, boolean isBoughtInDiscount, double pricePaid) {
        this.itemInOrder = itemInOrder;
        this.isBoughtInDiscount = isBoughtInDiscount;
        this.pricePaid = pricePaid;
    }


    public double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(double pricePaid) {
        this.pricePaid = pricePaid;
    }

    public StoreItem getItemInOrder() {
        return itemInOrder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalPrice() {
        return pricePaid * amount;
    }

    public void clearAmount() {
        amount = 0;
    }

    public void addAmount(double amountToAdd) throws NegativeAmountOfItemInException {
        if(amount + amountToAdd < 0) {
            throw new NegativeAmountOfItemInException(String.valueOf(amount), Double.toString(amountToAdd));
        }
        else {
            amount += amountToAdd;
        }
    }

    public void updateItemAmountSold() throws NegativeAmountOfItemInException {
        itemInOrder.getItem().addAmountThatSold(amount);
    }

    public void updateStoreItemAmountSold() throws NegativeAmountOfItemInException {
        itemInOrder.addAmountThatSold(amount);
    }
}
