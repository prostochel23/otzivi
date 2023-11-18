<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<h1>Otzivnik</h1><hr>
<h3>Подробная информация о пользователе ${user.name}</h3>
<b>Email: </b> ${user.email}<br>
<h4>Отзывы пользователя ${user.name}</h4>
<#list products as product>
    <div>
        <p><b>${product.title}</b> | <a href="/product/${product.id}">Подробнее...</a></p>
    </div>
    <#else>
    <h3>Отзывов нет</h3>
</#list>
<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>
</body>
</html>