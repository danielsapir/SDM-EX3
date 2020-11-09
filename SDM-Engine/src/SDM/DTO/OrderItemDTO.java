package SDM.DTO;

import SDM.Item;
import SDM.OrderItem;

public class OrderItemDTO
{
    int id;
    String name;
    Item.ItemType type;
    String storeName;
    int storeId;
    double amount;
    double pricePerOne;
    boolean isPartOfDiscount;


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
