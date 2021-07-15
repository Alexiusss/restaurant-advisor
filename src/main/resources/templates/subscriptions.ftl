<#import "parts/common.ftl" as c>

<@c.page>
    <h3>${userChannel.getFirstName()} ${userChannel.getLastName()} </h3>
    <div>${type}</div>
    <ul class="list-group">
        <#list users as user>
            <li class="list-group-item">
                <a href="/user-reviews/${user.getId()}">${user.getFirstName()} ${user.getLastName()}</a>
            </li>
        </#list>
    </ul>
</@c.page>