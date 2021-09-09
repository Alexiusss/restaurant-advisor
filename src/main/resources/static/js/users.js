const userAjaxUrl = "admin/users/";
let form;

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, updateTableByData);
    }
}

function activate(chkbox, id) {
    let active = chkbox.is(":checked");
    //  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: userAjaxUrl + id,
        type: "POST",
        data: "active=" + active
    }).done(function () {
        chkbox.closest("tr").attr("data-user-active", active);
        successNoty(active ? i18n["activated"] : i18n["deactivated"]);
    }).fail(function () {
        $(chkbox).prop("checked", !active);
    });
}

$(function () {
    ctx.datatableApi = $('#datatable').DataTable({
        "ajax": {
            "url": ctx.ajaxUrl,
            "dataSrc": ""
        },
        "language": {
            "search": i18n["search"]
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
                    return '<a class="btn btn-outline-info btn-sm" onclick="updateUser(' + row.id + ')">' + 'Edit' + '</a>';
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
        ],
        "createdRow": function (row, data, dataIndex) {
            if (!data.active) {
                $(row).attr("data-user-active", false);
            }
        }
    });
    form = $('#detailsForm');
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });
})

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function save() {
    $.ajax({
        type: "POST",
        url: userAjaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        ctx.updateTable();
        successNoty(i18n["saved"])
    })
}

function deleteUser(id) {
    if (confirm(i18n["confirmDelete"] + id + "?")) {
        $.ajax({
            url: userAjaxUrl + id,
            type: "DELETE",
            success: function () {
                ctx.updateTable();
                successNoty( id + i18n["deleted"])
            },
            error: function (xhr) {
                console.log(xhr.responseText);
            }
        })
    }
}

function updateUser(id) {
    form.find(":input").val("");
    form.find("#ADMIN").prop("checked", false);
    form.find("#USER").prop("checked", false);
    $.get("admin/users/" + id, function (data) {
        $.each(data, function (key, value) {
            if (key === "roles") {
                $.each(value, function (key, value) {
                    // https://stackoverflow.com/a/19630917
                    $("#" + value).prop("checked", true);
                });
            }
            form.find("input[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}

function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}