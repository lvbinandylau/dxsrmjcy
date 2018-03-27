<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getHeader("x-forwarded-proto") + "://" + request.getServerName() + path + "/";
%>

        <!DOCTYPE html>
        <html lang="zh-cn">

        <head>
            <meta charset="utf-8">
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
            <meta name="description" content="德兴市人民检察院">
            <meta name="author" content="lvbin">
            <meta http-equiv="pragma" content="no-cache">
            <meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
            <meta http-equiv="expires" content="Thu, 01 Jan 1970 00:00:01 GMT" />
            <base href="<%=basePath%>">

            <script src="myjs/jquery-1.12.4.min.js"></script>
            <script src="myjs/jquery-ui.min.js"></script>
            <script src="myjs/jquery.validate.js"></script>
            <script src="myjs/messages_zh.js"></script>
            <script src="myjs/bootstrap.min.js"></script>
            <link rel="stylesheet" type="text/css" href="mycss/jquery-ui.css" />
            <link rel="stylesheet" type="text/css" href="mycss/jquery-ui.theme.css" />
            <link rel="stylesheet" type="text/css" href="mycss/theme.css" />
            <link rel="stylesheet" type="text/css" href="mycss/bootstrap.css">
            <link rel="stylesheet" type="text/css" href="mycss/bootstrap-theme.css">
            <link rel="stylesheet" type="text/css" href="mycss/font-awesome.min.css">
            <link href="mycss/navbar.css" rel="stylesheet" type="text/css" />
            <link rel="shortcut icon" type="image/png" href="/favicon.png" />

            <title>德兴市人民检察院</title>

            <style type="text/css">
                body {
                    background-image: url(/pic/bg2.png);
                    background-size: cover;
                }

                .navbar-nav li:hover {
                    display: block;
                    background: darkgray;
                }

                .navbar-default {
                    background: rgba(0, 0, 0, 0.4);
                    margin-bottom: 0px;
                    border: 0px;
                }

                .navbar-default .navbar-nav>li>a {
                    color: #fff;
                }

                footer {
                    position: fixed;
                    bottom: 0;
                    width: 100%;
                    text-align: center;
                }

                a {
                    color: #FFFFFF;
                    font-size: 18px;
                }

                .form-control {
                    background: rgba(0, 0, 0, 0.0);
                    padding: 1px 1px 1px 1px;
                    margin-top: 4px;
                    margin-bottom: 4px;
                    color: blue;
                }

                .btn {
                    background: rgba(0, 0, 0, 0.0);
                    color: #fff;
                }

                .mycolor {
                    color: midnightblue;
                }

                .mycenter {
                    text-align: center;
                }
            </style>
        </head>

        <body>
            <footer>
                <a class="mycolor" href="http://www.miibeian.gov.cn">赣ICP备17009253号-1</a>
                <p class="mycenter mycolor">lvbin@2017</p>
            </footer>

            <div class="container-fluid">
                <!-- Static navbar -->
                <nav class="navbar navbar-default " role="navigation">
                    <div class="container-fluid">
                        <ul id="headerbar" class="nav navbar-nav navbar-right">
                            <li id="log_reg"><a href="reg.jsp"><i class="fa fa-user-o fa-fw" aria-hidden="true"></i>注册</a></li>
                            <li id="log_login"><a href="login.jsp"><i class="fa fa-sign-in fa-fw" aria-hidden="true"></i> 登录</a></li>
                            <li id="log_logout"><a href="logout.jsp"><i class="fa fa-sign-out fa-fw" aria-hidden="true"></i> 注销</a></li>
                        </ul>
                        <ul id="headerbar" class="nav navbar-nav">
                            <li id="index"><a href="index.jsp"><i class="fa fa-home fa-fw" aria-hidden="true"></i>首页</a></li>
                            <li id="upload"><a href="upload.jsp"><i class="fa fa-upload fa-fw" aria-hidden="true"></i>上传</a></li>
                            <li id="make"><a href="make.jsp"><i class="fa fa-cog fa-spin fa-fw"></i>微信网页制作</a></li>
                        </ul>
                        <!--/.nav-collapse -->
                    </div>
                    <!--/.container-fluid -->
                </nav>
            </div>
            <!-- /container -->

            <script>
                function getBrowerInfo() {
                    var Sys = {};
                    var ua = navigator.userAgent.toLowerCase();
                    var s;
                    (s = ua.match(/rv:([\d.]+)\) like gecko/)) ? Sys.ie = s[1]:
                        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
                        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
                        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
                        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
                        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

                    var BrowerInfo, BrowerVersion;
                    if (Sys.ie) {
                        BrowerInfo = 'IE';
                        BrowerVersion = Sys.ie
                    } else if (Sys.firefox) {
                        BrowerInfo = 'Firefox';
                        BrowerVersion = Sys.firefox
                    } else if (Sys.chrome) {
                        BrowerInfo = 'Chrome';
                        BrowerVersion = Sys.chrome
                    } else if (Sys.opera) {
                        BrowerInfo = 'Opera';
                        BrowerVersion = Sys.opera
                    } else if (Sys.safari) {
                        BrowerInfo = 'Safari';
                        BrowerVersion = Sys.safari
                    }
                    return ('当前浏览器为' + BrowerInfo + "；版本号为" + BrowerVersion);
                }

                function getSystemInfo() {
                    var isXP = true;
                    var agent = navigator.userAgent.toLowerCase();
                    var SystemInfo, CpuInfo
                    if (agent.indexOf("windows nt 5.") > -1) {
                        SystemInfo = 'XP';
                    } else if (agent.indexOf("windows nt 6.0") > -1) {
                        SystemInfo = 'Vista';
                    } else if (agent.indexOf("windows nt 6.1") > -1) {
                        SystemInfo = 'Win7';
                    } else if (agent.indexOf("windows nt 6.2") > -1) {
                        SystemInfo = 'Win8';
                    } else if (agent.indexOf("windows nt 6.3") > -1) {
                        SystemInfo = 'Win8.1';
                    } else if (agent.indexOf("windows nt 10.") > -1) {
                        SystemInfo = 'Win10';
                    }

                    if (agent.indexOf("win64") >= 0 || agent.indexOf("wow64") >= 0) CpuInfo = 'x64';
                    else CpuInfo = ' X86';
                    return ("当前操作系统为" + SystemInfo + ' ' + CpuInfo);
                }

                $(document).ready(function () {
                    var test = window.location.pathname;
                    if (test == "/" || test == "/login.jsp" || test == "/reg.jsp") {
                        document.getElementById("log_reg").style.display = "inline";
                        document.getElementById("log_login").style.display = "inline";
                        document.getElementById("log_logout").style.display = "none";
                        if (test == "/" || test == "/login.jsp") {
                            document.getElementById("log_login").setAttribute("class", "active");
                        } else {
                            document.getElementById("log_reg").setAttribute("class", "active");
                        }
                    } else {
                        document.getElementById("log_reg").style.display = "none";
                        document.getElementById("log_login").style.display = "none";
                        document.getElementById("log_logout").style.display = "inline";
                        if (test.indexOf("index.jsp") > 0) {
                            var div = document.getElementById('index');
                            div.setAttribute("class", "active");
                        } else if (test.indexOf("upload.jsp") > 0) {
                            var div = document.getElementById('upload');
                            div.setAttribute("class", "active");
                        } else if (test.indexOf("make.jsp") > 0) {
                            var div = document.getElementById('make');
                            div.setAttribute("class", "active");
                        }
                    }
                });
            </script>
        </body>

        </html>
