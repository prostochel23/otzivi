<!DOCTYPE html>
<html>
<head>
    <title>Otzivnik</title>
</head>
<body>
<h1>Otzivnik</h1><hr>
<h4>Восстановление пароля</h4>
<#if step == 1>
<form action="/recovery" method="post">
    Укажите ваш email: <input type="text" name="email"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Восстановить пароль"/>
</#if>
<#if step == 2>
<h1>Вам на почту отправлена ссылка для сброса пароля</h1><hr>
</#if>
<#if step == 3>
<form action="/recovery/${id!'0'}/${code!'No'}" method="post">
    Ваш новый пароль: <input type="password" name="password"><br><br>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Восстановить пароль"/>
</#if>
<#if step == 4>
    <h1>Теперь вы можете зайти с новым паролем</h1><hr>
</#if>

</form>
<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>
</body>
</html>