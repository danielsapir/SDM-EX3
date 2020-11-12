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
    for(let i=0; i<fetchedStores.length; i++) {
        if(fetchedStores[i].location.location.x === currentX && fetchedStores[i].location.location.y === currentY) {
            $("#deliveryPriceHolder").text("");
            $("#locationErrorPlaceHolder").text("You cant choose location of existing store").addClass("errorText");
            $("#firstStepBtn").attr("disabled", "disabled");
            return false;
        }
    }
    $("#locationErrorPlaceHolder").text("").addClass("errorText");
    $("#deliveryPriceHolder").removeClass("errorText");
    $("#firstStepBtn").removeAttr("disabled","disabled");
    return true;

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
                            storeItemCard.addClass("border-success");
                        }
                        else {
                            storeItemCard.removeClass("border-success");
                        }
                        updateCart(storeItem,parseFloat($(this).val()));
                    })
               });

                });
           }
    });
}

function showDynamicOrderMidOffer() {
    $.ajax({
        url: CUSTOMER_ORDER_URL,
        method: "GET",
        data: {
            reqtype: "dynamic-order-mid-info",
        },


        //oneStoreOrders = [{id:2, date:.., customerName:"Mosh", destinationLocation: { location: {x:1,y:2}}, numOfItemsInThisOrder:12, priceOfItemsInThisOrder:123.4
        //                  deliveryPrice:23.4, storeName:"Rami-Levi", distanceToStore: 42.1, ppk:23, numTypesOfItemsInThisOrder:2, storeLocation:{location{x:2,y:4}}}..]
        success: function (oneStoreOrders) {
            let dynamicMidTableBody = $("#dynamic-mid-order-table").find("tbody");

            $.each(oneStoreOrders || [], function (index, oneStoreOrder) {
                dynamicMidTableBody.append("<tr>" +
                    "<td>" + oneStoreOrder.id + "</td>" +
                    "<td>" + oneStoreOrder.storeName + "</td>" +
                    "<td>(" + oneStoreOrder.storeLocation.location.x + ", " + oneStoreOrder.storeLocation.location.y + ")</td>" +
                    "<td>" + oneStoreOrder.distanceToStore + "</td>" +
                    "<td>" + oneStoreOrder.ppk + "</td>" +
                    "<td>" + oneStoreOrder.deliveryPrice + "</td>" +
                    "<td>" + oneStoreOrder.numTypesOfItemsInThisOrder + "</td>" +
                    "<td>" + (oneStoreOrder.priceOfItemsInThisOrder + oneStoreOrder.deliveryPrice)  + "</td>" +
                    "</tr>");
            });

            $("#dynamicMidButton").click(function () {
                showDiscountsOfOrder();
                $("#order-carousel").carousel("next");
                return false;
            });
        }
    });
}

function addDiscountToOrder(discount, offerContainer, discountCard) {
    let idOfDiscount = discount.discountId;
    let idOfOffer = 0;
    if(discount.thenGetDTO.operator === "ONE-OF") {
        idOfOffer = offerContainer.find("option:selected").val();
    }
    $.ajax({
        url: CUSTOMER_ORDER_URL,
        method: "POST",
        data: {
            reqtype: "add-discount",
            discountId: idOfDiscount,
            offerId: idOfOffer
        },
        success: function (response) {
            discountCard.hide(400);
        }
    })
}

function showDiscountsOfOrder() {
    $.ajax({
        url: CUSTOMER_ORDER_URL,
        method: "GET",
        data: {
            reqtype: "get-discounts"
        },


        //discounts= [{discountName: "1+1", discountId :3, ifBuyDTO { itemName:"Milk", quantity:2 },
        //              thenGetDTO {operator:"ONE-OF", offerDTOList: [{offerId: 12, itemId: 4, itemName: "Milk", amount:23.4, forAdditionalPrice:3}..]}
        success: function (discounts) {
            let discountContainer = $("#discountContainer");
            $.each(discounts || [], function (index, discount) {
                let discountCard = $(
                    "<div class='card border-dark'>" +
                        "<div class='card-header row'>" +
                            "<div class='col-sm-12'>" +
                                "<p>&#x2B50<strong>" + discount.discountName + "</strong>&#x2B50</p>" +
                            "</div>" +
                        "</div>" +
                        "<div class='card-body text-center'>" +
                            "<div class='container-fluid'>" +
                                "<div class='row'>" +
                                    "<div class='col-sm-12'>" +
                                        "<p>Because you bought " +  discount.ifBuyDTO.quantity + " " + discount.ifBuyDTO.itemName + "</p>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='row'>" +
                                    "<div class='col-sm 12'>" +
                                        "<p>Then you can get " + (discount.thenGetDTO.operator !== "IRRELEVANT" ? discount.thenGetDTO.operator : "")+ "</p>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='row'>" +
                                    "<div class='col-sm-12'>" +
                                        "<select class='form-control' id='offers-container' name='offerChoose'>" +
                                        "</select>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='row invisible' id='forPartOfDiscount'>" +
                                    "<div class='col-sm-12'>" +
                                        "<p>for: <span id='totalPricePlaceHolder'></span></p>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='row'>" +
                                    "<div class='col-sm-12'>" +
                                        "<button class='btn btn-outline-primary' id='offerChoosedBtn'>I want it!</button>" +
                                    "</div>" +
                                "</div>" +
                            "</div>" +
                        "</div>" +
                    "</div>");

                let offerContainer = discountCard.find("#offers-container");
                $.each(discount.thenGetDTO.offerDTOList || [], function (index, offer) {
                    offerContainer.append("<option value='" + offer.offerId + "'>"+
                        offer.amount + " " + offer.itemName + (discount.thenGetDTO.operator === "ONE-OF" ? " for " + offer.forAdditionalPrice : "") +
                        "</option>")
                });
                if(discount.thenGetDTO.operator !== "ONE-OF") {
                    discountCard.find("#forPartOfDiscount").removeClass("invisible");
                    let totalPrice = 0;
                    for(let i=0; i<discount.thenGetDTO.offerDTOList.length; i++) {
                        totalPrice += (discount.thenGetDTO.offerDTOList)[i].forAdditionalPrice;
                    }

                    discountCard.find("#totalPricePlaceHolder").text(totalPrice);
                    offerContainer.attr("multiple");
                    offerContainer.attr("disabled");
                }

                discountCard.find("#offerChoosedBtn").click(function () {
                    addDiscountToOrder(discount, offerContainer, discountCard);
                    return false;
                });

                discountContainer.append(discountCard);

            })
        }
    })
}

$(function () {
    $("#itemChoiseBtn").click(function () {
        $.ajax({
            url: CUSTOMER_ORDER_URL,
            method: "POST",
            data: {
                reqtype: "add-items-to-order",
                cart: JSON.stringify(cart)
            },
            success: function (response) {

                if(typeOfOrder === "DYNAMIC") {
                    showDynamicOrderMidOffer();
                    $("#order-carousel").carousel(2);
                }
                else {
                    showDiscountsOfOrder();
                    $("#order-carousel").carousel(3);
                }
            }
        });

        return false;
    })
})

$(function () {
    $("#discountChoiseBtn").click(function () {
        //TODO change to summary
    })
})
//# sourceURL=customerOrder.js