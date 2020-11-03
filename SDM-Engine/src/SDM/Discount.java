package SDM;

import SDM.jaxb.schema.generated.IfYouBuy;
import SDM.jaxb.schema.generated.ThenYouGet;

import javax.xml.bind.annotation.XmlElement;

public class Discount
{

    private String name;
    private IfBuy ifBuy;
    private ThenGet thenGet;
    private Store storeOfDiscount;

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
}
