package SDM.DTO;

import SDM.Item;
import SDM.OrderItem;

public class OrderItemDTO
{
    private int id;
    private String name;
    private Item.ItemType type;
    private String storeName;
    private int storeId;
    private double amount;
    private double pricePerOne;
    private boolean isPartOfDiscount;


    public OrderItemDTO(OrderItem orderItem)
    {
        this.id=orderItem.getItemInOrder().getItem().getId();
        this.name=orderItem.getItemInOrder().getItem().getName();
        this.type=orderItem.getItemInOrder().getItem().getType();
        this.storeName=orderItem.getItemInOrder().getStore().getName();
        this.storeId=orderItem.getItemInOrder().getStore().getId();
        this.amount=orderItem.getAmount();
        this.pricePerOne=orderItem.getPricePaid();/////לבדוק אם זה באמת טוב מה שהחזרתי
        this.isPartOfDiscount=orderItem.isBoughtInDiscount();
    }
}
