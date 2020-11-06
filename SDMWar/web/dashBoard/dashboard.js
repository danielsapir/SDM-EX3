var DASHBOARD_URL = buildUrlWithContextPath("dashboard");
var userType;

$(function () {
    $.ajax({
        url: DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "user-info"
        },

        //user = {userName:"moshe", userType:"OWNER/CUSTOMER",...}
        success: function (user) {
            $("#user-name-placeholder").text(user.userName);
            userType = user.userType;
            userType = user.userType;
            if(user.userType === "OWNER") {
                $(".customer-only").addClass("invisible");
            }
            else {
                $(".owner-only").addClass("invisible");
            }
        }
    })
})

function allUserUpdate() {
    $.ajax({
        url: DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "all-users"
        },

        //users = [{userName:"moshe", userType:"OWNER/CUSTOMER",...} , {userName:"daniel", userType:"OWNER/CUSTOMER",...}...]
        success: function (users) {
            $("#usersPlaceHolder").empty();
            users.each([] || users, function (index, user) {
                $("#usersPlaceHolder").append("<li><ul><li>" + user.userName + "</li><li>" + user.userType + "</li></ul></li>");
            })
        }
    })
}

$(setInterval(allUserUpdate, 1000));

function updateZonesData() {
    $.ajax({
        url:DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "all-zones"
        },

        //zones = [{owner:"menashe", zoneName:"tel-aviv", itemsNumber: 7, storesNumber: 12, ordersNumber: 1, orderPriceAvg: 24.2}...]
        success: function (zones) {
            var zoneTableBody = $("#zones-table").find("tbody");
            zoneTableBody.empty();

            zones.each([] || zones, function (index, zone) {
                var newTableRow = $(("<tr data-value='" + zone.zoneName +"'>" +
                    "<td>" + zone.owner + "</td>" +
                    "<td>" + zone.zoneName + "</td>" +
                    "<td>" + zone.itemsNumber + "</td>" +
                    "<td>" + zone.storesNumber + "</td>" +
                    "<td>" + zone.ordersNumber + "</td>" +
                    "<td>" + zone.orderPriceAvg + "</td>" +
                    "</tr>"));

                newTableRow.click(function () {
                          var rowName = $(this).data('value');

                          $.ajax({
                              url: DASHBOARD_URL,
                              url: "GET",
                              data: {
                                  reqtype: "to-zone",
                                  currentZone: rowName
                              },
                              success: function (newPageURI) {
                                window.location.assign(window.location.hostname + buildUrlWithContextPath(newPageURI));
                              }
                          })
                    }
                );

                zoneTableBody.append(newTableRow);
            })
        }
    })
}

$(setInterval(updateZonesData, 1000));

function updateMoneyAmount() {
    $.ajax({
        url:DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "money-amount"
        },

        success: function (amount) {
            $("#moneyPlaceHolder").text(amount);
        },
        error: function () {

        }
    })
}

$(setInterval(updateMoneyAmount, 1000));

function updateTransactions() {
    $.ajax({
        url: DASHBOARD_URL,
        method: "GET",
        data: {
            reqtype: "transactions-info"
        },

        //transactions = [{transactionType: LoadingMoney, dateMade: 5/11/2020, amountOfAction: 23.4, amountBeforeOperation : 23.4 , amountAfterOperation: 23.4}...]
        success: function (transactions) {

            var transactionsTableBody = $("#transactions-table").find("tbody");
            transactionsTableBody.empty();

            transactions.each([] || transactions, function (index, transaction) {
                transactionsTableBody.append("<tr>" +
                    "<td>" + transaction.transactionType +  "</td>" +
                    "<td>" + transaction.dateMade + "</td>" +
                    "<td>" + transaction.amountOfAction + "</td>" +
                    "<td>" + transaction.amountBeforeOperation + "</td>" +
                    "<td>" + transaction.amountAfterOperation + "</td>" +
                    "</tr>")
            });
        }
    })
}

$(setInterval(updateTransactions, 1000));

$(function () {

    var depositForm = $("#deposit-form");

    depositForm.submit(function () {
        $.ajax({
            url: DASHBOARD_URL,
            method: "POST",
            data: depositForm.serialize() + "&reqtype=deposit-money",
            success: function () {
                showModal("Success!","Your money had been deposited succefully!");
            },
            error: function () {
                showModal("Failed!", "We could'nt deposit your money please try again later!");
            }
        })
        return false;
        })
})

$(function () {
    var date = new Date();
    var dateStr =date.getFullYear() + "-";
    if((date.getMonth() +1) < 10) {
        dateStr += "0";
    }
    dateStr += (date.getMonth()+1) + "-";

    if(date.getDate() < 10) {
        dateStr += "0";
    }
    dateStr += date.getDate();

    $("#date-input").val(dateStr);
})
