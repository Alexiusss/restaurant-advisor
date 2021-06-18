<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="card-deck">

        <div class="card" style="width: 33rem;">
            <#if restaurant.filename??>
                <img class="card-img-top" src="/img/${restaurant.filename}" alt="Card image cap">
            </#if>
            <div class="card-body">
                <h5 class="card-title">${restaurant.getName()}</h5>
                <div>${restaurant.rating()} </div>
                <p class="card-text">${restaurant.getCuisine()}</p>
            </div>
        </div>

        <div class="card" style="height: 15rem">
            <div class="card-body">
                <h5 class="card-title">Location and contact details</h5>
                <#if restaurant.contact??>
                    <p class="card-text">${restaurant.contact.getAdress()}</p>
                    <p class="card-text">${restaurant.contact.getPhone_number()}</p>
                    <#if restaurant.contact.getWebsite()??>
                        <p class="card-text">${restaurant.contact.getWebsite()}</p>
                    </#if>
                    <#if restaurant.contact.getEmail()??>
                        <p class="card-text">${restaurant.contact.getEmail()}</p>
                    </#if>
                <#else>
                    <p class="card-text"><small class="text-muted">No data</small></p>
                </#if>
            </div>
        </div>

    </div>
</@c.page>