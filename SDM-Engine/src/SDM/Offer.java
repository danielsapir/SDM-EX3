package SDM;

import javax.xml.bind.annotation.XmlAttribute;

public class Offer
{
    protected  int offerId;
    protected int itemId;
    protected double Amount;
    protected int forAdditionalPrice;
    //noy 11/11
    private static int idCounter=0;
    //noy 11/11
    public Offer()
    {
        this.offerId = ++idCounter;

    }



    public int getOfferId() {
        return offerId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getForAdditionalPrice() {
        return forAdditionalPrice;
    }

    public void setForAdditionalPrice(int forAdditionalPrice) {
        this.forAdditionalPrice = forAdditionalPrice;
    }

    @Override
    public String toString() {
        return "ID: " +  itemId  + " Amount: " + getAmount() + " for: " + forAdditionalPrice + " each";
    }
}
