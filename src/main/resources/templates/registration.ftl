<#import "parts/common.ftl" as c>

<@c.page>
    <div class="mb-1">Add new user</div>
    <form action="/registration" method="post" xmlns="http://www.w3.org/1999/html">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Email: </label>
            <div class="col-sm-6">
                <input type="text" name="email" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="Email"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User firstname: </label>
            <div class="col-sm-6">
                <input type="text" name="firstName" value="<#if user??>${user.firstName}</#if>"
                       class="form-control ${(firstNameError??)?string('is-invalid', '')}"
                       placeholder="User firstname"/>
                <#if firstNameError??>
                    <div class="invalid-feedback">
                        ${firstNameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User lastname: </label>
            <div class="col-sm-6">
                <input type="text" name="lastName" value="<#if user??>${user.lastName}</#if>"
                       class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                       placeholder="User lastname"/>
                <#if lastNameError??>
                    <div class="invalid-feedback">
                        ${lastNameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password: </label>
            <div class="col-sm-6">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password2: </label>
            <div class="col-sm-6">
                <input type="password" name="password2"
                       class="form-control ${(password2Error??)?string('is-invalid', '')}"
                       placeholder="Retype password"/>
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
            </div>
        </div>
        <input type="hidden" name="active" value="false">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Create</button>
    </form>
</@c.page>