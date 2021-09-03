<script defer type="text/javascript" src="../../static/js/users.js"></script>
<script defer type="text/javascript" src="../../static/js/common.js"></script>
<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>

<@c.page>

    <h3 class="text-center">List of users</h3>
    <button class="btn btn-primary" onclick="add()">
        <span class="fa fa-plus"></span>
        Add
    </button>
    <table class="table table-striped pb-3" id="datatable">
        <thead>
        <tr>
            <th>Firstname</th>
            <th>Lastname</th>
            <th>Email</th>
            <th>Role</th>
            <th>Active</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
    </table>

    <div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="exampleModalCenterTitle">Users editor</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">

                    <div class="form-group">
                        <label for="firstName" class="col-form-label">First name</label>
                        <input type="text" class="form-control" name="firstName" id="firstName">
                    </div>

                    <div class="form-group">
                        <label for="lastName" class="col-form-label">Last name</label>
                        <input type="text" class="form-control" name="lastName" id="lastName">
                    </div>


                    <div class="form-group">
                        <label for="email" class="col-form-label">Email</label>
                        <input type="text" class="form-control" name="email" id="email">
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-form-label">Password</label>
                        <input type="password" class="form-control" name="password" id="password">
                    </div>

                    <div class="form-group">
                        <label for="password2" class="col-form-label">Password2</label>
                        <input type="password" class="form-control" name="password2" id="password2">
                    </div>

                    <div class="form-group">
                           <label><input type="checkbox" name="USER" id="USER" value="USER"> USER</label>
                           <label><input type="checkbox" name="ADMIN" id="ADMIN" value="ADMIN"> ADMIN</label>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span class="fa fa-close"></span>
                            Cancel
                        </button>
                        <button type="button" class="btn btn-primary" onclick="save()">
                            <span class="fa fa-check"></span>
                            Save
                        </button>
                    </div>
            </div>
        </div>
    </div>
</@c.page>