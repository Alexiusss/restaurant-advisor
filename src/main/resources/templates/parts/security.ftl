<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUser().getFirstName()
    isAdmin = user.getAuthorities()?seq_contains('ADMIN')
    currentUserId = user.getUser().getId()
    currentUserActive = user.getUser().isActive()
    currentPageUrl = springMacroRequestContext.getRequestUri()
    >
<#else>
    <#assign
    name = ""
    isAdmin = false
    currentUserId = -1
    currentUserActive = false
    currentPageUrl = ""
    >
</#if>