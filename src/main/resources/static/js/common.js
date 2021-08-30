function deleteReview(id) {
    if (confirm("Are you sure you want to delete review?")) {
        $.ajax({
            url: "/user-reviews/" + id,
            type: "DELETE",
            success: function () {
                removeElement(id, "#review_")
                successNoty("Your review successfully deleted!")
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}

function deleteRestaurant(id, name) {
    if (confirm("Are you sure you want to delete " + name + " restaurant?")) {
        $.ajax({
            url: "/main/" + id,
            type: "DELETE",
            success: function () {
                removeElement(id, "#restaurant-card_")
                successNoty("Restaurant " + name + " successfully deleted!")
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}

function successNoty(msg) {
    new Noty({
        text: msg,
        type: 'success',
        layout: "bottomRight",
        timeout: 2000
    }).show();
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

$(function () {
    $("#datatable").DataTable();
})

function deleteUser(id) {
    if (confirm("Are you sure you want to delete user with " + id + " ?")) {
        $.ajax({
            url: "/user/" + id,
            type: "DELETE",
            success: function () {
                removeElement(id, "#user-id_")
                successNoty("User with " + id + " successfully deleted!")
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}