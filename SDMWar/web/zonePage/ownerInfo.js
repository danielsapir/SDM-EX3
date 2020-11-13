var OWNER_ZONE_INFO_URL = buildUrlWithContextPath("ownerzoneinfo");

function makeOrderItemsTable(order) {
    let orderItemsTable = $(" <div class=\"row\">\n" +
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

function addOrderToThisStoreRow(storeTableRow, store) {

    storeTableRow.addClass("table-success");
    storeTableRow.css("cursor","pointer");

    let storeOrdersTable = $(" <div class=\"row\">\n" +
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

    let storeOrderTableBody = storeOrdersTable.find("tbdoy");

    //oneStoreOrders = [{storeId:2, date:.., customerName:"Mosh", destinationLocation: { location: {x:1,y:2}}, numOfItemsInThisOrder:12, priceOfItemsInThisOrder:123.4
    //                  deliveryPrice:23.4, storeName:"Rami-Levi", distanceToStore: 42.1, ppk:23, numTypesOfItemsInThisOrder:2, storeLocation:{location{x:2,y:4}}}..]
    $.each(store.ordersOfStore || [], function (index, order) {
        let storeOrderRow = $("<tr>" +
        "<td>" + order.id + "</td>" +
        "<td>" + order.date + "</td>" +
        "<td>" + order.customerName + "-(" + order.orderDestination.location.x + ", " + order.orderDestination.location.y + ")</td>" +
        "<td>" + order.numOfItemsInThisOrder + "</td>" +
        "<td>" + order.priceOfAllItemsInThisOrder.toFixed(2) + "</td>" +
        "<td>" + order.priceOfAllDeliveriesInThisOrder.toFixed(2) +"</td>" +
        "</tr>");

        storeOrderRow.click(function () {
            let orderItemsTable = makeOrderItemsTable(order);
            showModal("Items of order number " + order.id, orderItemsTable.prop("outerHTML"));
        });

    });

    storeTableRow.click(function () {
       showModal("Order of store" + store.name, storeOrdersTable.prop("outerHTML"));
    });
}