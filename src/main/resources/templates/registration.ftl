<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>
<input type="hidden" value="recaptcha_tags ajax: true">
<@c.page>
    <div class="mb-1"><@spring.message "user.addNew"/></div>
    <form action="/user/registration" method="post" xmlns="http://www.w3.org/1999/html">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.email"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="email" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}"
                       placeholder="<@spring.message "user.email"/>"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.firstName"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="firstName" value="<#if user??>${user.firstName}</#if>"
                       class="form-control ${(firstNameError??)?string('is-invalid', '')}"
                       placeholder="<@spring.message "user.firstName"/>"/>
                <#if firstNameError??>
                    <div class="invalid-feedback">
                        ${firstNameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.lastName"/>: </label>
            <div class="col-sm-6">
                <input type="text" name="lastName" value="<#if user??>${user.lastName}</#if>"
                       class="form-control ${(lastNameError??)?string('is-invalid', '')}"
                       placeholder="<@spring.message "user.lastName"/>"/>
                <#if lastNameError??>
                    <div class="invalid-feedback">
                        ${lastNameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.password"/>: </label>
            <div class="col-sm-6">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="<@spring.message "user.password"/>"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"><@spring.message "user.password"/>2: </label>
            <div class="col-sm-6">
                <input type="password" name="password2"
                       class="form-control ${(password2Error??)?string('is-invalid', '')}"
                       placeholder="<@spring.message "user.retypePassword"/>"/>
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="g-recaptcha" data-sitekey="6Ldosm0bAAAAAB-x1DLac2pMamvL8_q5fl9KnMWj"></div>
            <#if captchaError??>
                <div class="alert alert-danger" role="alert">
                    ${captchaError}
                </div>
            </#if>
        </div>
        <input type="hidden" name="active" value="false">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"><@spring.message "common.save"/></button>
    </form>
</@c.page>