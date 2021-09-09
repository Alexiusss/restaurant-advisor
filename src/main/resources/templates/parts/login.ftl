<#include "security.ftl">
<#import "/spring.ftl" as spring/>

<#macro login path isRegisterForm>
    <form action="${path}" method="post" xmlns="http://www.w3.org/1999/html">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> <@spring.message "user.email"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="User email"/>
            </div>
        </div>
        <#nested>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> <@spring.message "user.password"/>: </label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <#if !isRegisterForm><a href="/user/registration"><@spring.message "user.addNew"/></a></#if>
        <button class="btn btn-primary" type="submit" ><@spring.message "user.sign"/></button>
    </form>
</#macro>

<#macro logout >
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit"><#if name!=""><@spring.message "app.logout"/><#else><@spring.message "app.login"/></#if></button>
    </form>
</#macro>