<#import "parts/common.ftl" as c>
<#--    https://stackoverflow.com/a/3154881-->
<#import "/spring.ftl" as spring/>


<@c.page>
<h5><@spring.message "app.greeting"/></h5>
    <div><@spring.message "app.description"/></div>
</@c.page>