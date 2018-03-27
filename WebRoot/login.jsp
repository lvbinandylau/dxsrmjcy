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

            <style>
            .UserHelp
            {
                text-align: center;
            }
            .login_a{
                color: #000000;
                font-size: 25px;
            }
            </style>
        </head>

        <body>
            <form class="form-horizontal" role="form" action="/login" method="post">
                <div class="container">
                    <div class="form-group row">
                        <input type="text" class="form-control" name="name" id="inputUser" placeholder="用户名" tabindex="1">
                        <input type="password" class="form-control" name="pwd" id="inputPassword" placeholder="******" tabindex="2">
                        <input type="submit" class="btn btn-default btn-lg pull-right" id="btn_login" value="登录" tabindex="3">
                    </div>
                </div>
            </form>
            <div class="UserHelp">
                <h1>使用说明</h1>
                <a class="login_a" href="help1.html" target="_blank">系统简介、注册以及登录</a>
                <br/>
                <a class="login_a" href="help2.html" target="_blank">系统首页、上传</a>
                <br/>
                <a class="login_a" href="help3.html" target="_blank">微信公众号文章编辑</a>
                <br/>
            </div>
            <script>
            $(function(){
                $("#inputUser").focus();
            })
            </script>
        </body>

        </html>
