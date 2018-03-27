<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getHeader("x-forwarded-proto") + "://" + request.getServerName() + request.getContextPath() + "/";
%>
<jsp:include page="header.jsp"></jsp:include>

<!DOCTYPE HTML">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<base href="<%=basePath%>">

</head>


<body>

<script type="text/javascript">
	$(document).ready(function(){
		$("#btn_login").click(function(){
			location.href = "/login.jsp";
		})
		$("#btn_signup").click(function(){
			var struname = $.trim($("#inputUser").val());
			var strpwd = $.trim($("#inputPassword").val());
			var strvalidpwd = $.trim($("#ValidPassword").val());
			if (struname.length == 0)
			{
				$("#status").html("用户名不能为空");
			}
			else
			{
				if (strpwd.length == 0 || strvalidpwd.length == 0 || strpwd != strvalidpwd)
				{
					$("#status").html("密码设置错误，请重新设置");
				}
				else
				{
					$.ajax({
						url: "/SqlCommand",
						data:	{command:"1", uname:struname, pwd: strpwd},
						type: "post",
						success: function(result){
							if (result == 1)
							{
								location.href = "/login.jsp";
							}
							else if (result == 0)
							{
								$("#status").html("存在此用户，请重新输入用户名！");
							}
						}
					});
				}
			}


		});
	});
</script>
			<div class="container">
		<div class="form-group row">
			<input type="text"  class="form-control" name="uname"  id="inputUser"  value=""  placeholder="用户名">
			<input type="password"  class="form-control"  name="pwd"  id="inputPassword" value="" placeholder="密码">
			<input type="password"  class="form-control"  name="validpwd"  id="ValidPassword" value="" placeholder="密码验证">
			<input type="button" class="btn btn-default	btn-lg pull-right" id="btn_signup" value="注册">
		<div>
			<h1><label id="status" ></label></h1>
		</div>
		</div>
		</div>
</body>
</html>
