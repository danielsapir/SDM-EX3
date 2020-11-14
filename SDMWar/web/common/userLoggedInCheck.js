var DASHBOARD_URL = buildUrlWithContextPath("dashboard");

function checkIfUserLoggedIn() {
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
            else {
                window.location.replace(buildUrlWithContextPath("dashBoard/dashboard.html"));
            }
        }
    })
}

$(checkIfUserLoggedIn());