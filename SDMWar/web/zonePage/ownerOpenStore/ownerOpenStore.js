var OPEN_STORE_URL = buildUrlWithContextPath("opennewstore");
var itemsForNewStore = [];
var storeNameInputIsOk = false;
var storeLocationInputIsOk = false;

class ItemForNewStore {
    constructor(id, price) {
        this.id = id;
        this.price = price;
    }
}

$(function () {
    $("#open-store-carousel").carousel({interval: false});
})

$(function () {
    let storeNameInput =  $("#storeNameInput");
    storeNameInput.change(function () {
        storeNameInput.val($.trim(storeNameInput.val()));
        if(storeNameInput.val().length > 0) {
            storeNameInputIsOk = true;
        }
        else{
            $("#firstStepBtn").attr("disabled", "disabled");
            storeNameInputIsOk = false;
        }

        tryToEnableFirstContinueBtn();
    })
})

function tryToEnableFirstContinueBtn() {
    if(storeLocationInputIsOk && storeNameInputIsOk) {
        $("#firstStepBtn").removeAttr("disabled");
    }
}

function checkIfStoreLocationIsOk() {
    var currentX = parseInt($("#xCorPicker").val());
    var currentY = parseInt($("#yCorPicker").val());
    for(let i=0; i<fetchedStores.length; i++) {
        if((fetchedStores[i].location.location.x === currentX) && (fetchedStores[i].location.location.y === currentY)) {
            $("#firstStepBtn").attr("disabled", "disabled");
            $("#locationErr").removeClass("invisible");
            storeLocationInputIsOk = false;
            return false;
        }
    }
    $("#locationErr").addClass("invisible");
    storeLocationInputIsOk = true;
    tryToEnableFirstContinueBtn();
    return true;

}

$(function () {
    $("#xCorPicker, #yCorPicker").change(function () {
        checkIfStoreLocationIsOk();
    });

    checkIfStoreLocationIsOk();
})

$(function () {
    $("#firstStepBtn").click(function () {
        $("#open-store-carousel").carousel("next");
        return false;
    });
})

function validInputForPrice(val) {
    return Math.ceil(Math.abs(parseFloat(val)));
}

function updateItemForStore(item, price) {
    let itemForSale = itemsForNewStore.find(itemInArr => itemInArr.id === item.id);
    if(itemForSale !== undefined && itemForSale !== null) {
        if(price === 0) {
            let tempArr =[];
            for(let i = 0; i < itemsForNewStore.length ; i++) {
                if(itemsForNewStore[i].id !== item.id) {
                    tempArr.push(itemsForNewStore[i]);
                }
            }

            itemsForNewStore = tempArr;
        }
        else {
            itemForSale.price = price;
        }
    }
    else {
        itemsForNewStore.push(new ItemForNewStore(item.id, price));
    }
}

function createSimpleItemCard(item) {
    return $(
        "<div class=\"card\">" +
            "<div class=\"card-body text-left\">" +
                "<ul>" +
                    "<li>Item id: " + item.id + "</li>" +
                    "<li>Item name: " + item.name + "</li>" +
                    "<li>Type: " + item.type + "</li>" +
            "<div class='row'>" +
                "<div class='col-sm-5'>" +
                    "<p>Price: </p>" +
                "</div>" +
                "<div class='col-sm-7'>" +
                    "<input type='text' class='form-control numeric-only' value='0' id='priceInput"  + item.id + "'>" +
                "</div>" +
            "</div>" +
        "</div>");
}

function updateOpenButtonByNumOfSelectedItems() {
    if(itemsForNewStore.length > 0) {
        $("#acceptOpenStoreBtn").removeAttr("disabled");
    }
    else {
        $("#acceptOpenStoreBtn").attr("disabled", "disabled");
    }
}

$(function () {
    $.each(fetchedItems || [], function (index, item) {
        let itemCard = createSimpleItemCard(item);
        $("#itemsContainer").append(itemCard);
        itemCard.find("input").each(function () {
            $(this).change(function () {
                if(isNaN($(this).val())) {
                    $(this).val(0);
                }
                else {
                    $(this).val(validInputForPrice($(this).val()));

                }
                if (parseFloat($(this).val()) > 0) {
                    itemCard.addClass("border-success");
                }
                else {
                    itemCard.removeClass("border-success");
                }
                updateItemForStore(item,parseFloat($(this).val()));
                updateOpenButtonByNumOfSelectedItems();
            })
        });
    })
})

$(function () {
    $("#cancelOpenStoreBtn").click(function () {
        location.reload();
    });

    $("#acceptOpenStoreBtn").click(function () {
        $.ajax({
            url: OPEN_STORE_URL,
            method: "POST",
            data: {
                reqtype: "open-store",
                storeName: $("#storeNameInput").val(),
                xCor: $("#xCorPicker").val(),
                yCor: $("#yCorPicker").val(),
                ppk: $("#storePPKInput").val(),
                itemsForStore: JSON.stringify(itemsForNewStore)
            },

            //response = {result: true/false}
            success: function (response) {
                if(response.result === true) {
                    $("#openResultPlaceHolder").append($("  <div class=\"alert alert-success\">\n" +
                        "    <strong>Success!</strong> The store is now open!.\n" +
                        "  </div>"));
                }
                else {
                    $("#openResultPlaceHolder").append($("<div class=\"alert alert-danger\">\n" +
                        "    <strong>Error!</strong> Another store is already open in the location you choosed, please try again!.\n" +
                        "  </div>"));
                }
                $("#open-store-carousel").carousel("next");
            }
        })

        return false;
    });

    $("#openStoreDoneBtn").click(function () {
        location.reload();
    });
})

//# sourceURL=ownerOpenStore.js