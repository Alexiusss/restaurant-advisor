<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="card-deck">
        <div class="row row-cols-1 row-cols-md-2">

            <div class="card " style="width: 33rem;">
                <#if restaurant.filename??>
                    <img class="card-img-top" src="/img/${restaurant.filename}" alt="Card image cap">
                </#if>
                <div class="card-body">
                    <h5 class="card-title">${restaurant.getName()}</h5>
                    <div>${rating} </div>
                    <p class="card-text">${restaurant.getCuisine()}</p>
                </div>

                <div class="text-right">
                    <#if isAdmin>
                        <a class="btn btn-outline-primary" data-toggle="collapse" href="#collapseRestaurant"
                           role="button" aria-expanded="false"
                           aria-controls="collapseRestaurant">
                            Edit
                        </a>
                    </#if>
                </div>
            </div>

            <div class="card-column">
                <div class="card text-right mb-1" style="height:17rem">
                    <div class="card-body">
                        <h5 class="card-title">Location and contact details</h5>
                        <#if restaurant.contact??>
                            <p class="card-text">${restaurant.contact.getAddress()}</p>
                            <p class="card-text">${restaurant.contact.getPhone_number()}</p>
                            <#if restaurant.contact.getWebsite()??>
                                <p class="card-text">${restaurant.contact.getWebsite()}</p>
                            </#if>
                            <#if restaurant.contact.getEmail()??>
                                <p class="card-text">${restaurant.contact.getEmail()}</p>
                            </#if>
                            <#if isAdmin>
                                <a class="btn btn-outline-primary" data-toggle="collapse" href="#collapseContact"
                                   role="button"
                                   aria-expanded="false"
                                   aria-controls="collapseContact">
                                    Edit contact
                                </a>
                            </#if>
                        <#else>
                            <p class="card-text"><small class="text-muted">No data</small></p>

                            <#if isAdmin>
                                <a class="btn btn-primary" data-toggle="collapse" href="#collapseContact" role="button"
                                   aria-expanded="false"
                                   aria-controls="collapseExample">
                                    Add contact
                                </a>
                            </#if>
                        </#if>
                    </div>
                </div>

                <div class="card mb-1" style="height:16rem">
                    <#if rating==0 >
                        <div>
                            ${restaurant.getName()} does not have enough reviews. Be one of the first people to write a
                            review!
                        </div>
                    <#else>
                        <#include "parts/ratingcard.ftl"/>
                    </#if>
                </div>
            </div>

        </div>

    </div>

    <#if isAdmin>
        <#include "parts/restaurantEdit.ftl"/>

        <#include "parts/contactEdit.ftl"/>
    </#if>
    <div class="mx-auto" style="width: 200px;">
        Centered element
    </div>

    <div class="list-group" style="width: 34rem">

        <a class="list-group-item list-group-item-action flex-column align-items-start">
            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">Reviews </h5>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter">
                    Write a review
                </button>
            </div>
        </a>

        <#include "parts/reviewEdit.ftl"/>

        <#include "parts/reviewList.ftl" />


    </div>
</@c.page>

<script>
    $(".rateyo").rateYo();
</script>