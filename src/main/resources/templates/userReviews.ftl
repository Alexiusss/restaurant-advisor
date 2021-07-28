<script type="text/javascript" src="../../static/js/common.js"></script>
<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <h3>${userChannel.getFirstName()} ${userChannel.getLastName()} </h3>
    <#if !isCurrentUser>
        <#if isSubscriber>
            <a class="btn btn-info" href="/user/unsubscribe/${userChannel.getId()}">Unsubscribe</a>
        <#else>
            <a class="btn btn-info" href="/user/subscribe/${userChannel.getId()}">Subscribe</a>
        </#if>
    </#if>
    <div class="container my-3">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/user/subscriptions/${userChannel.getId()}/list">${subscriptionsCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/user/subscribers/${userChannel.getId()}/list">${subscribersCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if isAdmin>
        <#include "parts/reviewEdit.ftl" />
    </#if>

    <#include "parts/reviewList.ftl" />

</@c.page>