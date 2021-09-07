<#--https://bbbootstrap.com/snippets/rating-and-review-system-user-comment-section-41283119-->
<#import "/spring.ftl" as spring/>
<div class="container-fluid px-1 py-5 mx-auto">
    <div class="row justify-content-center">
        <div class="row justify-content-left d-flex">
            <div class="col-md-4 d-flex flex-column">
                <div class="rating-box">
                    <h1 class="pt-4">${rating}</h1>
                    <p class=""><@spring.message "ratingCard.outOf"/>5</p>
                </div>
                <#if (rating > 0)??>
                    <div>
                        <div class="rateyo"
                             data-rateyo-rating="${rating}"
                             data-rateyo-num-stars="5"
                             data-rateyo-read-only="true"></div>
                    </div>
                </#if>
                <#assign reviews_count = page.content?size>
                <div class="text-center">
                    <@spring.message "ratingCard.count"/>${reviews_count}
                </div>
            </div>
            <div class="col-md-8">
                <div class="rating-bar0 justify-content-center">
                    <table class="text-left mx-auto">
                        <#list ratings as rating, count>
                            <#assign bar_width = count/reviews_count*100>
                            <#if rating==5>
                                <tr>
                                    <td class="rating-label"><@spring.message "ratingCard.excellent"/></td>
                                    <td class="rating-bar">
                                            <div class="bar-container">
                                                <div class="bar-5" style="width: ${bar_width}%"></div>
                                            </div>
                                    </td>

                                    <td class="text-right">${count}</td>
                                </tr>
                            </#if>

                            <#if rating==4>
                                <tr>
                                    <td class="rating-label"><@spring.message "ratingCard.good"/></td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div class="bar-4" style="width: ${bar_width}%"></div>
                                        </div>
                                    </td>
                                    <td class="text-right">${count}</td>
                                </tr>
                            </#if>

                            <#if rating==3>
                                <tr>
                                    <td class="rating-label"><@spring.message "ratingCard.average"/></td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div class="bar-3" style="width: ${bar_width}%"></div>
                                        </div>
                                    </td>
                                    <td class="text-right">${count}</td>
                                </tr>
                            </#if>

                            <#if rating==2>
                                <tr>
                                    <td class="rating-label"><@spring.message "ratingCard.poor"/></td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div class="bar-2" style="width: ${bar_width}%"></div>
                                        </div>
                                    </td>
                                    <td class="text-right">${count}</td>
                                </tr>
                            </#if>

                            <#if rating==1>
                                <tr>
                                    <td class="rating-label"><@spring.message "ratingCard.terrible"/></td>
                                    <td class="rating-bar">
                                        <div class="bar-container">
                                            <div class="bar-1" style="width: ${bar_width}%"></div>
                                        </div>
                                    </td>
                                    <td class="text-right">${count}</td>
                                </tr>
                            </#if>
                        </#list>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
