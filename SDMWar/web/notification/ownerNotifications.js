var OWNER_ZONE_INFO_URL = buildUrlWithContextPath("ownerzoneinfo");

function getOwnerNotifications() {
    $.ajax({
        url: OWNER_ZONE_INFO_URL,
        method: "GET",
        data: {
            reqtype: "owner-notifications"
        },

        //notifications = [{type: "NewOrder"/"Feedback"/"NewStore", message: "you have..."}...]
        success: function (notifications) {

            $.each(notifications || [], function (index, notification) {
                showModal(decamelize(notification.type, " "),notification.message);
                try {
                    document.getElementById("ding").play();
                }
                catch (exc) {}
            })
        }
    })
}

$(setInterval(getOwnerNotifications, 1000));