var OWNER_ZONE_INFO_URL = buildUrlWithContextPath("ownerzoneinfo");

function makeOrderItemsTable(order) {
    let orderItemsTable = $(" <div class=\"row\">\n" +
        "        <table class=\"table table-hover col-sm-12\">\n" +
        "            <thead>\n" +
        "            <tr>\n" +
        "                <th>Item ID</th>\n" +
        "                <th>Item name</th>\n" +
        "                <th>Item type</th>\n" +
        "                <th>Amount purchased</th>\n" +
        "                <th>Price per unit</th>\n" +
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
    $.each(order.itemsInOrder || [], function (index, orderItem) {
        orderItemsTable.find("tbody").append("<tr>" +
            "<td>" + orderItem.id + "</td>" +
            "<td>" + orderItem.name + "</td>" +
            "<td>" + capitalFirst(orderItem.type, true) + "</td>" +
            "<td>" + orderItem.amount.toFixed(2) + "</td>" +
            "<td>" + orderItem.pricePerOne + "</td>" +
            "<td>" + ((orderItem.pricePerOne*orderItem.amount).toFixed(2)) + "</td>" +
            "<td>" + (orderItem.isPartOfDiscount ? "Yes &#128516" : "No &#128549") + "</td>" +
            "</tr>")

    });

    return orderItemsTable;
}
// " <div class=\"row\">\n" +
// "        <table class=\"table table-hover col-sm-12\">\n" +
// "            <thead>\n" +
// "            <tr>\n" +
// "                <th>Item ID</th>\n" +
// "                <th>Item name</th>\n" +
// "                <th>Item type</th>\n" +
// "                <th>Amount purchased</th>\n" +
// "                <th>Price per unit</th>\n" +
// "                <th>Total price</th>\n" +
// "                <th>Discounted price?</th>\n" +
// "            </tr>\n" +
// "            </thead>\n" +
// "            <tbody>\n" +
// "            </tbody>\n" +
// "        </table>\n" +
// "    </div>"

function makeStoreOrdersTable(store) {
    let storeOrdersTable = $(" <div class=\"row table-responsive\">\n" +
        "        <table class=\"table table-hover col-sm-12\">\n" +
        "            <thead>\n" +
        "            <tr>\n" +
        "                <th>Orders ID</th>\n" +
        "                <th>Order date</th>\n" +
        "                <th>Customer name - location</th>\n" +
        "                <th>Total amount of items</th>\n" +
        "                <th>Total price of items</th>\n" +
        "                <th>Delivery price</th>\n" +
        "            </tr>\n" +
        "            </thead>\n" +
        "            <tbody>\n" +
        "            </tbody>\n" +
        "        </table>\n" +
        "    </div>");

    let storeOrderTableBody = storeOrdersTable.find("tbody");

    //oneStoreOrders = [{storeId:2, date:.., customerName:"Mosh", destinationLocation: { location: {x:1,y:2}}, numOfItemsInThisOrder:12, priceOfItemsInThisOrder:123.4
    //                  deliveryPrice:23.4, storeName:"Rami-Levi", distanceToStore: 42.1, ppk:23, numTypesOfItemsInThisOrder:2, storeLocation:{location{x:2,y:4}}}..]
    $.each(store.ordersOfStore || [], function (index, order) {
        let storeOrderRow = $(
            "<tr class='accordion-toggle collapsed' id='accordion" +order.storeId + "' data-toggle='collapse' data-parent='#accordion" +order.storeId + "' href='#collapse" + index + "'>" +
            "<td>" + order.id + "</td>" +
            "<td>" + fixDate(order.date) + "</td>" +
            "<td>" + order.customerName + " - (" + order.destinationLocation.location.x + ", " + order.destinationLocation.location.y + ")</td>" +
            "<td>" + order.numOfItemsInThisOrder + "</td>" +
            "<td>" + (order.priceOfItemsInThisOrder.toFixed(2)) + "</td>" +
            "<td>" + (order.deliveryPrice.toFixed(2)) +"</td>" +
            "</tr>");

        let orderItemsTable = makeOrderItemsTable(order);
        let collapse = $("<tr class='hide-table-padding'>\n" +
            "<td></td>\n" +
            "<td colspan='8'>\n" +
            "<div id='collapse" + index + "' class='collapse in p-3'>" +
            "</div></td>\n" +
            "</tr>");
        collapse.find("div.collapse").append(orderItemsTable);

        storeOrderTableBody.append(storeOrderRow.add(collapse));
    });

    return storeOrdersTable;
}

function addOrderToThisStoreRow(storeTableRow, store) {

    storeTableRow.addClass("table-success");
    storeTableRow.css("cursor","pointer");

    storeTableRow.click(function (event) {
        if(!($(event.target).is(storeTableRow.find("button")))) {
            showModal("Order of store " + store.name, makeStoreOrdersTable(store).prop("outerHTML"));
        }
    });

}

function showFeedbacks() {
    $.ajax({
        url: OWNER_ZONE_INFO_URL,
        method: "GET",
        data: {
            reqtype: "get-all-feedbacks"
        },

        //feedbacks = [{customerName:"David", date:"Nov 12...", rate: 1/2/3/4/5, description: "very good..."}...]
        success: function (feedbacks) {
            let feedbackTableBody = $("#feedback-table").find("tbody");
            feedbackTableBody.empty();

            $.each(feedbacks || [], function (index, feedback) {
                let feedbackDescription = feedback.description === "" ? "No description" : feedback.description;
                let feedbackTableRow = $(
                    "<tr class='d-flex'>" +
                    "<td class='col-3'>" + feedback.customerName + "</td>" +
                    "<td class='col-2'>" + fixDate(feedback.date) + "</td>" +
                    "<td class='col-2'>" + feedback.rate + "</td>" +
                    "<td class='col-5'>" + feedbackDescription + "</td>" +
                    "</tr>");

               feedbackTableBody.append(feedbackTableRow);
            });
        }
    })
}

$(setInterval(showFeedbacks, 1000));

//# sourceURL=ownerInfo.js