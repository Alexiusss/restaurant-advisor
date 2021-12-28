<#import "/spring.ftl" as spring/>

<div class="modal fade" tabindex="-1" id="editContactModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="editContactModalTitle"><@spring.message "contact.editTitle"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsFormContact">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <div class="form-group">
                        <label for="address" class="col-form-label"><@spring.message "contact.address"/></label>
                        <input type="text" class="form-control" name="address" id="address"
                               placeholder="<@spring.message "contact.address"/>">
                    </div>
                    <div class="form-group">
                        <label for="website" class="col-form-label"><@spring.message "contact.website"/></label>
                        <input type="url" class="form-control" name="website" id="website"
                               placeholder="<@spring.message "contact.website"/>">
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-form-label"><@spring.message "contact.email"/></label>
                        <input type="email" class="form-control" name="email" id="email"
                               placeholder="<@spring.message "contact.email"/>">
                    </div>
                    <div class="form-group">
                        <label for="phone_number"
                               class="col-form-label"><@spring.message "contact.phone_number"/></label>
                        <select id="country" content="form-control">
                            <option value="1">+1</option>
                            <option value="2">+12</option>
                            <option value="3">+123</option>
                            <option value="4">+1234</option>
                        </select>
                        <input type="tel" class="form-control" name="phone_number" id="phone_number"
                               pattern="([\+]*[0-9]{1,4}\s?[\(]*\d[0-9]{2,4}[\)]*\s?\d{3}[-]*\d{2}[-]*\d{2})"
                               placeholder="+1 (234) 567-89-10">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span class="fa fa-close"></span>
                            <@spring.message "common.close"/>
                        </button>
                        <button type="button" class="btn btn-primary"
                                onclick="saveContact(${restaurant.getId()})">
                            <span class="fa fa-check"></span>
                            <@spring.message "common.save"/>
                        </button>
                    </div>
            </div>
        </div>
    </div>
</div>
