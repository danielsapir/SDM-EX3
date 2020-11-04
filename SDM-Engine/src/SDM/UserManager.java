package SDM;


import java.util.LinkedList;
import java.util.List;

public class UserManager
{
    List<User> users = new LinkedList<>();


    void addUser (String userName, User.Type userType)
    {
        User user = new User(userName,userType);
        users.add(user);
    }

    void removeUser(String userName)
    {
        for (User user:users)
        {
            if(user.getName().equals(userName))
            {
                users.remove(user);
            }
        }
    }

    boolean IsUserAlreadyExist(String userName)
    {
        boolean flagIsUserExist=false;

        for (User user:users)
        {
            if(user.getName().equals(userName))
            {
                flagIsUserExist=true;
            }
        }
        return flagIsUserExist;
    }




}
