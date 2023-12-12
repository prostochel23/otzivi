<#import "pager.ftlh" as p>
<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous" />
</head>
<body>
<h1>Otzivnik</h1><hr>
<#if user.email??>
    <h3>Имя пользователя: <i>${user.name}</i></h3>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Выйти"/>
    </form>
    <#if user.isAdmin()>
        <a href="/admin">Панель администратора</a>
    </#if>
<#else>
    <a href="/login">Войти</a></h1>
</#if>
<hr>
<h4>Отзывы со всей России</h4>
<form action="/" method="get">
    Поиск по названию продукта: <input type="text" name="title"><br>
    <input type="submit" value="Поиск"/>
</form>
<form action="/" method="get">
    Поиск по категории:
	
    <select name="category">

    <#list categories as category>
    <option value="${category}">${category}</option>
    <#else>
    Без категорий пока
    </#list>
    </select>
    <input type="submit" value="Поиск"/>
</form>
<@p.pager url page />
<#list products as product>
    <div>
        <p><b>${product.title}</b> | <a href="/product/${product.id}">Подробнее...</a></p>
		<div class="rating">
        <!-- Display stars based on the product's rating -->
        <span><#if (product.rating > 0)> &#9733; <#else> &#9734;</#if></span>
        <span><#if (product.rating > 1)> &#9733; <#else> &#9734;</#if></span>
		<span><#if (product.rating > 2)> &#9733; <#else> &#9734;</#if></span>
		<span><#if (product.rating > 3)> &#9733; <#else> &#9734;</#if></span>
		<span><#if (product.rating > 4)> &#9733; <#else> &#9734;</#if></span>
		</div>
    </div>
    <#else>
    <h3>Товаров нет</h3>
</#list>
<#if user.email??>
    <hr>
    <h3>Создать новый отзыв</h3>
    <form action="/product/create" method="post" enctype="multipart/form-data">
        Название продукта: <input type="text" name="title"/><br><br>
        Отзыв на продукт: <input type="text" name="description"/><br><br>
        Первая фотография: <input type="file" name="file1"/><br><br>
        Вторая фотография: <input type="file" name="file2"/><br><br>
        Третья фотография: <input type="file" name="file3"/><br><br>
        <select name="category">
            <#list categories as category>
            <option value="${category}">${category}</option>
            <#else>
            Без категорий пока
            </#list>
            </select>
        <br>
		Если вы не нашли вашу категорию, вы можете указать ее здесь:<br>
        <input type="text" name="anotherCategory"><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Добавить отзыв"/>
    </form>
</#if>
</body>
</html>