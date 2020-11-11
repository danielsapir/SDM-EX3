package SDM.DTO;

import SDM.Discount;
import SDM.Offer;
import SDM.Store;
import SDM.StoreItem;

import java.util.List;

public class DiscountDTO
{
    private String discountName;
    private int discountId;
    private IfBuyDTO ifBuyDTO;
    private ThenGetDTO thenGetDTO;


    public DiscountDTO(Discount discount)
    {
        this.discountName=discount.getName();
        this.discountId=discount.getDiscountId();
        this.ifBuyDTO=new IfBuyDTO(discount.getIfBuy());
        this.thenGetDTO=new ThenGetDTO(discount.getThenGet());

        setItemNameInOffer(discount);
    }

    //update itemName in Offer in  offer list in class ThanYouGetDTO
    private void setItemNameInOffer(Discount discount)
    {
        for (OfferDTO offerDTO:thenGetDTO.getOfferDTOList())
        {
            StoreItem storeItem =discount.getStoreOfDiscount().getItemsThatSellInThisStore().get(offerDTO.getItemId());
            offerDTO.setItemName(storeItem.getItem().getName());
        }
    }
}
