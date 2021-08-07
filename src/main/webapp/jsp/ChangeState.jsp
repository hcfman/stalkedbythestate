<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<script type="text/javascript" src="js/jquery.js"></script>
<style type="text/css">
	body {
		padding-top: 8px;
	}
	
    .button {
            margin: 20px 10%;
            padding: 14px 15px;
            text-align: center;
            font-family: helvetica, arial, sans-serif;
            font-size: 50px;
            text-shadow: #000 0px 2px 2px;
            background-color: #5b5b5b;
            cursor: pointer;
            border-radius: 6px;
            -moz-border-radius: 6px;
            -webkit-border-radius: 6px;
            color: white;
    }

    .button-active {
            text-shadow: black 0px 1px 1px;
            background: #0361ba url(images/ListItemBackgroundHilite.gif) repeat-x;
    }

    .button-error {
            text-shadow: black 0px 1px 1px;
            background: #b90e03 url(images/ListItemBackgroundErrorHilite.gif) repeat-x;
    }
</style>
</head>
<body>

<jsp:include page="/jsp/content/ChangeState.jsp" />
</body>
</html>
