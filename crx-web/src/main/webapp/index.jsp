<!doctype html>
<%
    String contextPath = getServletContext().getContextPath();
%>

<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Special Calculator Application">

        <title>Rain Volume Calculator</title>

        <script type="text/javascript"  src="app/app.nocache.js"></script>


        <script type="text/javascript">
            erraiJaxRsApplicationRoot = "<%=contextPath%>/rest";
            erraiJaxRsJacksonMarshallingActive = true;
        </script>
    </head>
    <body>
        <iframe src="javascript:''" id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
    </body>
</html>
