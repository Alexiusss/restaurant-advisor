<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" placeholder="Search by restaurant name"
                       value="${filter!}">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="cuisine" class="form-control" placeholder="Search by cuisine"
                       value="${cuisine!}">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <#if isAdmin>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            Add new restaurant
        </a>
        <div class="collapse<#if restaurant?? || contact??>show</#if>" id="collapseExample">
            <div class="form-group mt-3">
                <form method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <input type="text" class="form-control ${(nameError??)?string('is-invalid', '')}"
                               value="<#if restaurant??>${restaurant.name}</#if>"
                               name="name" placeholder="Restaurant name"/>
                        <#if nameError??>
                            <div class="invalid-feedback">
                                ${nameError}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control ${(cuisineError??)?string('is-invalid', '')}"
                               value="<#if restaurant??>${restaurant.cuisine}</#if>"
                               name="cuisine" placeholder="Cuisine"/>
                        <#if cuisineError??>
                            <div class="invalid-feedback">
                                ${cuisineError}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <div class="custom-file">
                            <input type="file" name="file" id="customFile">
                            <label class="custom-file-label" for="customFile">Choose file</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control ${(addressError??)?string('is-invalid', '')}"
                               value="<#if contact??>${contact.address}</#if>"
                               name="address" placeholder="Address"/>
                        <#if addressError??>
                            <div class="invalid-feedback">
                                ${addressError}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control ${(websiteError??)?string('is-invalid', '')}"
                               value="<#if contact??>${contact.website}</#if>"
                               name="website" placeholder="Website"/>
                        <#if websiteError??>
                            <div class="invalid-feedback">
                                ${websiteError}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <input type="email" class="form-control ${(emailError??)?string('is-invalid', '')}"
                               value="<#if contact??>${contact.email}</#if>"
                               name="email" placeholder="Email"/>
                        <#if emailError??>
                            <div class="invalid-feedback">
                                ${emailError}
                            </div>
                        </#if>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control ${(phone_numberError??)?string('is-invalid', '')}"
                               value="<#if contact??>${contact.phone_number}</#if>"
                               name="phone_number" placeholder="Email"/>
                        <#if phone_numberError??>
                            <div class="invalid-feedback">
                                ${phone_numberError}
                            </div>
                        </#if>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </#if>

    <div class="card-columns">
        <#list restaurants as restaurant>
            <div class="card my-3">
                <div>
                    <#if restaurant.filename??>
                        <img src="/img/${restaurant.filename}" class="card-img-top"
                             onclick="window.location='/main/' + ${restaurant.getId()};">
                    </#if>
                </div>
                <div class="m-2">
                    <span>${restaurant.name}</span>
                </div>

                <div class="m-2">
                    <span>${restaurant.rating()}</span>
                </div>

                <div class="m-2">
                    <span>${restaurant.cuisine}</span>
                </div>
            </div>
        <#else>
            No restaurant
        </#list>
    </div>
</@c.page>