const userAjaxUrl = "user/getAll";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, updateTableByData);
    }
}

function activate (chkbox, id) {
    let active = chkbox.is(":checked");
    //  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: "user/" + id,
        type: "POST",
        data: "active=" + active
    }).done(function () {
        chkbox.closest("tr").attr("data-user-active", active);
        successNoty(active ? "User activated" : "User deactivated");
    }).fail(function() {
        $(chkbox).prop("checked", !active);
    });
}

$(function () {
    ctx.datatableApi = $('#datatable').DataTable({
        "ajax": {
            "url": ctx.ajaxUrl,
            "dataSrc": ""
        },
        "columns": [{
            "data": "firstName"
        },
            {
                "data": "lastName"
            },
            {
                "data": "email",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<a href='mailto:" + data + "'>" + data + "</a>";
                    }
                    return data;
                }
            },
            {
                "data": "roles"
            },
            {
                "data": "active",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='activate($(this)," + row.id + ");'/>";
                    }
                    return data;
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
