package SDM;

public class Discount
{

    private int discountId;
    private String name;
    private IfBuy ifBuy;
    private ThenGet thenGet;
    private Store storeOfDiscount;
    //noy 11/11
    private static int idCounter = 0;

    //noy 11/11
    public Discount()
    {
        this.discountId = ++idCounter;
    }


    public int getDiscountId() {
        return discountId;
    }

    public Store getStoreOfDiscount() {
        return storeOfDiscount;
    }

    public void setStoreOfDiscount(Store storeOfDiscount) {
        this.storeOfDiscount = storeOfDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IfBuy getIfBuy() {
        return ifBuy;
    }

    public void setIfBuy(IfBuy ifBuy) {
        this.ifBuy = ifBuy;
    }

    public ThenGet getThenGet() {
        return thenGet;
    }

    public void setThenGet(ThenGet thenGet) {
        this.thenGet = thenGet;
    }

    public boolean isItemInDiscount(Item item) {
        boolean itemInDiscount = false;

        itemInDiscount = ifBuy.getStoreItem().getItem().getId() == item.getId();
        for(Offer offer : thenGet.getOffers()) {
            itemInDiscount = itemInDiscount || (offer.getItemId() == item.getId());
        }

        return itemInDiscount;
    }



    //noy 12/11
    public Offer getOfferById(int offerId)
    {
        for (Offer offer: this.thenGet.getOffers())
        {
           if(offer.getOfferId()==offerId)
           {
               return (offer);
           }
        }
        return (null);
    }

}
