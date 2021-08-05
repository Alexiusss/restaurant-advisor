<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>

<@c.page>
    List of users

    <@p.pager url page />

    <table>
        <thead>
        <tr>
            <th>Firstname</th>
            <th>Lastname</th>
            <th>Email</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list page.content as user>
            <tr>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id()}">edit</a></td>
            </tr>
        </#list>
        </tbody>
    </table>

    <@p.pager url page />
</@c.page>