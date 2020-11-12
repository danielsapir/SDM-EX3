package SDM.DTO;

import SDM.IfBuy;

public class IfBuyDTO
{
    private String itemName;
    private double quantity;

    public IfBuyDTO(IfBuy ifBuy)
    {
        this.itemName=ifBuy.getStoreItem().getItem().getName();
        this.quantity=ifBuy.getQuantity();
    }
}
