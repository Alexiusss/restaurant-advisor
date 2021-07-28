function deleteReview(id){
    var reviewClear = confirm("Are you sure you want to delete review?");
    if (reviewClear) {
        $.ajax({
            url: "" + id,
            type: "DELETE",
            success: function () {
                location.reload();
                alert("Your review successfully deleted!")
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}