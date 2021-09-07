<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>

<@c.page>
    <h5>${email}</h5>
    ${message!}
    <form method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.firstName"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="firstName" class="form-control" placeholder="User firstname" value="${firstName!''}"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.lastName"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="lastName" class="form-control" placeholder="User lastname" value="${lastName!''}"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.password"/>: </label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="<@spring.message "user.password"/>"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"><@spring.message "common.save"/></button>
    </form>
</@c.page>