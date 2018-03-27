<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getHeader("x-forwarded-proto") + "://" + request.getServerName() + request.getContextPath() + "/";
%>
        <jsp:include page="header.jsp"></jsp:include>

        <!DOCTYPE html>
        <html lang="zh-cn">

        <head>
            <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <base href="<%=basePath%>">
            <script src="myjs/jquery-ui.min.js"></script>
            <script src="myjs/plupload.full.min.js"></script>
            <script src="myjs/jquery.ui.plupload.js"></script>
            <script src="myjs/plupload_zh_CN.js"></script>
            <link rel="stylesheet" href="mycss/jquery-ui.css" type="text/css" />
            <link rel="stylesheet" href="mycss/jquery-ui.theme.css" type="text/css" />
            <link rel="stylesheet" href="mycss/jquery.ui.plupload.css" type="text/css" />
            <title>文件上传</title>

            <style>
                .plupload_wrapper * {
                    background: rgba(255, 255, 255, 0.1);
                }

                .plupload_container .plupload_header {
                    background: rgba(255, 255, 255, 0.2);
                }
            </style>
            <!-- production -->

        </head>

        <body>
            <div class="container-fluid" id="uploader">你的浏览器没有安装Flash，Silverlight或者不支持HTML5.</div>

            <script type="text/javascript">
                // Initialize the widget when the DOM is ready
                $(function () {
                    $("#uploader").plupload({
                        // General settings
                        runtimes: 'html5,html4',
                        url: '/UploadServlet',
                        multipart: true,
                        // Maximum file size
                        max_file_size: '1000mb',

                        // Specify what files to browse for
                        filters: {
                            //mime_types: "image/*,application/zip,application/msword,application/pdf,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/x-7z-compressed,application/x-rar-compressed",
                        },

                        resize: {
                            width: 700,
                            height: 700,
                            crop: false,
                            quality: 90,
                            preserve_headers: false
                        },
                        // Rename files by clicking on their titles
                        rename: true,

                        // Sort files
                        sortable: true,

                        // Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
                        dragdrop: true,

                        // Views to activate
                        views: {
                            list: true,
                            thumbs: true, // Show thumbs
                            active: 'thumbs'
                        },
                    });
                });
            </script>
        </body>

        </html>