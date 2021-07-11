<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>

    <#if isAdmin>
        <#include "parts/reviewEdit.ftl" />
    </#if>

    <#include "parts/reviewList.ftl" />

</@c.page>