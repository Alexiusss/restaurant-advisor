<#import "parts/common.ftl" as c>
<#--    https://stackoverflow.com/a/3154881-->
<#import "/spring.ftl" as spring/>


<@c.page>
<h5><@spring.message "app.greeting"/></h5>
    <div class="container">
    <div><@spring.message "app.description"/></div>
    <a class="btn  btn-success my-4" href="swagger-ui.html" target="_blank">Swagger REST Api Documentation</a>
    </div>
</@c.page>