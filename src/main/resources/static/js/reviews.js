const reviewAjaxUrl = "reviews/";

$(document).ajaxError(
    function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    }
)

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

let formReview = $('#detailsFormReview');

function addReview() {
    formReview.find(":input").val("");
    $("#exampleModalCenter").modal();
}

function saveReview(restaurantId) {

    // https://mkyong.com/jquery/jquery-ajax-submit-a-multipart-form/
    let formData = new FormData(formReview[0]);

    $.ajax({
        type: "POST",
        url: reviewAjaxUrl + restaurantId,
        data: formData,
        processData: false,
        contentType: false
    }).done(function () {
            $("#exampleModalCenter").modal("hide");
            successNoty(i18n["saved"]);
        }
    )
}

function updateReview() {

    let formData = new FormData(formReview[0]);
    let isActive = $("#active").is(":checked")
    formData.set("active", isActive)

    $.ajax({
        type: "POST",
        url: reviewAjaxUrl,
        data: formData,
        processData: false,
        contentType: false
    }).done(function () {
        $("#exampleModalCenter").modal("hide");
        successNoty(i18n["saved"]);
        editElement(formData, isActive);
    })
}

function editReview(id) {
    formReview.find(":input").val("");
    $.get("reviews/" + id, function (data) {
        $.each(data, function (key, value) {
            if (key === "active") {
                $("#active").prop("checked", value);
            }
            formReview.find("input[name='" + key + "']").val(value);
        });
        $("#exampleModalCenter").modal();
    });
}

function editElement(formData, isActive) {
    // https://stackoverflow.com/a/40403989
    if (isActive) {
        $("#li_" + formData.get("id")).attr("class", "list-group-item");
        $("#heart_" + formData.get("id")).attr("style", "visibility: visible");
    } else {
        $("#li_" + formData.get("id")).attr("class", "list-group-item list-group-item-light");
        $("#heart_" + formData.get("id")).attr("style", "visibility: hidden");
    }
    //   https://itchief.ru/javascript/jquery-html-text
    $("#title_" + formData.get("id")).text(formData.get("title"))
    $("#comment_" + formData.get("id")).text(formData.get("comment"))
}

let infoModal = $("#infoModal");

function infoModalOpen(message) {
    if (message === 'notActivated') {
        $("#infoModalContentBody").text(i18n["notActivated"]);
    } else if (message === 'repeated') {
        $("#infoModalContentBody").text(i18n["repeated"]);
    }
    infoModal.modal()
}