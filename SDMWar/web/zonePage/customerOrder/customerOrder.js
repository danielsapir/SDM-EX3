var CUSTOMER_ORDER_URL = buildUrlWithContextPath("customerorder");

function isUserCustomer() {
    return (userType === "CUSTOMER");
}

$(function () {
    $("#order-carousel").carousel({interval: false});
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

    $("#date-input-order").val(dateStr);
})

function calculateDeliveryPrice(ppk, location) {
    var x1 = location.location.x;
    var y1 = location.location.y;
    var x2 = parseInt($("#xCorPicker").val());
    var y2 = parseInt($("#yCorPicker").val());
    var a = x2-x1;
    var b = y2-y1;
    var distance = Math.sqrt(Math.abs(a*a + b*b));

    return ppk*distance;

}

$(function () {
    $.each(fetchedStores || [], function (index, store) {
        var optionOfStore = $("<option value='" + store.id +"'>"
            + store.name +
            "</option>");
        if(index === 0) {
            optionOfStore.attr("selected", "selected");
        }
        $("#store-chooser").append(optionOfStore);
    });
    assignDeliveryPrice();
});

function checkIfStoreLocationIsOk(store) {
    var currentX = parseInt($("#xCorPicker").val());
    var currentY = parseInt($("#yCorPicker").val());
    if(store.location.location.x === currentX && store.location.location.y === currentY){
        $("#deliveryPriceHolder").text("You cant choose location of existing store").addClass("errorText");
        $("#firstStepBtn").attr("disabled","disabled");
        return false;
    }
    else {
        $("#deliveryPriceHolder").removeClass("errorText");
        $("#firstStepBtn").removeAttr("disabled","disabled");
        return true;
    }
}

function assignDeliveryPrice() {
    var chosenStoreId = parseInt($("#store-chooser option:selected").val());
    $.each(fetchedStores || [], function (index, store) {
        if(store.id === chosenStoreId) {
            if(checkIfStoreLocationIsOk(store)) {
                var deliveryPrice = calculateDeliveryPrice(store.ppk, store.location);
                $("#deliveryPriceHolder").text(deliveryPrice);
            }
        }
    });
}

$(function () {
    $("#store-chooser").change(function () {
        var chosenStoreId = parseInt($("#store-chooser option:selected").val());
        assignDeliveryPrice();
    });
});

$(function () {
    $("#dynamicOrderButton, #staticOrderButton").change(function () {
        if($("#dynamicOrderButton").is(":checked")) {
            $(".static-order").hide(500);
        }
        else {
            $(".static-order").show(500);
        }
    });
})

$(function () {
    $("#xCorPicker, #yCorPicker").change(function () {
        assignDeliveryPrice();
    })
})

$(function () {
    $("#firstStepBtn").click(function () {
        $.ajax({
           url: CUSTOMER_ORDER_URL,
           method: "POST",
           data: $("#firstStageForm").serialize() + "&reqtype=inital-order-data",
           success: function (response) {
               $("order-carousel").carousel("next");
           },
           error: function () {
               $("order-carousel").carousel("next");
           }
        });
    });
})

$(function () {
    $.ajax({
       url: CUSTOMER_ORDER_URL,
       method: "GET",
       data :{
           reqtype : "all-items-for-sale"
       },

        //storeItem = [{ id: 12, name: "Milki", type: "WEIGHT"/"QUANTITY", pricePerOne: 23, totalAmountSoldInThisStore: 23.4}..]
        success: function (storeItems) {

        }
    });
})

//# sourceURL=customerOrder.js