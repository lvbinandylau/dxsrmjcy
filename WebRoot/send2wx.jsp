<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getHeader("x-forwarded-proto") + "://" + request.getServerName() + request.getContextPath() + "/";
%>
<jsp:include page="header.jsp"></jsp:include>
<!DOCTYPE HTML>
<html lang="zh-cn">

  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <base href="<%=basePath%>"/>

    <script type="text/javascript" charset="utf-8" src="myjs/jquery.Jcrop.js"></script>
    <script type="text/javascript" charset="utf-8" src="myjs/jquery.mixitup.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="myjs/jquery.cookie-1.4.1.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="myjs/send2wx_custom.js"></script>

    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <link href="mycss/my.css" rel="stylesheet" type="text/css"/>
    <link href="mycss/send2wx.css" rel="stylesheet" type="text/css"/>
    <link href="mycss/jquery.Jcrop.min.css" rel="stylesheet" type="text/css"/>
  </head>
  <body>
    <div id="full-page" class="container-fluid">
      <div id="dialog">
        <p>请输入标题和作者</p>
        <form id="validateForm">
          <label>标题</label>
          <input type="text" name="title" id="title" class="text ui-widget-content ui-corner-all"/>
          <label>作者</label>
          <input type="text" name="author" id="author" class="text ui-widget-content ui-corner-all"/>
          <label class="status"></label>
        </form>
      </div>
      <div class="w0 p-r clearfix">
        <div class="w1 fl">
          <div class="n1">分类</div>
          <ul class="n1-1" id="style-categories">
            <li>
              <a href="javascript:void(0)" onclick="getContent()" role="tab" data-toggle="tab">上传至微信</a>
            </li>
          </ul>
        </div>
        <div id="content" class="w6 fl">
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-005785.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-043918.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-014263.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-094178.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-005785_crop.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-070965.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <img src="https://www.dxsrmjcy.com.cn/uploads/image/20170928-214805-041548.jpg" style="max-width:100%;max-height:100%"/>
            </p>
            <p>
              <br/>
            </p>
        </div>
        <div class="w4 fl" id="ContentIMG">
          <div style="text-align:center;">
            <button onclick="GetContent()">获取封面图片</button>
          </div>
          <div id="IMGHeader"></div>
        </div>
        <div class="w3 fl">
          <div id="dimgcrop" >
            <img onclick="CoverIMGCrop()" id="imgcrop" />
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
