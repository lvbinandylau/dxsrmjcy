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

        <script src="myjs/js_jqgrid/jquery.jqGrid.js"></script>
        <script src="myjs/js_jqgrid/jquery.tablednd.js"></script>
        <script src="myjs/js_jqgrid/jquery.contextmenu.js"></script>
        <script src="myjs/grid.locale-cn.js"></script>

        <link rel="stylesheet" type="text/css" href="mycss/ui.jqgrid.css" />
        <link rel="stylesheet" type="text/css" href="mycss/ui.jqgrid-bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="mycss/ui.jqgrid-bootstrap-ui.css" />
        <link rel="stylesheet" type="text/css" href="mycss/ui.multiselect.css" />
        <link rel="stylesheet" type="text/css" href="mycss/ui.jqgrid.mycustom.css" />

    </head>

    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-4">
                    <table class="table table-bordered table-condensed table-responsive " id="table-info">
                        <caption class="mycaption">基本信息</caption>
                        <tbody>
                            <tr>
                                <td class="td_left">用户名</td>
                                <td class="td_right" id="tid"></td>
                            </tr>
                            <tr>
                                <td class="td_left">文件总数</td>
                                <td class="td_right" id="tcount"></td>
                            </tr>
                            <tr>
                                <td class="td_left">空间容量</td>
                                <td class="td_right">1024M</td>
                            </tr>
                            <tr>
                                <td class="td_left">已使用</td>
                                <td class="td_right" id="tused"></td>
                            </tr>
                            <tr>
                                <td class="td_left">剩余</td>
                                <td class="td_right" id="tleft"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-xs-2">
                    <p class="myp">系统所有用户名单</p>
                    <select class="selector" id="selectUser"></select>
                </div>
            </div>

            <div id="jqGridPager"></div>
            <table id="jqGrid"></table>
            <br />
        </div>
        <script type="text/javascript">
            $(document).ready(function() {
                var uid = "${dxsrmjcy_UID}";
                url = "/SqlCommand";
                $.get(
                    url, {
                        command: "5",
                        uid: uid
                    },
                    function(result) {
                        jsonString = result;
                        var jsonArray = JSON.parse(jsonString);
                        document.getElementById("tid").innerHTML = jsonArray[0].user;
                        document.getElementById("tcount").innerHTML = jsonArray[0].fileCount;
                        var nused = jsonArray[0].totalSize;
                        if (nused == null) {
                            document.getElementById("tused").innerHTML = "0";
                            document.getElementById("tleft").innerHTML = "空间里什么都没有，赶紧上传点东西吧";
                        } else if (nused < 1024) //小于1k
                        {
                            document.getElementById("tused").innerHTML = nused + "B";
                            document.getElementById("tleft").innerHTML = "空间很充足，不要怕，随便上传";
                        } else if (nused < 1048576) {
                            nused /= 1024;
                            nused = nused.toFixed(2);
                            document.getElementById("tused").innerHTML = nused + "K";
                            document.getElementById("tleft").innerHTML = "空间很充足，不要怕，随便上传";
                        } else if (nused < 1073741824) {
                            nused /= 1024;
                            nused /= 1024;
                            nused = nused.toFixed(2);
                            document.getElementById("tused").innerHTML = nused + "M";
                            var nleft = 1024 - nused;
                            document.getElementById("tleft").innerHTML = nleft + "M";
                        } else {
                            nused /= 1024;
                            nused /= 1024;
                            nused /= 1024;
                            nused = nused.toFixed(2);
                            document.getElementById("tused").innerHTML = nused + "G";
                            var nleft = 10 - nused;
                            document.getElementById("tleft").innerHTML = nleft + "G";
                        }
                    }
                );

                var uid = "${dxsrmjcy_UID}";
                var lastsel;
                $(window).resize(function() {
                    $("#jqGrid").setGridWidth($(document.body).width() - 25);
                });

                $.get(
                    "/SqlCommand", {
                        command: "3",
                        uid: uid
                    },
                    function(result) {
                        jsonString = result;
                        var jsonArray = JSON.parse(jsonString);
                        $.each(jsonArray, function(i, item) {
                            var option = $("<option>").val(1).text(item.user);
                            $(".selector").append(option);
                        });
                    }
                );
                var strURL = "/helloWorld?UID=" + uid;
                $("#jqGrid").jqGrid({
                    url: strURL,
                    datatype: "json",
                    colModel: [{
                        label: 'ID',
                        name: 'id',
                        width: 45,
                        key: true,
                        hidden: true
                    }, {
                        label: '文件名称',
                        name: 'orifilename',
                        width: 30,
                        align: "center",
                        formatter: editLink,
                    }, {
                        label: '上传时间',
                        name: 'uploaddate',
                        width: 25,
                        align: "center",
                    }, {
                        label: '备注',
                        name: 'commit',
                        width: 100,
                        editable: true,
                        align: "center",
                    }, ],
                    autowidth: true,
                    height: "100%",
                    loadonce: true,
                    viewrecords: true,
                    rowNum: 20,
                    rowList: [20, 40, 60],
                    rownumbers: true,
                    rownumWidth: 25,
                    multiselect: true,
                    cellEdit: true,
                    cellurl: "/SqlCommand?command=2",
                    pager: "#jqGridPager",
                    toolbar: [true, "top"],
                    onSelectRow: function(id) {
                        if (id && id !== lastsel) {
                            $('#jqGrid').jqGrid('restoreRow', lastsel);
                            //$('#jqGrid').jqGrid('editRow', id, true);
                            lastsel = id;
                        }
                    },
                    editurl: "/SqlCommand?command=0",
                });
                jQuery("#jqGrid").jqGrid('navGrid', '#jqGridPager', {
                    edit: false,
                    add: false,
                    del: true
                });
                $("#t_jqGrid").append("<button type='button' id='share' onclick='ShareFile()' class='btn btn-default'>共享</button>");
            });

            function editLink(cellValue, options, rowdata, action) {
                var dlurl = "<a id='mydl' href='" + rowdata['path'] + "'" + " download='" + rowdata['orifilename'] + "'>" + rowdata['orifilename'] + "</a>";
                return dlurl;
            }

            function ShareFile() {
                var grid = $("#jqGrid");
                var rowKey = grid.getGridParam("selrow");
                var uid = "${dxsrmjcy_UID}";
                if (!rowKey)
                    alert("请选择需要共享的文件");
                else {
                    var selectedIDs = grid.getGridParam("selarrrow");
                    var result = "";
                    for (var i = 0; i < selectedIDs.length; i++) {
                        result += selectedIDs[i] + ",";
                        //将选中的文件发送给选中的人
                    }
                    result = result.substr(0, result.length - 1);
                    var strTo = $("#selectUser").find("option:selected").text();
                    $.ajax({
                            url: "/SqlCommand",
                            data: {
                                command: "4",
                                from: uid,
                                to: strTo,
                                fileIDs: result
                            },
                            type: "POST"
                        })
                        .done(function(data) {
                            if (data == 1)
                                alert("共享文件成功！");
                        });
                }
            }
        </script>
    </body>

    </html>