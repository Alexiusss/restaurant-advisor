<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Spring Security Example </title>
</head>
<body>
Login page
<form method="post" action="/login" >
    <div><label> User email : <input type="email" name="username"/> </label></div>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <div><input type="submit" value="Sign In"/></div>
</form>
<a href="/registration">Add new user</a>
</body>
</html>