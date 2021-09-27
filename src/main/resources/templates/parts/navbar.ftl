<#include "security.ftl">
<#import "login.ftl" as l>
<#import "/spring.ftl" as spring/>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#"><@spring.message "app.title"/></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/"><@spring.message "app.home"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/restaurants"><@spring.message "restaurant.title"/></a>
            </li>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/users"><@spring.message "user.title"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/reviews"><@spring.message "review.title"/></a>
                </li>
            </#if>
            <#if name!="">
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile"><@spring.message "app.profile"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="/user-reviews/${currentUserId}"><@spring.message "review.usersReview"/></a>
                </li>
            </#if>
        </ul>
        <div class="navbar-text mr-3">${name}</div>
        <@l.logout/>
        <div class="nav-item dropdown">
            <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown">${rc.locale.language!}</a>
            <div class="dropdown-menu">
                <a class="dropdown-item" onclick="show('en')">English</a>
                <a class="dropdown-item"onclick="show('ru')">Русский</a>
            </div>
        </div>
    </div>
</nav>
<#--https://stackoverflow.com/a/28662284 -->
<script type="text/javascript">
    function show(lang) {
        window.location.href = window.location.href.split('?')[0] + '?lang=' + lang;
    }
</script>