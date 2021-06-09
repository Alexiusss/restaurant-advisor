<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Restaurant advisor</title>
</head>
<body>
<div>
    <form method="post">
        <input type="text" name="name" placeholder="Название ресторана">
        <input type="text" name="cuisine" placeholder="Тип кухни">
        <button type="submit">Добавить</button>
    </form>
</div>
<div>Список ресторанов</div>

    <form method="get" action="/main">
        <input type="text" name="filter" placeholder="Введите название ресторана">
        <button type="submit">Найти</button>
    </form>

<form method="get" action="/main">
    <input type="text" name="cuisine" placeholder="Введите название кухни">
    <button type="submit">Найти</button>
</form>

<#list restaurants as restaurant>
    <div>
        <b>${restaurant.id()}</b>
        <span>${restaurant.name}</span>
        <span>${restaurant.cuisine}</span>
    </div>
<#else>
    No restaurant
</#list>
</body>
</html>