package SDM.jaxb.schema;

import SDM.*;
import SDM.Exception.*;
import SDM.Location;
import SDM.jaxb.schema.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class XMLHandlerBaseOnSchema
{
    private List<Store> stores = null;
    private Map<Integer,Item> items =null;
    //NOY 25/9
    private Map<Integer,Customer> costumers =null;
    private Consumer<String> updateGuiWithProgressMessage;
    private Consumer<Double> updateGuiWithProgressPercent;

    public Map<Integer, Customer> getCostumers() {
        return costumers;
    }


    public List<Store> getStores()
    {
        return stores;
    }

    public Map<Integer, Item> getItems()
    {
        return items;
    }

    public void updateStoresAndItemsAndCostumers(String stPath, Consumer<String> updateGuiWithProgressMessage, Consumer<Double> updateGuiWithProgressPercent) throws FileNotFoundException, JAXBException, FileNotEndWithXMLException, DuplicateItemException, LocationIsOutOfBorderException, DuplicateStoreIDException, DuplicateStoreItemException, TryingToGivePriceOfItemWhichIDNotExistException, TryingToGiveDifferentPricesForSameStoreItemException, DuplicateCustomerIdException, DuplicatedLocationException, DiscountWithItemNotSoldByStoreException {
        this.updateGuiWithProgressMessage = updateGuiWithProgressMessage;
        this.updateGuiWithProgressPercent = updateGuiWithProgressPercent;

        this.updateGuiWithProgressMessage.accept("Fetching File...");
        this.updateGuiWithProgressPercent.accept(0.0);
        ThreadSleepProxy.goToSleep(500);
        SuperDuperMarketDescriptor sdmDescriptor=this.fromStringPathToDescriptor(stPath);
        this.updateGuiWithProgressMessage.accept("Fetching Items...");
        this.updateGuiWithProgressPercent.accept(0.1);
        ThreadSleepProxy.goToSleep(500);
        parseFromSDMItemToItem(sdmDescriptor);
        this.updateGuiWithProgressMessage.accept("Fetching Stores...");
        this.updateGuiWithProgressPercent.accept(0.2);
        ThreadSleepProxy.goToSleep(500);
        parseFromSDMStoresToStores(sdmDescriptor);
        this.updateGuiWithProgressMessage.accept("Fetching Customers...");
        this.updateGuiWithProgressPercent.accept(0.5);
        ThreadSleepProxy.goToSleep(500);
        parseFromSDMCustomersToCustomers(sdmDescriptor);

        verifyNoDuplicatedLocations();

    }

    private void verifyNoDuplicatedLocations() throws DuplicatedLocationException {
        Set<Location> locations  = new HashSet<>();

        for(Store store : stores) {
            if(locations.contains(store.getLocation())) {
                throw new DuplicatedLocationException(store.getLocation());
            }
            else {
                locations.add(store.getLocation());
            }
        }
        for(Customer customer : costumers.values()) {
            if(locations.contains(customer.getLocation())){
                throw new DuplicatedLocationException(customer.getLocation());
            }
            else {
                locations.add(customer.getLocation());
            }
        }
    }

    private SuperDuperMarketDescriptor fromStringPathToDescriptor(String inpPath) throws FileNotFoundException, JAXBException, FileNotEndWithXMLException
    {
        File inputFile = new File(inpPath);
        if(!inputFile.exists()) {
            throw new FileNotFoundException();
        }

        if(inpPath.length()-4!=(inpPath.toLowerCase().lastIndexOf(".xml")))
        {
            throw (new FileNotEndWithXMLException(inpPath.substring(inpPath.length()-3)));
        }

        InputStream inputStream = new FileInputStream(new File(inpPath));
        return (deserialize(inputStream));

    }

    //deserialize from input to SuperDuperMarket

    private SuperDuperMarketDescriptor deserialize(InputStream in)throws JAXBException
    {
        String JAXB_XML_PACKAGE_NAME = "SDM.jaxb.schema.generated";
        JAXBContext jc= JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u=jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }


    ///////NOY JOB 25/9
    private void parseFromSDMCustomersToCustomers(SuperDuperMarketDescriptor sdmObj) throws DuplicateItemException, LocationIsOutOfBorderException, DuplicateCustomerIdException {

        List<SDMCustomer> sdmCustomers= sdmObj.getSDMCustomers().getSDMCustomer();
        this.costumers=new HashMap<>();

        Customer customer;

        for (SDMCustomer sdmCustomer:sdmCustomers)
        {
            if(this.costumers.containsKey(sdmCustomer.getId()))
            {
                throw (new DuplicateItemException(sdmCustomer.getId()));
            }

            //else
            SDM.Location customerLocation =new SDM.Location(new Point(sdmCustomer.getLocation().getX(),sdmCustomer.getLocation().getY()));
            customer=new Customer(sdmCustomer.getId(),sdmCustomer.getName(),customerLocation);

            if(!Location.checkIfIsLegalLocation(customer.getLocation().getXLocation(), customer.getLocation().getYLocation())) {
                throw new LocationIsOutOfBorderException(Location.minBorder, Location.maxBorder, "Customer", customer.getId());
            }

            if(this.costumers.containsKey(customer.getId())) {
                throw new DuplicateCustomerIdException(customer.getId());
            }

            this.costumers.put(customer.getId(), customer);
        }

    }





    private void parseFromSDMItemToItem(SuperDuperMarketDescriptor sdmObj) throws DuplicateItemException
    {
        List<SDMItem> sdmItems= sdmObj.getSDMItems().getSDMItem();
        this.items=new HashMap<>();
        Item item;

        for (SDMItem sdmItem:sdmItems)
        {
            if(this.items.containsKey(sdmItem.getId()))
            {
                throw (new DuplicateItemException(sdmItem.getId()));
            }
            item=new Item(sdmItem.getId(),sdmItem.getName());
            item.checkAndUpdateItemType(sdmItem.getPurchaseCategory());
            this.items.put(item.getId(),item);
        }
    }


    private void parseFromSDMStoresToStores(SuperDuperMarketDescriptor sdmObj) throws DuplicateStoreIDException, DuplicateStoreItemException, LocationIsOutOfBorderException, TryingToGivePriceOfItemWhichIDNotExistException, TryingToGiveDifferentPricesForSameStoreItemException, DiscountWithItemNotSoldByStoreException {
        List<SDMStore> sdmStores=  sdmObj.getSDMStores().getSDMStore();
        this.stores=new ArrayList<>();
        Store st;
        this.updateGuiWithProgressMessage.accept("Fetching Items in Stores...");
        this.updateGuiWithProgressPercent.accept(0.3);
        ThreadSleepProxy.goToSleep(1000);
        this.updateGuiWithProgressMessage.accept("Fetching Discounts of Stores...");
        this.updateGuiWithProgressPercent.accept(0.4);
        ThreadSleepProxy.goToSleep(1000);

        for (SDMStore sdmSt:sdmStores)
        {
            //st=new Store();

            for (Store s:this.stores)
            {
                if(s.getId()==sdmSt.getId())
                {
                    throw (new DuplicateStoreIDException(sdmSt.getId()));
                }
            }

            st=new Store();
            st.setId(sdmSt.getId());
            st.setName(sdmSt.getName());
            st.setDeliveryPPK(sdmSt.getDeliveryPpk());
            
            boolean flagIsItLegalLocation=Location.checkIfIsLegalLocation(sdmSt.getLocation().getX(),sdmSt.getLocation().getY());
            if(!flagIsItLegalLocation)
            {
                throw (new LocationIsOutOfBorderException(Location.minBorder,Location.maxBorder, "Store" , sdmSt.getId() ));
            }
            
            st.setLocation(new Location(new Point(sdmSt.getLocation().getX(),sdmSt.getLocation().getY())));


            Map<Integer,StoreItem> itemsInStStore= this.getStoreItemesFromsdmPrices(sdmSt,st);
            st.setItemsThatSellInThisStore(itemsInStStore);

            //NOY 26/9

            List<Discount> discountsOfStore=this.getDiscountsFromSDMDiscounts(sdmSt,st);
            st.setDiscounts(discountsOfStore);


            this.stores.add(st);
        }
    }


    //NOY 26/9
    private List<Discount> getDiscountsFromSDMDiscounts(SDMStore sdmSt, Store st) throws DiscountWithItemNotSoldByStoreException {
        List<Discount> storeDiscountsRetList=new LinkedList<>();
        Discount discount;

        if (sdmSt.getSDMDiscounts()==null)//no discounts in this store
        {
            discount=null;
        }

        else {//have discounts in this store
            for (SDMDiscount sdmDiscount : sdmSt.getSDMDiscounts().getSDMDiscount()) {


                discount = new Discount();
                discount.setName(sdmDiscount.getName());
                discount.setStoreOfDiscount(st);
                discount.setIfBuy(getClassIfBuyFromClassIfYouBuy(sdmDiscount.getIfYouBuy(), st));
                discount.setThenGet(getClassThanGetFromClassThanYouGet(sdmDiscount.getThenYouGet(), st));


                storeDiscountsRetList.add(discount);
            }
        }

        return(storeDiscountsRetList);
    }


    //Noy 26/9
    private ThenGet getClassThanGetFromClassThanYouGet(ThenYouGet thenYouGet, Store st) throws DiscountWithItemNotSoldByStoreException {

        List<Offer> offersList=new LinkedList<>();

        //convert SDMOffer to Offer//maybe function?
        for (SDMOffer sdmOffer:thenYouGet.getSDMOffer()) {
            Offer offer = new Offer();
            offer.setItemId(sdmOffer.getItemId());
            offer.setAmount(sdmOffer.getQuantity());
            offer.setForAdditionalPrice(sdmOffer.getForAdditional());

            if(!st.getItemsThatSellInThisStore().containsKey(offer.getItemId())) {
                throw new DiscountWithItemNotSoldByStoreException(offer.getItemId(), st);
            }
            offersList.add(offer);
        }

        ThenGet thenGetRes=new ThenGet(thenYouGet.getOperator(),offersList);
        return (thenGetRes);
    }


    //Noy 26/9
    //get the class IfBuy from the class IfYouBuy
    private IfBuy getClassIfBuyFromClassIfYouBuy(IfYouBuy ifYouBuy, Store st) throws DiscountWithItemNotSoldByStoreException {
        StoreItem storeItem=st.getItemsThatSellInThisStore().get(ifYouBuy.getItemId());

        if(storeItem == null){
            throw new DiscountWithItemNotSoldByStoreException(ifYouBuy.getItemId(), st);
        }

        IfBuy ifBuyRet=new IfBuy(storeItem, ifYouBuy.getQuantity());
        return(ifBuyRet);

    }

    //convert sdmPrices to storeItem
    private Map<Integer, StoreItem> getStoreItemesFromsdmPrices(SDMStore sdmSt, Store st) throws DuplicateStoreItemException, TryingToGivePriceOfItemWhichIDNotExistException, TryingToGiveDifferentPricesForSameStoreItemException {
        List<SDMSell> listSDMSell =sdmSt.getSDMPrices().getSDMSell();
        Map<Integer,StoreItem> retMapStoreItems=new HashMap<>();
        StoreItem sti;

        for (SDMSell sdmSell:listSDMSell)
        {
            if(retMapStoreItems.containsKey(sdmSell.getItemId()))
            {
               throw (new DuplicateStoreItemException(sdmSell.getItemId()));
            }
            if(this.items.containsKey(sdmSell.getItemId()))
            {
                sti=new StoreItem();
                if (sti.getPrice()!=0)
                {
                    throw (new TryingToGiveDifferentPricesForSameStoreItemException(st.getId()));
                }
                sti.setPrice(sdmSell.getPrice());
                sti.setStore(st);
                sti.setItem(this.items.get(sdmSell.getItemId()));
                retMapStoreItems.put(sti.getItem().getId(),sti);
            }
            else
                throw (new TryingToGivePriceOfItemWhichIDNotExistException(sdmSell.getItemId()));
        }
        return (retMapStoreItems);
    }
}
