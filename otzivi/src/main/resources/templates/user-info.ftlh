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
<#list products as comment>
    <div>
        <p><b>${comment.product.title}</b> | <a href="/product/${comment.product.id}">Подробнее...</a></p>
        <p><b>Текст отзыва:</b> | ${comment.text}</p>
        <p><b>Оценка:</b> | ${comment.estimation}</p>
    </div>
    <#else>
    <h3>Отзывов нет</h3>
</#list>
<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>
</body>
</html>