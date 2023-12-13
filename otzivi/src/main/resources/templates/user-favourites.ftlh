<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<h1>Otzivnik</h1><hr>
<#if user.email??>
    <h3>Имя пользователя: <i>${user.name}</i></h3>
</#if>
<hr>
<h4>Отзывы со всей России</h4>


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
		<a href="/product/deleteFavourite/${product.id}">Убрать товар из избранного</a><br>
    </div>
    <#else>
    <h3>Товаров нет</h3>
</#list>

</body>
</html>