var CUSTOMER_ORDER_URL = buildUrlWithContextPath("customerorder");
var typeOfOrder = "STATIC";
var chosenStoreId;

var cart = [];

class ItemInCart {
    constructor(id, amount) {
        this.id = id;
        this.amount = amount;
    }
}


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
    chosenStoreId = parseInt($("#store-chooser option:selected").val());
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
            typeOfOrder = "DYNAMIC";
        }
        else {
            $(".static-order").show(500);
            typeOfOrder = "STATIC";
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
        var queryString =  $("#firstStageForm").serialize().toString() + "&reqtype=inital-order-data";
        $.ajax({
           url: CUSTOMER_ORDER_URL,
           method: "POST",
           data: queryString,

           success: function (response) {
               $("#order-carousel").carousel("next");
               getAllStoreItemData();
           },
           // error: function () {
           //     $("#order-carousel").carousel("next");
           // }
        });

        return false;
    });
})

function validInputForNumber(val, type) {
    if(type === "WEIGHT") {
        return Math.abs(parseFloat(val));
    }
    else {
        return Math.ceil(Math.abs(parseFloat(val)));
    }
}

function createSimpleItemCard(item) {
    return $("<div class=\"card\">" +
        "<div class=\"card-body text-left\">" +
        "<ul>" +
        "<li>Item id: " + item.id + "</li>" +
        "<li>Item name: " + item.name + "</li>" +
        "<li>Type: " + item.type + "</li>" +
        "</div>" +
        "</div>");
}

function updateCart(storeItem, amount) {
    let storeCartItem = cart.find(item => item.id === storeItem.id);
    if(storeCartItem !== undefined && storeCartItem !== null) {
        storeCartItem.amount = amount;
    }
    else {
        cart.push(new ItemInCart(storeItem.id, amount));
    }
}

function createInputAmountLineToStoreItem(storeItem) {
    return "<div class='col-sm-4'>" +
        "<input type='text' class='form-control numeric-only' value='0' id='amountInput"  + storeItem.id + "'>" +
        "</div>";
}

function getAllStoreItemData() {
    $.ajax({
       url: CUSTOMER_ORDER_URL,
       method: "GET",
       data :{
           reqtype : "all-items-for-sale"
       },

        //storeItem = [{ id: 12, name: "Milki", type: "WEIGHT"/"QUANTITY", pricePerOne: 23, totalAmountSoldInThisStore: 23.4}..]
        success: function (storeItems) {
           $.each(storeItems || [], function (index, storeItem) {
               var storeItemCard = createSimpleItemCard(storeItem);
               if(typeOfOrder === "STATIC" && storeItem.pricePerOne !== 0){
                   storeItemCard.find("ul").append("<li>Price: " + storeItem.pricePerOne +"</li>")
               }

               if(storeItem.pricePerOne !==0 || typeOfOrder === "DYNAMIC") {
                   storeItemCard.find("div.card-body").append("<div class='row'>" +
                       "<div class='col-sm-5'>Amount: </div>" +

                       createInputAmountLineToStoreItem(storeItem) +

                       "</div>");

               }else {
                   storeItemCard.find("div.card-body").append("<span style='color:red'>Not sold in this store</span>")
               }
               $("#storeItemsContainer").append(storeItemCard);
               storeItemCard.find("input").each(function () {
                    $(this).change(function () {
                        if(isNaN($(this).val())) {
                            $(this).val(0);
                        }
                        else {
                            $(this).val(validInputForNumber($(this).val(), storeItem.type));

                        }
                        if (parseFloat($(this).val()) > 0) {
                            storeItemCard.addClass("choosed");
                        }
                        else {
                            storeItemCard.removeClass("choosed");
                        }
                        updateCart(storeItem,parseFloat($(this).val()));
                    })
               });

                });
           }
    });
}

$(function () {
    $("#itemChoiseBtn").click(function () {
        $.ajax({
            url: CUSTOMER_ORDER_URL,
            method: "POST",
            data: {
                cart: cart
            },
            success: function () {
                $("#order-carousel").carousel("next");
            }
        });

        return false;
    })
})

//# sourceURL=customerOrder.js