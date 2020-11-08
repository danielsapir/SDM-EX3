var ZONE_PAGE_URL = buildUrlWithContextPath("zonepage");

$(function () {
    $.ajax({
        url: ZONE_PAGE_URL,
        method: "GET",
        data: {
            reqtype: "zone-info"
        },

        //currentZone = {ownerName: "Moshe", zoneName: "Tel-Aviv", itemNumber: 7, storesNumber: 12, ordersNumber: 32, orderPriceAvg: 23.53
        success: function (currentZone) {
            $("#zoneName").innerText = response.zoneName;
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

        //items = [{
        success: function (items) {
            var itemsTableBody = $("#items-table").find("tbody");
            itemsTableBody.empty();

            $.each(items || [], function(index, item) {
                itemsTableBody.append("<tr>" +
                "<td></td>")
            })
        }

//                transactionsTableBody.append("<tr>" +
//                     "<td>" + capitalFirst(decamelize(transaction.type, " "), true) +  "</td>" +
//                     "<td>" + transaction.date + "</td>" +
//                     "<td>" + transaction.amountOfAction + "</td>" +
//                     "<td>" + transaction.amountBeforeOperation + "</td>" +
//                     "<td>" + transaction.amountAfterOperation + "</td>" +
//                     "</tr>")
//             });
    })
};

$(setInterval(updateItemsInfo, 1000));

function createStoreItemShowerButton(store) {
    var showStoreItemsButton = $("<button></button>").attr("type", "button").
                                    addClass("btn").addClass("btn-outline-info");
    showStoreItemsButton.innerText = "Show items";
    showStoreItemsButton.click(function () {
        var itemsTableHtml = " <div class=\"row\">\n" +
            "        <table class=\"table table-hover col-sm-12\">\n" +
            "            <thead>\n" +
            "            <tr>\n" +
            "                <th>Item ID</th>\n" +
            "                <th>Item name</th>\n" +
            "                <th>Weight/Quantity</th>\n" +
            "                <th>Number of stores sold by</th>\n" +
            "                <th>Average price</th>\n" +
            "                <th>Total sold</th>\n" +
            "            </tr>\n" +
            "            </thead>\n" +
            "            <tbody>\n" +
            "            </tbody>\n" +
            "        </table>\n" +
            "    </div>"
        showModal("Items sold by " + store.name, )
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

        //stores= [{
        success: function (stores) {
            var storesTableBody = $("#stores-table").find("tbody");
            storesTableBody.empty();

            $.each(stores || [], function(index, store) {
                storesTableBody.append("<tr>" +
                "<td></td>" + 
                "<td></td>" +
                "<td></td>" +
                "<td></td>" +
                "<td></td>" +
                "<td id='itemsButton'></td>" +
                "<td></td>" +
                "<td></td>" +
                "<td></td>" +
                "</tr>");
                
                createStoreItemShowerButton(store).appendTo($("#itemsButton"));

            })
        }
    })
}
// var zoneTableBody = $("#zones-table").find("tbody");
//             zoneTableBody.empty();
//
//             $.each(zones || [], function (index, zone) {
//                 var newTableRow = $(("<tr data-value='" + zone.zoneName +"'>" +
//                     "<td>" + zone.owner + "</td>" +
//                     "<td>" + zone.zoneName + "</td>" +
//                     "<td>" + zone.itemsNumber + "</td>" +
//                     "<td>" + zone.storesNumber + "</td>" +
//                     "<td>" + zone.ordersNumber + "</td>" +
//                     "<td>" + zone.orderPriceAvg + "</td>" +
//                     "</tr>"));
//
//                 newTableRow.click(function () {
//                           var rowName = $(this).data('value');
//
//                           $.ajax({
//                               url: DASHBOARD_URL,
//                               method: "GET",
//                               data: {
//                                   reqtype: "to-zone",
//                                   currentZone: rowName
//                               },
//                               success: function (newPageURI) {
//                                 window.location.assign(window.location.origin + buildUrlWithContextPath(newPageURI));
//                               }
//                           })
//                     }
//                 );
//
//                 zoneTableBody.append(newTableRow);
//             })