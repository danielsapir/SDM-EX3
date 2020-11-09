var CUSTOMER_ZONE_INFO_URL = buildUrlWithContextPath("customerzoneinfo");

$(function () {
    $("#orderButton").click(function () {
        $("#cart").animate({left:($("#orderDiv").width() - $("#orderButton").width() - 200) + "px"});
    });
})

function updateOrdersInfo() {
    $.ajax({
        url: CUSTOMER_ZONE_INFO_URL,
        method: "GET",
        data: {
            reqtype: "all-customer-orders"
        },

        success: function (orders) {
            var ordersTableBody = $("#customer-orders-table").find("tbody");
            ordersTableBody.empty();

            //order = {id: 12, date:"12, nov..", orderDestination: {location :{x:12, y:23}}, numOfStoresInThisOrder:12, numOfItemsInThisOrder:43,
            //            priceOfAllItemsInThisOrder: 32.4, priceOfAllDeliveriesInThisOrder: 43.5, totalPrice: 75.9}
            $.each(orders || [], function(index, order) {
                let customerOrderRow = $("<tr>" +
                    "<td>" + order.id + "</td>" +
                    "<td>" + order.date + "</td>" +
                    "<td>(" + order.orderDestination.location.x + ", " + order.orderDestination.location.y + ")</td>" +
                    "<td>" + order.numOfStoresInThisOrder + "</td>" +
                    "<td>" + order.numOfItemsInThisOrder + "</td>" +
                    "<td>" + order.priceOfAllItemsInThisOrder + "</td>" +
                    "<td>" + order.priceOfAllDeliveriesInThisOrder +"</td>" +
                    "<td>" + order.totalPrice +"</td>" +
                    "</tr>");

                customerOrderRow.click(function () {
                    var orderItemsTableHtml = $(" <div class=\"row\">\n" +
                        "        <table class=\"table table-hover col-sm-12\">\n" +
                        "            <thead>\n" +
                        "            <tr>\n" +
                        "                <th>Item ID</th>\n" +
                        "                <th>Item name</th>\n" +
                        "                <th>Weight/Quantity</th>\n" +
                        "                <th>Store ID - Store name</th>\n" +
                        "                <th>Number of units</th>\n" +
                        "                <th>Price for unit</th>\n" +
                        "                <th>Total price</th>\n" +
                        "                <th>Discount price?</th>\n" +
                        "            </tr>\n" +
                        "            </thead>\n" +
                        "            <tbody>\n" +
                        "            </tbody>\n" +
                        "        </table>\n" +
                        "    </div>");

                    //TODO orderItem = {...}
                    $.each(order.itemsThatSellInThisStore || [], function (index, orderItem) {
                        orderItemsTableHtml.find("tbody").append("<tr>" +
                            "<td>" + orderItem.id + "</td>" +
                            "<td>" + orderItem.name + "</td>" +
                            "<td>" + capitalFirst(orderItem.type, true) + "</td>" +
                            "<td>" + orderItem.name + "</td>" +
                            "<td>" + storeItem.pricePerOne + "</td>" +
                            "<td>" + storeItem.totalAmountSoldInThisStore + "</td>" +
                            "</tr>")

                    })
                    showModal("Items in order:", orderItemsTableHtml.prop("outerHTML"));
                });

            })
        }
    })
}

$(setInterval(updateOrdersInfo, 1000));