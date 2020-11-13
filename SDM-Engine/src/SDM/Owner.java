package SDM;

import javafx.collections.transformation.FilteredList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//TODO
public class Owner extends User
{
    private Map<Integer, Store> stores = new HashMap<>();


    public Owner(String userName)
    {
        super(userName, User.Type.OWNER);
    }

    //noy 6/11
    //add stores to map of owner stores
    public void addStoresToOwner(Map<Integer, Store> allStores)
    {
        for (Store st:allStores.values())
        {
            this.stores.put(st.getId(),st);
        }
    }

    //noy 12/11
    public Map<Integer, Store> getOwnerStores()
    {
        return stores;
    }


    //noy 12/11
    public List<FeedBack> feedBacksOfOwnerInTheZone(Zone zone)
    {
        List<FeedBack> feedBacks=new LinkedList<>();
        for (Store store:zone.getAllStores())
        {
            if(store.getOwner().getName().equals(this.name))
            {
                for (OneStoreOrder oneStoreOrder:store.getOrders())
                {
                    feedBacks.add(oneStoreOrder.getFeedBack());
                }
            }
        }
        return (feedBacks);
    }


}



