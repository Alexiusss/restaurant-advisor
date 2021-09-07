<#import "/spring.ftl" as spring/>
<div class="collapse<#if restaurant?? && !restaurant.getId()??>show</#if>" id="collapseRestaurant">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(nameError??)?string('is-invalid', '')}"
                       value="<#if restaurant??>${restaurant.name}</#if>"
                       name="name" placeholder="<@spring.message "restaurant.name"/>"/>
                <#if nameError??>
                    <div class="invalid-feedback">
                        ${nameError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control ${(cuisineError??)?string('is-invalid', '')}"
                       value="<#if restaurant??>${restaurant.cuisine}</#if>"
                       name="cuisine" placeholder="<@spring.message "restaurant.cuisine"/>"/>
                <#if cuisineError??>
                    <div class="invalid-feedback">
                        ${cuisineError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile" />
                    <label class="custom-file-label" for="customFile"><@spring.message "restaurant.photo"/></label>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary"><@spring.message "common.save"/></button>
            </div>
        </form>
    </div>
</div>