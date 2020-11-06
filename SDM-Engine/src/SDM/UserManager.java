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
        User user = new User(userName,userType);
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
            if(user.getName().equals(userName))
            {
                flagIsUserExist=true;
            }
        }
        return flagIsUserExist;
    }




}
