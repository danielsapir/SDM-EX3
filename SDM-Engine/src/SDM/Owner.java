package SDM;

import java.util.HashMap;
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
}



