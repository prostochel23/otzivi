<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<script>
function show()
    {
     document.getElementById("categories").style.display="none";
     document.getElementById("anotherCategory").style.display="block";
    }
</script>
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