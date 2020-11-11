package SDM.DTO;

import SDM.Offer;

public class OfferDTO
{
    private int offerId;
    private int itemId;
    private String itemName;//////////////////need to care in DiscountDTO
    private double amount;
    private int forAdditionalPrice;

    public OfferDTO(Offer offer)
    {
        this.offerId=offer.getOfferId();
        this.itemId=offer.getItemId();
        this.amount=offer.getAmount();
        this.forAdditionalPrice=offer.getForAdditionalPrice();
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public int getItemId()
    {
        return itemId;
    }
}
