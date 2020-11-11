package SDM.DTO;

import SDM.Offer;
import SDM.ThenGet;

import java.util.LinkedList;
import java.util.List;

public class ThenGetDTO
{
    private String operator;
    private List<OfferDTO> offerDTOList=new LinkedList<>();

    public ThenGetDTO(ThenGet thenGet)
    {
        this.operator=thenGet.getOperator();

        for (Offer offer:thenGet.getOffers())
        {
            OfferDTO offerDTO=new OfferDTO(offer);
            this.offerDTOList.add(offerDTO);
        }
    }

    public String getOperator()
    {
        return operator;
    }

    public List<OfferDTO> getOfferDTOList()
    {
        return offerDTOList;
    }



}
