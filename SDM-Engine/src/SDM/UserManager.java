package SDM;


import java.util.LinkedList;
import java.util.List;

public class UserManager
{
    private List<User> users = new LinkedList<>();

    public List<User> getUsers() {
        return users;
    }

    public User getUserByName(String userName) {
        User userRet = null;
        for(User user : users) {
            if(user.name.equals(userName)) {
                userRet = user;
                break;
            }
        }

        return userRet;
    }

    public void addUser (String userName, User.Type userType)
    {
        User user;
        if(userType==User.Type.CUSTOMER)
        {
            user = new Customer(userName);
        }
        else
        {
            user = new Owner(userName);
        }

        users.add(user);
    }

    public void removeUser(String userName)
    {
        for (User user:users)
        {
            if(user.getName().equals(userName))
            {
                users.remove(user);
            }
        }
    }

    public boolean IsUserAlreadyExist(String userName)
    {
        boolean flagIsUserExist=false;

        for (User user:users)
        {
            if(user.getName().toLowerCase().equals(userName.toLowerCase()))
            {
                flagIsUserExist=true;
            }
        }
        return flagIsUserExist;
    }




}
