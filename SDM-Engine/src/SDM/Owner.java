package SDM;

import java.util.HashMap;
import java.util.Map;

//TODO
public class Owner extends User
{
    private Map<Integer, Store> Stores = new HashMap<>();


    public Owner(String userName)
    {
        super(userName, User.Type.OWNER);
    }
}
