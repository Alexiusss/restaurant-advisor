$(function () {
    $('#datatable').DataTable({
        "ajax": {
            "url": "user/getAll",
            "dataSrc": ""
        },
        "columns": [{
            "data": "firstName"
        },
            {
                "data": "lastName"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "active",
                "render": function (data, type, row) {
                    return "<input type='checkbox' " + (data ? "checked" : "") + "/>";
                }
            },
            {
                "orderable": false,
                "defaultContent": "",
                //  https://stackoverflow.com/a/22476277
                "render": function (data, type, row) {
                    return '<a class="btn btn-outline-info btn-sm" href=/user/' + row.id + '>' + 'Edit' + '</a>';
                }
            }, {
                "orderable": false,
                "defaultContent": "",
                "render": function (data, type, row) {
                    return '<a class="btn btn-outline-danger btn-sm" onclick="deleteUser(' + row.id + ')">' + 'Delete' + '</a>';
                }
            }],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
})

function deleteUser(id) {
    if (confirm("Are you sure you want to delete user with id " + id + "?")) {
        $.ajax({
            url: "/user/" + id,
            type: "DELETE",
            success: function () {
                successNoty("User with " + id + " successfully deleted!")
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}
