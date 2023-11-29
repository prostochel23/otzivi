<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<h1>Otzivnik</h1><hr>
<hr>
<h4>Изменение информации о товаре</h4>
<#list images as img>
    <img src="/images/${img.id}" height="60px"/><br><a href="/images/delete/${img.id}">Удалить изображение</a><br><br>

</#list>
    <hr>
    <form action="/product/update/${product.id}" method="post" enctype="multipart/form-data">
        Название продукта: <input type="text" name="title" value="${product.title}"/><br><br>
        Отзыв на продукт: <input type="text" name="description" value="${product.description}"/><br><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Обновить отзыв"/>
    </form>
</body>
</html>