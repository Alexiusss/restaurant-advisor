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
            <th>Active</th>
            <th>Role</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr id="user-id_${user.id()}">
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td><label><input type="checkbox" ${user.isActive()?string("checked", "")}></label></td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id()}" class="btn btn-outline-info">Update</a></td>
                <td><a onclick="deleteUser(${user.id()})" class="btn btn-outline-danger">Delete</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>