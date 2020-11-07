package SDM.DTO;

import SDM.MoneyAccount;
import SDM.User;

public class UserDTO
{
    private String userName;
    private String userType;
    private MoneyAccount moneyAccount;

    public UserDTO(User user)
    {
        this.userName=user.getName();
        this.userType=user.getType().toString();
        this.moneyAccount=user.getMoneyAccount();
    }

}
