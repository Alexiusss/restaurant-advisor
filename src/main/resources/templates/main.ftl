<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" placeholder="Search by restaurant name"
                       value="${filter!}"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="cuisine" class="form-control" placeholder="Search by cuisine"
                       value="${cuisine!}"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <#if isAdmin>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseRestaurant" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            Add new restaurant
        </a>
        <#include "parts/restaurantEdit.ftl"/>
    </#if>

    <div class="card-columns" id="restaurant-list">
        <#list page.content as restaurant>
            <div class="card my-3" data-id="${restaurant.getId()}">
                <div>
                    <#if restaurant.filename??>
                        <img src="/img/${restaurant.filename}" class="card-img-top"
                             onclick="window.location='/main/' + ${restaurant.getId()};"/>
                    </#if>
                </div>
                <div class="m-2">
                    <span>${restaurant.name}</span>
                </div>
                <#--                https://stackoverflow.com/questions/45523742/how-can-i-use-rate-yo-jquery-star-rating-plugin-with-data-attribute-->
                <#if (restaurant.rating() > 0)??>
                    <div>
                        <div class="rateyo"
                             data-rateyo-rating="${restaurant.rating()}"
                             data-rateyo-num-stars="5"
                             data-rateyo-read-only="true"></div>
                    </div>
                </#if>
                <div class="m-2">
                    <i>${restaurant.cuisine}</i>
                </div>
            </div>
        <#else>
            No restaurant
        </#list>
    </div>

    <@p.pager url page />

</@c.page>
<script>
    $(".rateyo").rateYo();
</script>