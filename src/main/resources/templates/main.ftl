<script type="text/javascript" src="../../static/js/common.js"></script>
<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<#import "parts/pager.ftl" as p>
<#import "/spring.ftl" as spring/>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" placeholder="<@spring.message "restaurant.searchByName"/>"
                       value="${filter!}"/>
                <button type="submit" class="btn btn-primary ml-2"><@spring.message "common.search"/></button>
            </form>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="cuisine" class="form-control" placeholder="<@spring.message "restaurant.searchByCuisine"/>"
                       value="${cuisine!}"/>
                <button type="submit" class="btn btn-primary ml-2"><@spring.message "common.search"/></button>
            </form>
        </div>
    </div>

    <#if isAdmin>
        <a class="btn btn-primary" data-toggle="collapse" href="#collapseRestaurant" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            <@spring.message "restaurant.add"/>
        </a>
        <#include "parts/restaurantEdit.ftl"/>
    </#if>

    <div class="card-columns" id="restaurant-list">
        <#list page.content as restaurant>
            <div class="card my-3" id="restaurant-card_${restaurant.getId()}">
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
                <#if (restaurant.getRating() > 0)??>
                    <div>
                        <div class="rateyo"
                             data-rateyo-rating="${restaurant.getRating()}"
                             data-rateyo-num-stars="5"
                             data-rateyo-read-only="true"></div>
                    </div>
                </#if>
                <row>
                <div class="m-2">
                    <i>${restaurant.cuisine}</i>
                </div>
                <#if isAdmin>
                    <div class="float-right">
                    <button class="btn btn-outline-danger" id="deleteRestaurant" onclick="deleteRestaurant(${restaurant.getId()}, '${restaurant.getName()}')">
                        <@spring.message "common.delete"/>
                    </button>
                    </div>
                </#if>
                </row>
            </div>
        <#else>
            No restaurant
        </#list>
    </div>
    <@p.pager url page />
</@c.page>
<#include "parts/i18n.ftl">
<script>
    setTimeout(function(){$(".rateyo").rateYo()}, 10)
</script>