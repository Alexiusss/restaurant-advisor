<#include "security.ftl">
<#import "/spring.ftl" as spring/>

<div class="modal fade " id="exampleModalCenter" tabindex="-1" role="dialog"
     aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle"><@spring.message "review.editTitle"/></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <form method="post"
                          <#if !reviewEdit?? && restaurant??>action="/main/${restaurant.getId()}/review"</#if>
                          enctype="multipart/form-data">
                        <#if !reviewEdit??>
                        <div class="form-group">
                            <label for="rating" class="col-form-label"><@spring.message "review.rating"/></label>
                            <input type="number"
                                   class="form-control ${(ratingError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getRating()}</#if>"
                                   id="rating" name="rating"
                                   placeholder="<@spring.message "review.placeholderRating"/>">
                            <#if ratingError??>
                                <div class="invalid-feedback">
                                    ${ratingError}
                                </div>
                            </#if>
                        </div>
                        </#if>
                        <div class="form-group">
                            <label for="title" class="col-form-label"><@spring.message "review.titleEdit"/></label>
                            <input type="text" class="form-control ${(titleError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getTitle()}</#if>"
                                   id="title" name="title"
                                   placeholder="<@spring.message "review.placeholderTitle"/>"/>
                            <#if titleError??>
                                <div class="invalid-feedback">
                                    ${titleError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <label for="comment" class="col-form-label"><@spring.message "review.comment"/></label>
                            <input type="text" class="form-control ${(commentError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getComment()}</#if>"
                                   id="comment" name="comment"
                                   placeholder="<@spring.message "review.placeholderComment"/>">
                            <#if commentError??>
                                <div class="invalid-feedback">
                                    ${commentError}
                                </div>
                            </#if>
                        </div>
                        <#if !reviewEdit??>
                        <div class="form-group">
                            <label for="file" class="col-form-label"><@spring.message "review.photo"/></label>
                            <div class="custom-file">
                                <input type="file" name="photo" id="customPhoto"/>
                                <label class="custom-file-label" for="customPhoto"><@spring.message "review.placeholderPhoto"/></label>
                            </div>
                        </div>
                        </#if>
                        <#if isAdmin && reviewEdit??>
                            <div class="form-group">
                                <label><input type="checkbox" class="form-check" name="active"
                                              id="active" ${reviewEdit.isActive()?string("checked", "")}><@spring.message "review.allow"/></label>
                            </div>
                        <#else>
                            <input type="hidden" name="active" id="active" value="<#if isAdmin>true<#else>false</#if>">
                        </#if>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="hidden" name="id"
                               value="<#if reviewEdit?? && reviewEdit.getId()??>${reviewEdit.getId()}</#if>">
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"><@spring.message "common.close"/></button>
                            <button type="submit" class="btn btn-primary"><@spring.message "common.save"/></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>