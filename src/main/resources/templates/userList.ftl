<script defer type="text/javascript" src="../../static/js/users.js"></script>
<script defer type="text/javascript" src="../../static/js/common.js"></script>
<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>

<@c.page>
    List of users
    <table class="table table-striped" id="datatable">
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
</@c.page>