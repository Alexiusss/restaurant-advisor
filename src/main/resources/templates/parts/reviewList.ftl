<#include "security.ftl">
<#list reviews as review>
    <ul class="list-group" id="review_${review.getId()}">
        <li class="list-group-item <#if !review.isActive()>list-group-item-light</#if>">

            <div class="d-flex w-100 justify-content-between">
                <h5 class="mb-1">${review.getTitle()}</h5>
                <small class="text-muted">${review.getDate().year}</small>
            </div>
            <p class="mb-1">${review.getComment()}</p>
            <div class="figure w-25">
                <#if review.filename??>
                    <img src="/img/${review.filename}" class="img-fluid rounded z-depth-2 mb-4" alt="No image"/>
                </#if>
            </div>
            <div class="d-flex w-100 justify-content-between">
                <div class="text-muted"><a
                            href="/user-reviews/${review.getUser().getId()}">${review.getUser().getFirstName()}</a>
                </div>
                <div class="float-right">
                    <#if isAdmin>
                        <#if currentPageUrl == "/reviews">
                            <a class="btn btn-outline-primary"
                               href="/reviews?review=${review.getId()}">
                                Edit
                            </a>
                        </#if>
                    </#if>
                    <#if currentUserId == review.getUser().getId() && currentPageUrl?contains("user-reviews")>
                    <button class="btn btn-outline-danger" id="deleteReview" onclick="deleteReview(${review.getId()})">
                        Delete
                    </button>
                </div>
            </div>
            </#if>
        </li>
    </ul>
    <#include "reviewEdit.ftl">
</#list>