var numberOfTries = 0;
var LOGIN_URL = buildUrlWithContextPath("login");

function signUpClick(formLogin) {
        $.ajax({
            url:LOGIN_URL,
            type:"POST",
            data: formLogin.serialize(),

            //res = {loginRes:true/false, loginName:"moshe"}
            success: function (res) {
                if(res.loginRes===true) {
                    document.getElementById('linkToDashBoard').click();
                }
                else {
                    $("#errorPlaceHolder").removeClass("invisible");
                    $("#nameExists").innerText = res.loginName;
                }
            },
            error: function () {
                numberOfTries++;
                if(numberOfTries === 3) {
                    var mainContainer = $("div.container");
                    mainContainer.children().empty();
                    mainContainer.append(
                        "<div class=\"alert alert-danger\">\n" +
                        "                <strong>ERROR!</strong> We might have error on our server please refresh this page and try again in few seconds.\n" +
                        "            </div>")
                }

                setTimeout(submitClick, 1000);
            }
        });

}

function submitClick() {
    signUpClick($("#loginTry"));
}



$(function () {

    var formLogin= $("#loginTry");

    formLogin.submit(function () {
        signUpClick(formLogin);
        return false;
    });
});

$(function() {
    $(':input[type="submit"]').prop('disabled', true);
    $('input[type="text"]').keyup(function() {
        if($(this).val() != '') {
            $(':input[type="submit"]').prop('disabled', false);
        }
        else {
            $(':input[type="submit"]').prop('disabled', true);
        }
    });
})

