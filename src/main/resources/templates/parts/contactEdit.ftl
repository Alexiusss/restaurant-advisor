<#import "/spring.ftl" as spring/>

<div class="collapse<#if contact??>show</#if>" id="collapseContact">
    <div class="form-group mt-3">
        <form method="post" action="/main/${restaurant.getId()}/contact" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(addressError??)?string('is-invalid', '')}"
                       value="<#if restaurant.getContact()??>${restaurant.getContact().getAddress()}</#if>"
                       name="address" placeholder="Address"/>
                <#if addressError??>
                    <div class="invalid-feedback">
                        ${addressError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control ${(websiteError??)?string('is-invalid', '')}"
                       value="<#if restaurant.getContact()??>${restaurant.getContact().getWebsite()}</#if>"
                       name="website" placeholder="Website"/>
                <#if websiteError??>
                    <div class="invalid-feedback">
                        ${websiteError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="email" class="form-control ${(emailError??)?string('is-invalid', '')}"
                       value="<#if restaurant.getContact()??>${restaurant.getContact().getEmail()}</#if>"
                       name="email" placeholder="Email"/>
                <#if emailError??>
                    <div class="invalid-feedback">
                        ${emailError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control ${(phone_numberError??)?string('is-invalid', '')}"
                       value="<#if restaurant.getContact()??>${restaurant.getContact().getPhone_number()}</#if>"
                       name="phone_number" placeholder="Phone number"/>
                <#if phone_numberError??>
                    <div class="invalid-feedback">
                        ${phone_numberError}
                    </div>
                </#if>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="form-group">
                <button type="submit" class="btn btn-outline-primary ml-2 btn-sm"><@spring.message "common.save"/></button>
            </div>
        </form>
    </div>
</div>