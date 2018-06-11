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

        <!-- Bootstrap Core CSS -->
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/default.css">

        <script type="text/javascript" src="<%=contextPath%>/app/app.nocache.js"></script>

        <script type="text/javascript">
            erraiJaxRsApplicationRoot = "<%=contextPath%>/rest";
            erraiJaxRsJacksonMarshallingActive = true;
        </script>
    </head>
    <body>
        <div id="rootPanel" class="container"></div>
        
        <iframe src="javascript:''" id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

        <script src="<%=contextPath%>/js/jquery.js"></script>
        <script src="<%=contextPath%>/js/bootstrap.min.js"></script>
    </body>
</html>
