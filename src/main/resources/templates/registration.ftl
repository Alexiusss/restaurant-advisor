<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Spring Security Example </title>
</head>
<body>
Add new user
<form action="/registration" method="post">
    <div><label> User email : <input type="text" name="email"/> </label></div>
    <div><label> User firstname : <input type="text" name="firstName"/> </label></div>
    <div><label> User lastname : <input type="text" name="lastName"/> </label></div>
    <div><label> Password: <input type="password" name="password"/> </label></div>
    <div><input type="submit" value="Sign In"/></div>
</form>
</body>
</html>