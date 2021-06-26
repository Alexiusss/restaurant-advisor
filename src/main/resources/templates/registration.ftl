<#import "parts/common.ftl" as c>

<@c.page>
    <div class="mb-1">Add new user</div>
<form action="/registration" method="post" xmlns="http://www.w3.org/1999/html">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email: </label>
            <div class="col-sm-6">
                <input type="text" name="email" class="form-control" placeholder="Email"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User firstname: </label>
            <div class="col-sm-6">
                <input type="text" name="firstName" class="form-control" placeholder="User firstname"/>
            </div>
        </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">User lastname: </label>
        <div class="col-sm-6">
            <input type="text" name="lastName" class="form-control" placeholder="User lastname"/>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password: </label>
        <div class="col-sm-6">
            <input type="password" name="password" class="form-control" placeholder="Password"/>
        </div>
    </div>
    <input type="hidden" name="active" value="false">
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button type="submit" class="btn btn-primary">Create</button>
</form>
</@c.page>