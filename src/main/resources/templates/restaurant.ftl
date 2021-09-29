<script defer type="text/javascript" src="../../static/js/restaurant.js"></script>
<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "/spring.ftl" as spring/>

<@c.page>
    <div class="card-deck">
        <div class="row row-cols-1 row-cols-md-2">
            <div class="card mb-1 " style="width: 43rem;">
                <#if restaurant.filename??>
                    <img class="h-75" src="/img/${restaurant.filename}" alt="Card image cap">
                </#if>
                <div class="card-body">
                    <h5 class="card-title">${restaurant.getName()}</h5>
                    <p class="card-text">${restaurant.getCuisine()}</p>
                    <#if restaurant.getMenu()??>
                    <a id="menuLink" href="${restaurant.menu}" target="_blank"><@spring.message "restaurant.menu"/></a>
                    </#if>
                </div>
            </div>

            <div class="card-column mb-1">
                <div class="card text-right mb-1" style="height:17rem">
                    <div class="card-body">
                        <h5 class="card-title"><@spring.message "contact.title"/></h5>
                        <#if restaurant.contact??>
                            <p id="address_${restaurant.getId()}"
                               class="card-text">${restaurant.contact.getAddress()}</p>
                            <p id="phone_number_${restaurant.getId()}"
                               class="card-text">${restaurant.contact.getPhone_number()}</p>
                            <#if restaurant.contact.getWebsite()??>
                                <p id="website_${restaurant.getId()}"
                                   class="card-text">${restaurant.contact.getWebsite()}</p>
                            </#if>
                            <#if restaurant.contact.getEmail()??>
                                <p id="email_${restaurant.getId()}"
                                   class="card-text">${restaurant.contact.getEmail()}</p>
                            </#if>
                            <#if isAdmin>
                                <button class="btn btn-outline-primary ml-2 btn-sm"
                                        onclick="editContact(${restaurant.getId()})">
                                    <span class="fa"></span>
                                    <@spring.message "contact.edit"/>
                                </button>
                            </#if>
                        <#else>
                            <p class="card-text"><small class="text-muted"><@spring.message "contact.noData"/></small>
                            </p>

                            <#if isAdmin>
                                <button class="btn btn-outline-primary ml-2 btn-sm" onclick="addContact()">
                                    <span class="fa"></span>
                                    <@spring.message "contact.add"/>
                                </button>
                            </#if>
                        </#if>
                    </div>
                </div>

                <div class="card flex-fill">
                    <#if rating==0 >
                        <div>
                            ${restaurant.getName()} <@spring.message "ratingCard.noReviews"/>
                        </div>
                    <#else>
                        <#include "parts/ratingcard.ftl"/>
                    </#if>
                </div>
            </div>

        </div>

    </div>

    <#if isAdmin>
        <#include "parts/contactEdit.ftl"/>
    </#if>

    <div class="list-group">
        <a class="list-group-item list-group-item-action flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1"><@spring.message "review.title"/> </h5>
                <#if !isAdmin>
                    <button class="btn btn-outline-primary ml-2 btn-sm text-right" onclick="
                    <#if currentUserActive>
                        <#if isCurrentUserVoted==true>
                                infoModalOpen('repeated')
                        <#else>
                                addReview()
                        </#if>
                    <#else>
                            infoModalOpen('notActivated')
                    </#if>
                            ">
                        <span class="fa"></span>
                        <@spring.message "review.write"/>
                    </button>
                </#if>
            </div>
        </a>
        <#include "parts/reviewList.ftl" />
    </div>

    <#if !currentUserActive || isCurrentUserVoted??>
        <div class="modal" tabindex="-1" role="dialog" id="infoModal">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body" id="infoModalContentBody">
                    </div>
                </div>
            </div>
        </div>
    </#if>

</@c.page>

<script>
    setTimeout(function () {
        $(".rateyo").rateYo()
    }, 10)
</script>