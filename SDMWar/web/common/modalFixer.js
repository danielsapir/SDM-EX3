function showModal(head, body) {
    var popUp = $("<div id=\"modalPopUp\" class=\"modal fade\" role=\"dialog\">\n" +
        "        <div class=\"modal-dialog modal-xl\">\n" +
        "\n" +
        "            <!-- Modal content-->\n" +
        "            <div class=\"modal-content\">\n" +
        "                <div class=\"modal-header text-left\">\n" +
        "                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "                    <h4 class=\"modal-title text-left\" id=\"modal-title-head\">"+ head + "</h4>\n" +
        "                </div>\n" +
        "                <div class=\"modal-body\">\n" +
        "                    <div class=\"modal-body\">" + body + "</div>\n" +
        "                </div>\n" +
        "                <div class=\"modal-footer\">\n" +
        "                    <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">OK</button>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "\n" +
        "        </div>\n" +
        "    </div>");

    var clone =  popUp.clone();
    $(clone).appendTo($("#modalPlace"));
    $(clone).modal({show: false});
    $(clone).modal("show");
};