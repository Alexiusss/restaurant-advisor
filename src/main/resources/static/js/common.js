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