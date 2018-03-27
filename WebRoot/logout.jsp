<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getHeader("x-forwarded-proto") + "://" + request.getServerName() + path + "/";
%>
<jsp:include page="header.jsp"></jsp:include>

<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<base href="<%=basePath%>">
<script src="myjs/jquery-3.2.1.min.js"></script>
</head>
<body>
	<script>
	$(document).ready(function(){
		$.get("logout");
		window.location.href = "login.jsp";
	});
	</script>
</body>

</html>
