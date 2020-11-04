var numberOfTries = 0;

function signUpClick(formLogin) {
        $.ajax({
            url:"../login",
            type:"POST",
            data: formLogin.serialize(),

            //res = {"result":true/false, "name":"moshe"}
            success: function (res) {
                if(res.result===true) {
                    $("#linkToDashBoard").click();
                }
                else {
                    $("#errorPlaceHolder").removeClass("invisible");
                    $("#nameExists").innerText = res.name;
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

                setTimeout(signUpClick, 1000);
            }
        });
        return false;
}


$(function () {

    var formLogin= $("#loginTry");

    formLogin.submit(function () {
        signUpClick(formLogin);
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

