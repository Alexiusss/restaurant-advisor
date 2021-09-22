<#include "security.ftl">
<#import "/spring.ftl" as spring/>
<#if currentPageUrl=="/reviews">
    <#assign isEditPage=true>
</#if>

<div class="modal fade" tabindex="-1" id="exampleModalCenter">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="exampleModalCenterTitle"><@spring.message "review.editTitle"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsFormReview">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">

                    <div class="form-group">
                        <#if !isEditPage??><label for="rating"
                                                  class="col-form-label"><@spring.message "review.rating"/></label></#if>
                        <input type="<#if isEditPage??>hidden<#else> number</#if>" class="form-control" name="rating"
                               id="rating"
                               placeholder="<@spring.message "review.placeholderRating"/>">
                    </div>

                    <div class="form-group">
                        <label for="title" class="col-form-label"><@spring.message "review.titleEdit"/></label>
                        <input type="text" class="form-control" name="title" id="title"
                               placeholder="<@spring.message "review.placeholderTitle"/>">
                    </div>

                    <div class="form-group">
                        <label for="comment" class="col-form-label"><@spring.message "review.comment"/></label>
                        <input type="text" class="form-control" name="comment" id="comment"
                               placeholder="<@spring.message "review.placeholderComment"/>">
                    </div>

                    <#if !isEditPage??>
                        <div class="form-group">
                            <label for="file" class="col-form-label"><@spring.message "review.photo"/></label>
                            <div class="custom-file">
                                <input type="file" name="photo" id="customPhoto"/>
                                <label class="custom-file-label"
                                       for="customPhoto"><@spring.message "review.placeholderPhoto"/></label>
                            </div>
                        </div>
                    </#if>

                    <#if isEditPage??>
                        <div class="form-group">
                            <label><input type="checkbox" name="active" id="active"> <@spring.message "review.allow"/> ?</label>
                        </div>
                    </#if>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span class="fa fa-close"></span>
                            <@spring.message "common.close"/>
                        </button>
                        <button type="button" class="btn btn-primary"
                                onclick="
                                <#if restaurant??>
                                        saveReview(${restaurant.getId()})
                                <#else>
                                        updateReview()
                                </#if> ">
                            <span class="fa fa-check"></span>
                            <@spring.message "common.save"/>
                        </button>
                    </div>
            </div>
        </div>
    </div>
</div>
