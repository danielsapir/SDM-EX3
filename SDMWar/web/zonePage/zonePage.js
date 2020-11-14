var ZONE_PAGE_URL = buildUrlWithContextPath("zonepage");
var DASHBOARD_URL = buildUrlWithContextPath("dashboard");
var userType;
var fetchedStores;
var fetchedItems;
var ownerScriptIsLoaded = false;
var userName;

$(function () {
    $.ajax({
        url: DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "user-info"
        },

        //user = {userName:"moshe", userType:"OWNER/CUSTOMER",...}
        success: function (user) {
            if(user === null) {
                window.location.replace(buildUrlWithContextPath("login/login.html"));
            }

            $("#user-name-placeholder").text(user.userName);
            userType = user.userType;
            userName = user.userName;
            if(user.userType === "OWNER") {
                $(".customer-only").remove();
            }
            else {
                $(".owner-only").remove();
            }
        }
    })
})

$(function () {
    $.ajax({
        url: ZONE_PAGE_URL,
        method: "GET",
        data: {
            reqtype: "zone-info"
        },

        //currentZone = {ownerName: "Moshe", zoneName: "Tel-Aviv", itemNumber: 7, storesNumber: 12, ordersNumber: 32, orderPriceAvg: 23.53
        success: function (currentZone) {
            $("#zoneName").text(currentZone.zoneName);
        }
    })
})

function updateItemsInfo(){
    $.ajax({
        url: ZONE_PAGE_URL,
        method: "GET",
        data: {
            reqtype: "all-items"
        },

        //items = [{id: 12, name: "Milki", type:"QUANTITY"/"WEIGHT", numOfStoresSellThisItem: 23, avgPrice: 23.5, totalAmountSoldOnAllStores:123.5}...]
        success: function (items) {
            fetchedItems = items;

            var itemsTableBody = $("#items-table").find("tbody");
            itemsTableBody.empty();

            $.each(items || [], function(index, item) {
                itemsTableBody.append("<tr>" +
                "<td>" + item.id + "</td>" +
                "<td>" + item.name + "</td>" +
                "<td>" + capitalFirst(item.type, true) + "</td>" +
                "<td>" + item.numOfStoresSellThisItem + "</td>" +
                "<td>" + item.avgPrice.toFixed(2) + "</td>" +
                "<td>" + item.totalAmountSoldOnAllStores + "</td>" +
                "</tr>")
            })
        }


    })
};

$(setInterval(updateItemsInfo, 1000));

function createStoreItemShowerButton(store) {
    var showStoreItemsButton = $("<button>Click here to see items</button>").attr("type", "button").
                                    addClass("btn").addClass("btn-outline-info");
    showStoreItemsButton.innerText = "Show items";
    showStoreItemsButton.click(function () {
        var itemsTableHtml = $(" <div class=\"row\">\n" +
            "        <table class=\"table table-hover col-sm-12\">\n" +
            "            <thead>\n" +
            "            <tr>\n" +
            "                <th>Item ID</th>\n" +
            "                <th>Item name</th>\n" +
            "                <th>Weight/Quantity</th>\n" +
            "                <th>Price for unit</th>\n" +
            "                <th>Total sold</th>\n" +
            "            </tr>\n" +
            "            </thead>\n" +
            "            <tbody>\n" +
            "            </tbody>\n" +
            "        </table>\n" +
            "    </div>");

        //storeItem = { id: 12, name: "Milki", type: "WEIGHT"/"QUANTITY", pricePerOne: 23, totalAmountSoldInThisStore: 23.4}
        $.each(store.itemsThatSellInThisStore || [], function (index, storeItem) {
            itemsTableHtml.find("tbody").append("<tr>" +
                "<td>" + storeItem.id + "</td>" +
                "<td>" + storeItem.name + "</td>" +
                "<td>" + capitalFirst(storeItem.type, true) + "</td>" +
                "<td>" + storeItem.pricePerOne + "</td>" +
                "<td>" + storeItem.totalAmountSoldInThisStore + "</td>" +
                "</tr>")

        })
        showModal("Items sold by " + store.name, itemsTableHtml.prop("outerHTML"));
    });

    return showStoreItemsButton;
}

function updateStoresInfo() {
    $.ajax({
        url: ZONE_PAGE_URL,
        method: "GET",
        data: {
            reqtype: "all-stores"
        },

        //stores= [{id: 12, name:"Rami-Levi", ownerName:"Moshe", location: {location: {x:12, y:32}}, [StoreItemDTO], numOfOrdersFromThisStore: 12, totalCostOfSoldItems: 32.4,
                    //ppk: 12, totalCostOfDeliveries:123}...]
        success: function (stores) {
            fetchedStores = stores;
            var storesTableBody = $("#stores-table").find("tbody");
            storesTableBody.empty();

            $.each(stores || [], function(index, store) {
                let storeTableRow = $("<tr>" +
                "<td>" + store.id + "</td>" +
                "<td>" + store.name + "</td>" +
                "<td>" + store.ownerName + "</td>" +
                "<td>(" + store.location.location.x + ", " + store.location.location.y + ")</td>" +
                "<td id='itemsButton" + index + "'></td>" +
                "<td>" + store.numOfOrdersFromThisStore + "</td>" +
                "<td>" + store.totalCostOfSoldItems.toFixed(2) +"</td>" +
                "<td>" + store.ppk +"</td>" +
                "<td>" + store.totalCostOfDeliveries.toFixed(2) +"</td>" +
                "</tr>");
                storesTableBody.append(storeTableRow);
                createStoreItemShowerButton(store).appendTo($("#itemsButton" + index));

                if((userType === "OWNER") && ownerScriptIsLoaded && (userName === store.ownerName)) {
                    addOrderToThisStoreRow(storeTableRow, store);
                }
            })
        }
    })
}

$(setInterval(updateStoresInfo, 1000));

function getScriptsFromServer() {
    if(userType !== undefined) {
        $.ajaxSetup({cache: true});
        if (userType === "OWNER") { //TODO owner stuff - show store owned, show feedback and open new store
            $.getScript(buildUrlWithContextPath("zonePage/ownerInfo.js"));
            ownerScriptIsLoaded = true;
        } else {
            $.getScript(buildUrlWithContextPath("zonePage/customerInfo.js"));
        }

    }
    else {
        setTimeout(getScriptsFromServer, 200);
    }
}

$(getScriptsFromServer());
