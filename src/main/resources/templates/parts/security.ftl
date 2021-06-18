<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication
    name = user.name
    isAdmin = user.getAuthorities()?seq_contains('ADMIN')
    >
<#else>
    <#assign
    name = "unkown"
    isAdmin = false
    >
</#if>