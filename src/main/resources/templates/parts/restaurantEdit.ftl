<#import "/spring.ftl" as spring/>

<div class="modal fade" tabindex="-1" id="editRestaurantModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="editRestaurantModalTitle"><@spring.message "restaurant.editTitle"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsFormRestaurant">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <div class="form-group">
                        <label for="title" class="col-form-label"><@spring.message "restaurant.name"/></label>
                        <input type="text" class="form-control" name="name" id="name"
                               placeholder="<@spring.message "restaurant.name"/>">
                    </div>
                    <div class="form-group">
                        <label for="comment" class="col-form-label"><@spring.message "restaurant.cuisine"/></label>
                        <input type="text" class="form-control" name="cuisine" id="cuisine"
                               placeholder="<@spring.message "restaurant.cuisine"/>">
                    </div>
                    <div class="form-group">
                        <label for="file" class="col-form-label"><@spring.message "review.photo"/></label>
                        <div class="custom-file">
                            <input type="file" name="photo" id="customPhoto"/>
                            <label class="custom-file-label"
                                   for="customPhoto"><@spring.message "review.placeholderPhoto"/></label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span class="fa fa-close"></span>
                            <@spring.message "common.close"/>
                        </button>
                        <button type="button" class="btn btn-primary"
                                onclick="saveRestaurant()">
                            <span class="fa fa-check"></span>
                            <@spring.message "common.save"/>
                        </button>
                    </div>
            </div>
        </div>
    </div>
</div>
