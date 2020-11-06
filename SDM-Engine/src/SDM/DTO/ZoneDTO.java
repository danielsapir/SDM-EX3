package SDM.DTO;

import SDM.Order;
import SDM.Owner;
import SDM.Zone;

public class ZoneDTO
{

    private Owner owner;
    private String zoneName;
    private int itemsNumber;
    private int storesNumber;
    private int ordersNumber;
    private double orderPriceAvg;

    public ZoneDTO(Zone zone)
    {
       this.owner=zone.getOwner();
       this.zoneName=zone.getName();
       this.itemsNumber=zone.getAllItems().size();
       this.storesNumber=zone.getAllStores().size();
       //this.ordersNumber=zone.order

    }
}
