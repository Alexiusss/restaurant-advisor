const restaurantAjaxUrl = "restaurants/";

$(document).ajaxError(
    function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    }
)

let formRestaurant = $('#detailsFormRestaurant');
let formContact = $('#detailsFormContact')

function addRestaurant() {
    formRestaurant.find(":input").val("");
    $("#editRestaurantModal").modal();
}

function addContact() {
    formContact.find(":input").val("");
    $("#editContactModal").modal();
}

function saveRestaurant() {
    let formRestaurantData = new FormData(formRestaurant[0]);
    $.ajax({
        type: "POST",
        url: restaurantAjaxUrl,
        data: formRestaurantData,
        processData: false,
        contentType: false,
    }).done(function () {
        $("#editRestaurantModal").modal("hide");
        successNoty(i18n["saved"]);
        if (formRestaurantData.get("id") > 0) {
            editRestaurantElement(formRestaurantData)
        } else {
            window.location.reload()
        }
    })
}

function editRestaurant(id) {
    formRestaurant.find(":input").val("");
    $.get(restaurantAjaxUrl + id + "/restaurant", function (data) {
        $.each(data, function (key, value) {
            formRestaurant.find("input[name='" + key + "']").val(value);
        });
        $("#editRestaurantModal").modal();
    })
}

function deleteRestaurant(id, name) {
    if (confirm(i18n["confirmDelete"] + name + " ?")) {
        $.ajax({
            url: restaurantAjaxUrl + id,
            type: "DELETE"
        }).done(function () {
            removeElement(id, "#restaurant-card_")
            successNoty(i18n["deleted"])
        })
    }
}

function saveContact(restaurantId) {
    $.ajax({
        type: "POST",
        url: restaurantAjaxUrl + restaurantId + "/contact",
        data: formContact.serialize()
    }).done(function () {
        $("#editContactModal").modal("hide");
        successNoty(i18n["saved"]);
        let formData = new FormData(formContact[0])
        if (formData.get("id") > 0) {
            editContactElement(formData)
        } else {
            window.location.reload()
        }
    })
}

function editContact(id) {
    formContact.find(":input").val("");
    $.get(restaurantAjaxUrl + id + "/contact", function (data) {
        $.each(data, function (key, value) {
            formContact.find("input[name='" + key + "']").val(value);
        });
        $("#editContactModal").modal();
    })
}

function editRestaurantElement(formData) {
    $("#name_" + formData.get("id")).text(formData.get("name"))
    $("#cuisine_" + formData.get("id")).text(formData.get("cuisine"))
}

function editContactElement(formData) {
    $("#address_" + formData.get("id")).text(formData.get("address"))
    $("#website_" + formData.get("id")).text(formData.get("website"))
    $("#email_" + formData.get("id")).text(formData.get("email"))
    $("#phone_number_" + formData.get("id")).text(formData.get("phone_number"))
}

// https://itchief.ru/javascript/input-mask
$(function () {
    $(function () {
        function maskPhone() {
            let country = $('#country option:selected').val();
            switch (country) {
                case "1" :
                    $("#phone_number").mask("+9 (999) 999-99-99");
                    $('#phone_number').attr('placeholder','+1 (234) 567-89-10');
                    break;
                case "2" :
                    $("#phone_number").mask("+99 (999) 999-99-99");
                    $('#phone_number').attr('placeholder','+12 (234) 567-89-10');
                    break;
                case "3" :
                    $("#phone_number").mask("+999 (999) 999-99-99");
                    $('#phone_number').attr('placeholder','+123 (234) 567-89-10');
                    break;
                case "4" :
                    $("#phone_number").mask("+9999 (999) 999-99-99");
                    $('#phone_number').attr('placeholder','+1234 (234) 567-89-10');
                    break;
            }
        }
        maskPhone();
        $('#country').change(function () {
            maskPhone()
        })
    })
})