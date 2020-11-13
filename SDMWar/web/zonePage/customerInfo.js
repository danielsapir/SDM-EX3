var CUSTOMER_ZONE_INFO_URL = buildUrlWithContextPath("customerzoneinfo");
var html
$(function () {
    $("#orderButton").click(function () {
        $(this).addClass("disabled");
        $(this).attr("disabled", "disabled");
        $("#cart").animate({left:($("#orderDiv").width() - $("#orderButton").width() - $("#cart").width()-20) + "px"});
        $.get(buildUrlWithContextPath("zonePage/customerOrder/customerOrder.html"),function(htmlFile) {
            $("#orderOperationPlaceHolder").html(htmlFile).hide();
            $("#orderOperationPlaceHolder").show(1000);
            $.getScript(buildUrlWithContextPath("zonePage/customerOrder/customerOrder.js"));
        });
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
            //            priceOfAllItemsInThisOrder: 32.4, priceOfAllDeliveriesInThisOrder: 43.5, totalPrice: 75.9,
            //            itemsInThisOrder: [{id: 12, name:"Banana", type: "WEIGHT"/"QUANTITY", storeName: "Hazi-Hinam", storeId: 32,
            //                                  amount: 23.4, pricePerOne: 23, isPartOfDiscount: true/false}...]
            $.each(orders || [], function(index, order) {
                let customerOrderRow = $("<tr>" +
                    "<td>" + order.id + "</td>" +
                    "<td>" + order.date + "</td>" +
                    "<td>(" + order.orderDestination.location.x + ", " + order.orderDestination.location.y + ")</td>" +
                    "<td>" + order.numOfStoresInThisOrder + "</td>" +
                    "<td>" + order.numOfItemsInThisOrder + "</td>" +
                    "<td>" + order.priceOfAllItemsInThisOrder.toFixed(2) + "</td>" +
                    "<td>" + order.priceOfAllDeliveriesInThisOrder.toFixed(2) +"</td>" +
                    "<td>" + order.totalPrice.toFixed(2) +"</td>" +
                    "</tr>");

                customerOrderRow.click(function () {
                    var orderItemsTableHtml = $(" <div class=\"row\">\n" +
                        "        <table class=\"table table-hover col-sm-12\">\n" +
                        "            <thead>\n" +
                        "            <tr>\n" +
                        "                <th>Item ID</th>\n" +
                        "                <th>Item name</th>\n" +
                        "                <th>Weight/Quantity</th>\n" +
                        "                <th>Store ID - name</th>\n" +
                        "                <th>Number of units</th>\n" +
                        "                <th>Price for unit</th>\n" +
                        "                <th>Total price</th>\n" +
                        "                <th>Discounted price?</th>\n" +
                        "            </tr>\n" +
                        "            </thead>\n" +
                        "            <tbody>\n" +
                        "            </tbody>\n" +
                        "        </table>\n" +
                        "    </div>");

                    //orderItem = {id: 12, name:"Banana", type: "WEIGHT"/"QUANTITY", storeName: "Hazi-Hinam", storeId: 32,
                    //              amount: 23.4, pricePerOne: 23, isPartOfDiscount: true/false}
                    $.each(order.itemsInThisOrder || [], function (index, orderItem) {
                        orderItemsTableHtml.find("tbody").append("<tr>" +
                            "<td>" + orderItem.id + "</td>" +
                            "<td>" + orderItem.name + "</td>" +
                            "<td>" + capitalFirst(orderItem.type, true) + "</td>" +
                            "<td>" + orderItem.storeId + "-" + orderItem.storeName + "</td>" +
                            "<td>" + orderItem.amount.toFixed(2) + "</td>" +
                            "<td>" + orderItem.pricePerOne + "</td>" +
                            "<td>" + ((orderItem.pricePerOne*orderItem.amount).toFixed(2)) + "</td>" +
                            "<td>" + (orderItem.isPartOfDiscount ? "Yes &#128516" : "No &#128549") + "</td>" +
                            "</tr>")

                    })
                    showModal("Items in order:", orderItemsTableHtml.prop("outerHTML"));
                });

                ordersTableBody.append(customerOrderRow);
            })
        }
    })
}

$(setInterval(updateOrdersInfo, 1000));

//# sourceURL=customerInfo.js