function deleteRestaurant(id, name) {
    if (confirm(i18n["confirmDelete"] + name + " ?")) {
        $.ajax({
            url: "/main/" + id,
            type: "DELETE",
            success: function () {
                removeElement(id, "#restaurant-card_")
                successNoty(i18n["deleted"])
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}
let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(msg) {
    closeNoty();
    new Noty({
        text: msg,
        type: 'success',
        layout: "bottomRight",
        timeout: 2000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
     let errorInfo = jqXHR.responseJSON;
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + errorInfo.typeMessage + "<br>" + errorInfo.details.join("<br>"),
        type: "error",
        layout: "bottomRight"
    });
    failedNote.show()
}

// https://makitweb.com/dynamically-add-and-remove-element-with-jquery/
function removeElement(id, elementName) {
    $(elementName + id).remove();
}
