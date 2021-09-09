function deleteReview(id) {
    if (confirm(i18n["confirmDelete"] + "?")) {
        $.ajax({
            url: "/user-reviews/" + id,
            type: "DELETE",
            success: function () {
                removeElement(id, "#review_")
                successNoty(i18n["deleted"])
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}

function deleteRestaurant(id, name) {
    // if (confirm("Are you sure you want to delete " + name + " restaurant?")) {
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

function like(id) {
    $.ajax({
        url: "/reviews/" + id + "/like",
        type: "GET",
        success: function () {
            // https://stackoverflow.com/a/45661901
            let $count = $('.likes-count_' + id);
            let liked = document.getElementById("likeReview_" + id).className === "fas fa-heart";
            $count.text(function (idx, txt) {
                if (liked) {
                    return (+txt === 0) ? 0 : (+txt - 1);
                } else {
                    return +txt + 1;
                }
            });
            // https://qr.ae/pGOoOz
            $("#likeReview_" + id).toggleClass("fas fa-heart far fa-heart");
        },
        error: function (xhr) {
            console.log(xhr.responseText);
        }
    })
}