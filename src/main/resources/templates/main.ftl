<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
<div>
    <@l.logout/>
</div>
    <span><a href="/user">User list</a></span>
<div>
    <form method="post" enctype="multipart/form-data">
        <input type="text" name="name" placeholder="Название ресторана">
        <input type="text" name="cuisine" placeholder="Тип кухни">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Добавить</button>
    </form>
</div>
<div>Список ресторанов</div>

    <form method="get" action="/main">
        <input type="text" name="filter" placeholder="Введите название ресторана" value="${filter!}">
        <button type="submit">Найти</button>
    </form>

<form method="get" action="/main">
    <input type="text" name="cuisine" placeholder="Введите название кухни" value="${cuisine!}">
    <button type="submit">Найти</button>
</form>

<#list restaurants as restaurant>
    <div>
        <b>${restaurant.id()}</b>
        <span>${restaurant.name}</span>
        <span>${restaurant.cuisine}</span>
        <b>${restaurant.rating()}</b>
        <div>
            <#if restaurant.filename??>
                <img src="/img/${restaurant.filename}">
            </#if>
        </div>
    </div>
<#else>
    No restaurant
</#list>
</@c.page>