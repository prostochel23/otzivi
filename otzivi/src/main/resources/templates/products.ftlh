<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<h1>Otzivnik</h1><hr>
<h4>Отзывы со всей России</h4>
<form action="/" method="get">
    Поиск по названию объявления: <input type="text" name="title"><br>
    <input type="submit" value="Поиск"/>
</form>
<#list products as product>
    <div>
        <p><b>${product.title}</b> | <a href="/product/${product.id}">Подробнее...</a></p>
    </div>
    <#else>
    <h3>Товаров нет</h3>
</#list>
<hr>
<h3>Создать новый отзыв</h3>
<form action="/product/create" method="post">
    Название продукта: <input type="text" name="title"/><br><br>
    Отзыв на продукт: <input type="text" name="description"/><br><br>
    Имя: <input type="text" name="author"/><br><br>
    <input type="submit" value="Добавить отзыв"/>
</form>
</body>
</html>