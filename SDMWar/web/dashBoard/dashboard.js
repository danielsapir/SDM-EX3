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

            if(user === null) {
                window.location.replace(buildUrlWithContextPath("login/login.html"));
            }

            $("#user-name-placeholder").text(user.userName);
            userType = user.userType;
            if(user.userType === "OWNER") {
                $(".customer-only").remove();
            }
            else {
                $(".owner-only").remove();
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
            $.each(users || [], function (index, user) {
                $("#usersPlaceHolder").append("<li><ul><li>" + user.userName + "</li><li>" + capitalFirst(user.userType, true) + "</li></ul></li>");
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

        //zones = [{ownerName:"menashe", zoneName:"tel-aviv", itemsNumber: 7, storesNumber: 12, ordersNumber: 1, orderPriceAvg: 24.2}...]
        success: function (zones) {
            var zoneTableBody = $("#zones-table").find("tbody");
            zoneTableBody.empty();

            $.each(zones || [], function (index, zone) {
                var newTableRow = $(("<tr data-value='" + zone.zoneName +"'>" +
                    "<td>" + zone.ownerName + "</td>" +
                    "<td>" + zone.zoneName + "</td>" +
                    "<td>" + zone.itemsNumber + "</td>" +
                    "<td>" + zone.storesNumber + "</td>" +
                    "<td>" + zone.ordersNumber + "</td>" +
                    "<td>" + zone.orderPriceAvg.toFixed(2) + "</td>" +
                    "</tr>"));

                newTableRow.click(function () {
                          var rowName = $(this).data('value');

                          $.ajax({
                              url: DASHBOARD_URL,
                              method: "GET",
                              data: {
                                  reqtype: "to-zone",
                                  currentZone: rowName
                              },
                              success: function (response) {
                                window.location.replace(window.location.origin + buildUrlWithContextPath("zonePage/zonePage.html"));
                              }
                          });
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
            $("#moneyPlaceHolder").text(amount.toFixed(2));
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

            $.each(transactions || [], function (index, transaction) {

                transactionsTableBody.append("<tr>" +
                    "<td>" + capitalFirst(decamelize(transaction.type, " "), true) +  "</td>" +
                    "<td>" + fixDate(transaction.date) + "</td>" +
                    "<td>" + transaction.amountOfAction.toFixed(2) + "</td>" +
                    "<td>" + transaction.amountBeforeOperation.toFixed(2) + "</td>" +
                    "<td>" + transaction.amountAfterOperation.toFixed(2) + "</td>" +
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
    $("#transactionAmountInput").change(function () {
        if(isNaN($("#transactionAmountInput").val())) {
            $("#transactionAmountInput").val(0);

        }
        else {
            $("#transactionAmountInput").val(Math.abs(parseFloat($("#transactionAmountInput").val())));
        }
    });
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

$(function() {
    $("input:file").change(function (){
        var fileName = $(this).val();
        if(fileName !== undefined && fileName !== null) {
            $("#fileUploadSubmit").removeAttr("disabled");
        }

    });
});

$(function() { // onload...do
    $("#uploadForm").submit(function() {

        var file = this[0].files[0];
        var formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: DASHBOARD_URL,
            method: "PUT",
            data: formData,
            processData: false,
            contentType: false,

            //res = {succeeded: true/false,  message:"Error.."}
            success: function(res) {
                if(res.succeeded) {
                    showModal("File has been loaded!", "The file had been loaded successfully!");
                }
                else {
                    showModal("Error! File could not be loaded!", res.message);
                }
            }
        });

        return false;
    })
})