package SDM;

import java.util.List;


public class ThenGet
{

    private List<Offer> Offers;
    private String operator;
    //private double totalPriceToAdd;

    public ThenGet(String operator, List<Offer> offersList)
    {
        this.Offers=offersList;
        this.operator=operator;
    }


    public List<Offer> getOffers() {
        return Offers;
    }

    public void setOffers(List<Offer> offers) {
        Offers = offers;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }




}
