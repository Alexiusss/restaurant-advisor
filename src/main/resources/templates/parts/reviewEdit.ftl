<#include "security.ftl">

<div class="modal fade " id="exampleModalCenter" tabindex="-1" role="dialog"
     aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalCenterTitle">Reviews editor</h5>
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
                            <label for="rating" class="col-form-label">Rating</label>
                            <input type="number"
                                   class="form-control ${(ratingError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getRating()}</#if>"
                                   id="rating" name="rating"
                                   placeholder="From 1 to 5">
                            <#if ratingError??>
                                <div class="invalid-feedback">
                                    ${ratingError}
                                </div>
                            </#if>
                        </div>
                        </#if>
                        <div class="form-group">
                            <label for="title" class="col-form-label">Title</label>
                            <input type="text" class="form-control ${(titleError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getTitle()}</#if>"
                                   id="title" name="title"
                                   placeholder="Short title for your review"/>
                            <#if titleError??>
                                <div class="invalid-feedback">
                                    ${titleError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <label for="comment" class="col-form-label">Comment</label>
                            <input type="text" class="form-control ${(commentError??)?string('is-invalid', '')}"
                                   value="<#if reviewEdit??>${reviewEdit.getComment()}</#if>"
                                   id="comment" name="comment"
                                   placeholder="Comment">
                            <#if commentError??>
                                <div class="invalid-feedback">
                                    ${commentError}
                                </div>
                            </#if>
                        </div>
                        <#if !reviewEdit??>
                        <div class="form-group">
                            <label for="file" class="col-form-label">Photo</label>
                            <div class="custom-file">
                                <input type="file" name="photo" id="customPhoto"/>
                                <label class="custom-file-label" for="customPhoto">Choose photo</label>
                            </div>
                        </div>
                        </#if>
                        <#if isAdmin && reviewEdit??>
                            <div class="form-group">
                                <label><input type="checkbox" class="form-check" name="active"
                                              id="active" ${reviewEdit.isActive()?string("checked", "")}> Allow
                                    publication</label>
                            </div>
                        <#else>
                            <input type="hidden" name="active" id="active" value="<#if isAdmin>true<#else>false</#if>">
                        </#if>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="hidden" name="id"
                               value="<#if reviewEdit?? && reviewEdit.getId()??>${reviewEdit.getId()}</#if>">
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Save review</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>