<script type="text/javascript" src="../../static/js/common.js"></script>
<#include "security.ftl">
<#import "pager.ftl" as p>

<#list page.content as review>
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
                            href="/user-reviews/${review.getAuthor().getId()}">${review.getAuthorName()}</a>
                </div>
                <#if review.isActive()>
                <a class="float-center" onclick="like(${review.getId()})">
                    <#if review.meLiked>
                        <i id="likeReview_${review.getId()}" class="fas fa-heart"></i>
                    <#else>
                        <i id="likeReview_${review.getId()}" class="far fa-heart"></i>
                    </#if>
                    <span class="likes-count_${review.getId()}"> ${review.likes} </span>
                </a>
                </#if>
                <div class="float-right">
                    <#if isAdmin>
                        <#if currentPageUrl == "/reviews">
                            <a class="btn btn-outline-primary"
                               href="/reviews?review=${review.getId()}">
                                Edit
                            </a>
                        </#if>
                    </#if>
                    <#if currentUserId == review.getAuthor().getId() && currentPageUrl?contains("user-reviews")>
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

<@p.pager url page />