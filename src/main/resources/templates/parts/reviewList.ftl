<script defer type="text/javascript" src="../../static/js/common.js"></script>
<script defer type="text/javascript" src="../../static/js/reviews.js"></script>
<#include "security.ftl">
<#import "pager.ftl" as p>
<#import "/spring.ftl" as spring>

<#list page.content as review>
    <ul class="list-group" id="review_${review.getId()}">
        <li id="li_${review.getId()}" class="list-group-item <#if !review.isActive()>list-group-item-light</#if>">

            <div class="d-flex w-100 justify-content-between">
                <h5 id="title_${review.getId()}" class="mb-1">${review.getTitle()}</h5>
                <small class="text-muted">${review.getDate().year}</small>
            </div>
            <p id="comment_${review.getId()}" class="mb-1">${review.getComment()}</p>
            <div class="figure w-25">
                <#if review.filename??>
                    <img src="/img/${review.filename}" class="img-fluid rounded z-depth-2 mb-4" alt="No image"/>
                </#if>
            </div>
            <div class="d-flex w-100 justify-content-between">
                <div class="text-muted"><a
                            href="/user-reviews/${review.getAuthor().getId()}">${review.getAuthorName()}</a>
                </div>

                    <a id="heart_${review.getId()}"
                       <#if !review.isActive()> style="visibility: hidden" </#if>
                       class="float-center" onclick="like(${review.getId()})">
                        <#if review.meLiked>
                            <i id="likeReview_${review.getId()}" class="fas fa-heart"></i>
                        <#else>
                            <i id="likeReview_${review.getId()}" class="far fa-heart"></i>
                        </#if>
                        <span class="likes-count_${review.getId()}"> ${review.likes} </span>
                    </a>

                <div class="float-right">
                    <#if isAdmin>
                        <#if currentPageUrl == "/reviews">
                            <a class="btn btn-outline-info btn-sm" onclick="editReview(${review.getId()})">
                                <@spring.message "common.edit"/>
                            </a>
                        </#if>
                    </#if>
                    <#if currentUserId == review.getAuthor().getId() && currentPageUrl?contains("user-reviews")>
                    <button class="btn btn-outline-danger" id="deleteReview" onclick="deleteReview(${review.getId()})">
                        <@spring.message "common.delete"/>
                    </button>
                </div>
            </div>
            </#if>
        </li>
    </ul>
</#list>
<#include "reviewEdit.ftl">
<#include "i18n.ftl">
<@p.pager url page />