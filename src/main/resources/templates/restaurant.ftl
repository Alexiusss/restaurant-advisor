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

        <div class="card text-right" style="height: 15rem">
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

        <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalCenterTitle">New review</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <form method="post" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label for="rating" class="col-form-label">Rating</label>
                                    <input type="number" id="rating" name="rating" class="form-control"
                                           placeholder="From 1 to 5">
                                </div>
                                <div class="form-group">
                                    <label for="rating" class="col-form-label">Title</label>
                                    <input type="text" id="title" name="title" class="form-control"
                                           placeholder="Short title for your review">
                                </div>
                                <div class="form-group">
                                    <label for="comment" class="col-form-label">Comment</label>
                                    <input type="text" if="comment" name="comment" class="form-control"
                                           placeholder="Comment">
                                </div>
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
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

        <#list restaurant.getReviews() as review>
            <a class="list-group-item list-group-item-action flex-column align-items-start">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1">${review.getTitle()}</h5>
                    <small class="text-muted">${review.getDate().year}</small>
                </div>
                <p class="mb-1">${review.getComment()}</p>
                <small class="text-muted">Some text?</small>
            </a>
        </#list>
    </div>
</@c.page>