<#macro page>
    <!DOCTYPE html>
    <html lang="${rc.locale.language!}">

    <head>
        <meta charset="UTF-8"/>
        <title>Restaurant advisor</title>
        <link rel="stylesheet" href="/static/style.css"/>

        <base href="${rc.contextPath}">

        <#--        https://qna.habr.com/q/618257-->
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/rateYo/2.3.2/jquery.rateyo.min.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"
              integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU"
              crossorigin="anonymous">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css"
              integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l"
              crossorigin="anonymous"/>
        <link rel="stylesheet" href="webjars/datatables/1.10.20/css/dataTables.bootstrap4.min.css">

        <link rel="stylesheet" href="webjars/noty/3.1.4/demo/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="webjars/noty/3.1.4/lib/noty.css"/>

        <script src="https://www.google.com/recaptcha/api.js"></script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/turbolinks/5.2.0/turbolinks.js"
                integrity="sha512-G3jAqT2eM4MMkLMyQR5YBhvN5/Da3IG6kqgYqU9zlIH4+2a+GuMdLb5Kpxy6ItMdCfgaKlo2XFhI0dHtMJjoRw=="
                crossorigin="anonymous" referrerpolicy="no-referrer"></script>

        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js, then Bootstrap JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <!-- Latest compiled and minified JavaScript -->
        <script defer src="https://cdnjs.cloudflare.com/ajax/libs/rateYo/2.3.2/jquery.rateyo.min.js"></script>
        <script defer src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
                integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
                crossorigin="anonymous"></script>
        <script defer src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.min.js"
                integrity="sha384-+YQ4JLhjyBLPDQt//I+STsc9iw4uQqACwlvpslubQzn4u2UU2UFM80nGisd026JF"
                crossorigin="anonymous"></script>
        <script defer type="text/javascript" src="webjars/datatables/1.10.20/js/jquery.dataTables.min.js"></script>
        <script defer type="text/javascript" src="webjars/datatables/1.10.20/js/dataTables.bootstrap4.min.js"></script>

        <script defer type="text/javascript" src="webjars/noty/3.1.4/lib/noty.min.js"></script>
    </head>
    <body>
    <#include "navbar.ftl">
    <div class="container mt-5">
        <#nested>
    </div>

    <#if review?? || reviewEdit??>
        <script>
            // https://github.com/twbs/bootstrap/issues/3902#issuecomment-17786002
            setTimeout(function () {
                $('#exampleModalCenter').modal('show')
            }, 10)
        </script>
    </#if>
    </body>

    <script>
        $.ajaxSetup({
            headers: {
                'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
            }
        });
    </script>
    </html>
</#macro>