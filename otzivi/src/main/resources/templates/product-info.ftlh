<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<style type="text/css" media="screen">
    .rating {
      width: 200px;
      height: 40px;
      display: flex;
      flex-direction: row-reverse;
      align-items: center;
      justify-content: flex-end;
    }

    .rating:not(:checked)>input {
      display: none;
    }

    .rating:not(:checked)>label {
      width: 40px;
      cursor: pointer;
      font-size: 40px;
      color: lightgrey;
      text-align: center;
      line-height: 1;
    }

    .rating:not(:checked)>label:before {
      content: '★';
    }

    .rating>input:checked~label {
      color: gold;
    }

    .rating:not(:checked)>label:hover,
    .rating:not(:checked)>label:hover~label {
      color: gold;
    }

    .rating>input:checked+label:hover,
    .rating>input:checked+label:hover~label,
    .rating>input:checked~label:hover,
    .rating>input:checked~label:hover~label,
    .rating>label:hover~input:checked~label {
      color: gold;
    }
</style>
<h1>Otzivnik</h1><hr>
<h3>Подробная информация о товаре</h3>
<#list images as img>
    <img src="/images/${img.id}" height="60px"/><br><br>
</#list>
<b>Название товара: </b>${product.title}<br>
<b>Описание товара: </b>${product.description}<br>
<b>Категория:</b> ${product.category}<br>
<b>Автор: </b><a href="/user/${product.user.id}">${product.user.name}</a><br>
<#if !alreadyLoved>
<a href="/product/addFavourite/${product.id}">Добавить товар в избранное</a><br>
<#else>
<a href="/product/deleteFavourite/${product.id}">Убрать товар из избранного</a><br>
</#if>
<#if edit_allowed>
<a href="/product/delete/${product.id}">Удалить товар</a><br>
<a href="/product/update/${product.id}">Изменить описание товара</a><br>
</#if>
<hr>
<h3>Отзывы на товар</h3>
<#list comments as comment>
<div>
<#if comment.active>
    <div>
        <p><b>Автор:</b> <a href="/user/${comment.user.id}">${comment.user.name}</a><br></p>
		 <p><b>Комментарий: </b>${comment.text}</p>
		  <p><b>Оценка:</b> ${comment.estimation}</p>
		  <#if hide_allowed>
		  <a href="/hide/${comment.id}">Скрыть комментарий</a>
		  </#if>
	</div>
</#if>
<#else>
<h3>Отзывов нет</h3>
</div>
</#list>
<br>
<#if hide_allowed>
<hr>
<h3>Скрытые отзывы:</h3>
<#list comments as comment>
<#if !comment.active>
    <div>
        <p><b>Автор:</b> <a href="/user/${comment.user.id}">${comment.user.name}</a><br></p>
		 <p><b>Комментарий: </b>${comment.text}</p>
		  <p><b>Оценка:</b> ${comment.estimation}</p>
		  <#if hide_allowed>
		  <a href="/show/${comment.id}">Показать отзыв</a>
		  </#if>
</#if>
    <#else>
    <h3>Отзывов нет</h3>
</#list>
</#if>
<hr>
<h4>Оставить отзыв на товар</h4>
<form action="/makecomment/${product.id}" method="post">
    Ваше описание: <input type="text" name="text"><br><br>
    <div class="rating">
    <input type="radio" id="star-5" name="estimation" value="5">
          <label for="star-5" title="Оценка «5»"></label>
<input type="radio" id="star-4" name="estimation" value="4">
      <label for="star-4" title="Оценка «4»"></label>
      <input type="radio" id="star-3" name="estimation" value="3">
      <label for="star-3" title="Оценка «3»"></label>

            <input type="radio" id="star-2" name="estimation" value="2">
            <label for="star-2" title="Оценка «2»"></label>
      <input type="radio" id="star-1" name="estimation" value="1">
            <label for="star-1" title="Оценка «1»"></label>
      </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Добавить свой отзыв"/>
</form>
</body>
</html>