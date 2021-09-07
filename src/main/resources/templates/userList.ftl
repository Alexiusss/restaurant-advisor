<script defer type="text/javascript" src="../../static/js/users.js"></script>
<script defer type="text/javascript" src="../../static/js/common.js"></script>
<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>
<#import "/spring.ftl" as spring/>

<@c.page>

    <h3 class="text-center"><@spring.message "user.title"/></h3>
    <button class="btn btn-primary" onclick="add()">
        <span class="fa fa-plus"></span>
        <@spring.message "common.add"/>
    </button>
    <table class="table table-striped pb-3" id="datatable">
        <thead>
        <tr>
            <th><@spring.message "user.firstName"/></th>
            <th><@spring.message "user.lastName"/></th>
            <th><@spring.message "user.email"/></th>
            <th><@spring.message "user.role"/></th>
            <th><@spring.message "user.active"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
    </table>

    <div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="exampleModalCenterTitle"><@spring.message "user.editorTitle"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">

                    <div class="form-group">
                        <label for="firstName" class="col-form-label"><@spring.message "user.firstName"/></label>
                        <input type="text" class="form-control" name="firstName" id="firstName">
                    </div>

                    <div class="form-group">
                        <label for="lastName" class="col-form-label"><@spring.message "user.lastName"/></label>
                        <input type="text" class="form-control" name="lastName" id="lastName">
                    </div>


                    <div class="form-group">
                        <label for="email" class="col-form-label"><@spring.message "user.email"/></label>
                        <input type="text" class="form-control" name="email" id="email">
                    </div>

                    <div class="form-group">
                        <label for="password" class="col-form-label"><@spring.message "user.password"/></label>
                        <input type="password" class="form-control" name="password" id="password">
                    </div>

                    <div class="form-group">
                        <label for="password2" class="col-form-label"><@spring.message "user.password"/> 2</label>
                        <input type="password" class="form-control" name="password2" id="password2">
                    </div>

                    <div class="form-group">
                           <label><input type="checkbox" name="USER" id="USER" value="USER"> USER</label>
                           <label><input type="checkbox" name="ADMIN" id="ADMIN" value="ADMIN"> ADMIN</label>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <span class="fa fa-close"></span>
                            <@spring.message "common.close"/>
                        </button>
                        <button type="button" class="btn btn-primary" onclick="save()">
                            <span class="fa fa-check"></span>
                            <@spring.message "common.save"/>
                        </button>
                    </div>
            </div>
        </div>
    </div>
    <#include "parts/i18n.ftl">
</@c.page>