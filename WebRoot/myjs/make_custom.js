"use strict";

var count = 0;
var jcrop_api;
var ntrueWidth, ntrueHeight;
var strimgurl = "";
var img_x, img_y, img_w, img_h;
var current_edit_msg_id;
var strcontent = "";
var strtitle = "德兴市人民检察院";
var strauthor = "吕斌";
const NewsMaxCount = 9;

function WxNews(content, title, author, imgurl, x, y, w, h) {
    this.content = content;
    this.title = title;
    this.author = author;
    this.imgurl = imgurl;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.isNULL = true;
}

function reloadStyleOperate() {
    /*
              $('#style-operate-area').append(
                  '<div style="position:absolute; background: rgba(255,255,255,0.95);width: 100%;line-height: 65px;text-align: center" title="若自动刷新失败，请按“F5”手动刷新"><img src="' +
                  BASEURL + '/img/ajax/circle_ball.gif"> 正在加载最新数据...</div>').load(
                  '/beautify_editor #style-operate-area',
                  function () {
                      $('.editor-template-list > li').addClass('mix');
                      $('.editor-template-list').mixItUp({
                          layout: {
                              display: 'block'
                          },
                          callbacks: {}
                      });

                      //$('#left-side-affix').width($('#left-sidebar').width());
                      $('.popover-trigger').popover({
                          trigger: "hover"
                      });
                      $('#btn-help').popover();
                      $('#editor-type-pop').popover({
                          trigger: 'hover'
                      });
                      $(window).trigger('resize');
                  });
                  */
}

function openFeedsModal() {
    $('#feedsModal').modal('show');
}

function getEditorHtml() {
    //console.log("getEditorHtml");
    return UE.getEditor('editor').getContent();
}

function doUpload() {
    var formData = new FormData($("#uploadForm")[0]);
    $.ajax({
        url: 'UploadServlet',
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (returndata) {
            //显示上传的图片
            strimgurl = window.location.protocol + '//' + window.location.host + '/' + returndata;
            $("#imgcrop").attr("src", strimgurl);
            $("#coverimgcroped").attr("src", strimgurl);
        },
        error: function (returndata) {
            alert(returndata);
        }
    });
}

$(function () {
    var newscount = 0; //当前图文素材的个数
    current_edit_msg_id = 0;//当前图文素材的ID号
    $("#validateForm").validate();
    var myDate = new Date();
    var month = myDate.getMonth() + 1;
    var date = myDate.getDate();
    $("#wxdate").html(month + "年" + date + "日");
    var WxMaterialNews = new Array(NewsMaxCount); //创建最多9个微信图文素材的数组
    //计算wxnewstotal的width
    var addcover2_width = $("#addcover2").width();
    console.log(addcover2_width);
    var wxnewstotal_width = $("#addcover2").width() - $("#coverimgcroped").width();
    $("#wxnewstotal").width(wxnewstotal_width);
    
    /*
    $("#dialog").dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            "确认": function() {
                strcontent = UE.getEditor('editor').getContent();
                var strtitle = $("#title").val();
                var strauthor = $("#author").val();
                if ($.trim(strtitle).length > 0 || $.trim(strauthor).length > 0) {
                    $.post("WXServer", {
                        c: strcontent,
                        t: strtitle,
                        a: strauthor,
                        u: strimgurl,
                        x: img_x,
                        y: img_y,
                        w: img_w,
                        h: img_h
                    }, function(data) {
                        console.log(data);
                    });
                    $(this).dialog("close");
                } else
                    alert("请输入内容");
            },
            "取消": function() {
                $(this).dialog("close");
            }
        }
    });
    */
    $("#wxpreview_title").keyup(function () {
        strtitle = $("#wxpreview_title").val();
        $("#wxtitle").html(strtitle);
    });
    $("#wxpreview_author").keyup(function () {
        strauthor = $("#wxpreview_author").val();
    });

    $('#style-categories .filter').click(function () {
        $('#insert-style-list').scrollTop(0); //切换分类时，回到顶部
    });
    $(window).resize(function () {
        var win_height = $(window).height() - $(".navbar-default").height() - $("footer").height();
        $('#full-page').height(win_height);
        var area_height = win_height - 30;

        $('#editor,.edui-editor-iframeholder').height(area_height - 8);
        $('.item,.n1-1').height(area_height);

        $('.editor2').height(area_height);

        if ($(window).width() < $('body').width()) {
            $('body').scrollLeft($('body').width() - $(window).width());
        }

    }); // .trigger('resize');

    setTimeout(function () {
        $(window).trigger('resize');
    }, 500);

    $('.autonum').on('mousewheel', function (event) {
        if (event.deltaY < 0) { //向下滚动
            $(this).html(parseInt($(this).html()) - 1);
        } else {
            $(this).html(parseInt($(this).html()) + 1);
        }
        return false;
    }).tooltip({
        'title': '上下滚动鼠标，可调整序号大小',
        container: 'body'
    });
    window.onbeforeunload = function (event) {
        var html = getEditorHtml();
        if (html != "") {
            if (window.localStorage) {
                localStorage._135content = html;
                window.localStorage._edit_msg_id = current_edit_msg_id;
                event.returnValue = "即将离开页面，是否确认编辑的内容已使用并废弃？";
            }
        }
    };
    //$('#html-more-popover').data('content',$('#more-popover-content').html());
    /*$('#html-more-popover').popover({trigger:"hover"}).on('shown.bs.popover', function () {
                var $this = $(this);
                $('#more-popover .popover-content').html($('#more-popover-content').html());
              })*/
    $('.editor-template-list > li').addClass('mix');
    $('.editor-template-list').mixItUp({
        layout: {
            display: 'block'
        },
        callbacks: {}
    });

    //$('#left-side-affix').width($('#left-sidebar').width());
    $('.popover-trigger').popover({
        trigger: "hover"
    });
    $('#btn-help').popover();
    $('#editor-type-pop').popover({
        trigger: 'hover'
    });

    $("#imgcrop").click(function () {
        ntrueWidth = getNaturalWidth(strimgurl);
        ntrueHeight = getNaturalHeight(strimgurl);
        initJcrop();
    });
    $("#wxnewstotal").click(function (e) {
        var v_id = ($(e.target).attr('id'))[12];
        var nid = parseInt(v_id);
        if (nid <= newscount)
        {
            console.log(WxMaterialNews[nid - 1]);            
        }
    });
    $("#addnews").click(function () {
        if (newscount < NewsMaxCount) {
            getUEContent(); //获取素材内容
            WxMaterialNews[newscount] = new WxNews(strcontent, strtitle, strauthor, strimgurl, img_x, img_y, img_w, img_h);
            console.log(WxMaterialNews[newscount]);
            newscount++;
            current_edit_msg_id = newscount;
        } else
            alert("图文消息最大值为" + NewsMaxCount + "个，请勿再添加图文消息")
    });
    $("#delnews").click(function () {
        if (newscount > 0)
            newscount--;
    });
});

//实例化编辑器 建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
var ue = UE.getEditor('editor', {
    toolbars: [
        [
            'source', '|',
            'undo', 'redo', '|', 'fontfamily', 'fontsize', '|', 'blockquote', 'horizontal', '|', 'removeformat', 'formatmatch', 'template', '|', 'link', 'unlink', 'emotion', '|',
            'bold', 'italic', 'underline', 'forecolor', 'backcolor', 'fontborder', 'strikethrough', 'pasteplain', '|', 'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|', 'insertorderedlist', 'insertunorderedlist', '|',
            'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'cleardoc', '|',
            'simpleupload', 'insertimage', 'scrawl', 'insertvideo', 'music', 'background', '|',
            'preview', 'drafts'
        ]
    ]
});

function isFocus(e) {
    UE.dom.domUtils.preventDefault(e);
};

function setblur(e) {
    UE.getEditor('editor').blur();
    UE.dom.domUtils.preventDefault(e);
}

function insertHtml(value) {
    ue.execCommand('insertHtml', value);
}

function createEditor() {
    enableBtn();
    UE.getEditor('editor');
}

function getAllHtml() {
    alert(UE.getEditor('editor').getAllHtml());
}

function getUEContent() {

    strcontent = UE.getEditor('editor').getContent();
    /*
    if (strcontent.length > 0) {
        $("#dialog").dialog("open");
    } else {
        alert("请勿提交空内容！");
    }
    */
};

function setUEContent(content) {

    strcontent = UE.getEditor('editor').setContent(content);
}

function Send2WX() {
    strcontent = UE.getEditor('editor').getContent();
    if (strcontent.length > 0) {
        $("#dialog").dialog("open");
    } else {
        alert("请勿提交空内容！");
    }
}

function GetIMGContent() {
    //先销毁jcrop
    if (jcrop_api) {
        jcrop_api.destroy();
    }
    //首先删除原来的元素
    $("#IMGHeader").empty();
    var ueContent = UE.getEditor('editor').getContent();
    var $jContent = $("<div id='tmp'></div>");
    $jContent.append(ueContent);
    var $imgs = $jContent.find("img");
    var nCount = $imgs.length;
    for (var j = 0; j < nCount; j++) {
        var $tmpFieldSet = $("<fieldset style=\"display:inline-block;text-align:center;\"></fieldset>");
        $tmpFieldSet.append($imgs[j]);
        $("#IMGHeader").append($tmpFieldSet);
        $("#IMGHeader").append("<br />");
    }
    console.log($("#IMGHeader").html());

    //$("#ContentIMG").append($("#IMGHeader").html());
}

function getPlainTxt() {
    var arr = [];
    arr.push("使用editor.getPlainTxt()方法可以获得编辑器的带格式的纯文本内容");
    arr.push("内容为：");
    arr.push(UE.getEditor('editor').getPlainTxt());
    alert(arr.join('\n'));
}

function setContent(isAppendTo) {
    var arr = [];
    arr.push("使用editor.setContent('欢迎使用ueditor')方法可以设置编辑器的内容");
    UE.getEditor('editor').setContent('欢迎使用ueditor', isAppendTo);
    alert(arr.join("\n"));
}

function setDisabled() {
    UE.getEditor('editor').setDisabled('fullscreen');
    disableBtn("enable");
}

function setEnabled() {
    UE.getEditor('editor').setEnabled();
    enableBtn();
}

function getText() {
    //当你点击按钮时编辑区域已经失去了焦点，如果直接用getText将不会得到内容，所以要在选回来，然后取得内容
    var range = UE.getEditor('editor').selection.getRange();
    range.select();
    var txt = UE.getEditor('editor').selection.getText();
    alert(txt);
}

function getContentTxt() {
    var arr = [];
    arr.push("使用editor.getContentTxt()方法可以获得编辑器的纯文本内容");
    arr.push("编辑器的纯文本内容为：");
    arr.push(UE.getEditor('editor').getContentTxt());
    alert(arr.join("\n"));
}

function hasContent() {
    var arr = [];
    arr.push("使用editor.hasContents()方法判断编辑器里是否有内容");
    arr.push("判断结果为：");
    arr.push(UE.getEditor('editor').hasContents());
    alert(arr.join("\n"));
}

function setFocus() {
    UE.getEditor('editor').focus();
}

function deleteEditor() {
    disableBtn();
    UE.getEditor('editor').destroy();
}

function getLocalData() {
    alert(UE.getEditor('editor').execCommand("getlocaldata"));
}

function clearLocalData() {
    UE.getEditor('editor').execCommand("clearlocaldata");
    alert("已清空草稿箱");
}
//获取图片原始尺寸
function getNaturalWidth(imgsrc) {
    var image = new Image();
    image.src = imgsrc;
    var naturalWidth = image.width;
    console.log("naturalWidth=" + naturalWidth);
    return naturalWidth;
}

function getNaturalHeight(imgsrc) {
    var image = new Image();
    image.src = imgsrc;
    var naturalHeight = image.height;
    console.log("naturalHeight=" + naturalHeight);
    return naturalHeight;
}
// The function is pretty simple
function initJcrop() {
    // Invoke Jcrop in typical fashion
    $('#imgcrop').Jcrop({
        onChange: setCoords,
        aspectRatio: 1.8,
        setSelect: [20, 20, 180, 100],
        trueSize: [ntrueWidth, ntrueHeight],
    }, function () {
        jcrop_api = this;
        jcrop_api.animateTo([20, 20, 180, 100]);
        jcrop_api.tellSelect();
    });
}

function setCoords(c) {
    img_x = Math.round(c.x);
    img_y = Math.round(c.y);
    img_w = Math.round(c.w);
    img_h = Math.round(c.h);
    //console.log(img_x + " " + img_y + " " + img_w + " " + img_h);
    var tmpa = img_w / 180;

    $("#coverimgcroped").css({
        width: Math.round(ntrueWidth / tmpa) + 'px',
        height: Math.round(ntrueHeight / tmpa) + 'px',
        marginLeft: '-' + Math.round(img_x / tmpa) + 'px',
        marginTop: '-' + Math.round(img_y / tmpa) + 'px',
    });

    //$("#coverimgcroped").attr("width", img_x);

}

function AddCover() {
    //更新ueditor的内容
    var strcontent = UE.getEditor('editor').getContent();
    $("#content").html(strcontent);
    $("#Ueditor").hide();
    $("#modelshow").hide();
    $("#addcover").hide();
    $("#content").show();
    $("#ContentIMG").show();
    $("#coverimgcrop").show();
    $("#send2wx").show();
    $("#ret2edit").show();
}

function returnedit() {
    $("#content").hide();
    $("#ContentIMG").hide();
    $("#coverimgcrop").hide();
    $("#ret2edit").hide();
    $("#send2wx").hide();
    $("#Ueditor").show();
    $("#modelshow").show();
    $("#addcover").show();
}

$(function () {
    /*
    $('.colorPicker').colorPicker({
        customBG: '#FFF',
        init: function(elm, colors) { // colors is a different instance (not connected to colorPicker)
                elm.style.backgroundColor = elm.value;
                elm.style.color = colors.rgbaMixCustom.luminance > 0.22 ?
                    '#222' :
                    '#ddd';
            }
    });

    $('.colorPicker').blur(function() {
        setBackgroundColor(this.value, '#FFF', true);
        this.style.backgroundColor = this.value;
        this.style.color = '#FFF';
    });
    $('.colorPicker').keyup(function() {
        if (this.value.search('#') == 0) {
            if (this.value.length == 7) { //|| this.value.length ==4
                $(this).trigger('focus.colorPicker');
            }
        } else {
            //alert(this.value.search('rgb'));alert(this.value.indexOf(")"));
            if (this.value.search('rgb') == 0 && this.value.indexOf(")") > 0) {
                $(this).trigger('focus.colorPicker');
            }
        }
    });
    */
    $(document).on('click', '#flat-strip-shadow', function () {
        var html = getEditorHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('*').css('box-shadow', '');
        var newHtml = obj.html();
        setEditorHtml(newHtml);
    });

    $(document).on('click', '#flat-add-shadow', function () {
        var html = getEditorHtml();
        var obj = $('<div>' + html + '</div>');

        obj.find('*').each(function () {
            if ($(this).css('background-color') == 'transparent' || $(this).css('background-color') == '') {} else {
                $(this).css('box-shadow', 'rgba(205, 205, 205,0.9) 2px 3px 2px');
            }
        });
        obj.find('hr').each(function () {
            $(this).css('box-shadow', 'rgba(205, 205, 205,0.9) 1px 1px 2px');
        });
        var newHtml = obj.html();
        setEditorHtml(newHtml);
    });

    $(document).on('click', '#set-image-radius', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('img').css('border-radius', '50%');
        setDealingHtml(obj.html());
        obj = null;
    });
    $(document).on('click', '#set-image-border', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('img').css({
            "background-color": "#fff",
            "border-radius": "4px",
            "max-width": "100%",
            "padding": "4px",
            "border": "1px solid #ddd"
        });
        setDealingHtml(obj.html());
        obj = null;
    });

    $(document).on('click', '#flat-add-radius', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('*').each(function () {
            if ($(this).css('background-color') != 'transparent' && $(this).css('background-color') != '') {
                $(this).css('border-radius', '4px');
            }
        });
        setDealingHtml(obj.html());
        obj = null;
    });

    $(document).on('click', '#flat-strip-radius', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('*').css('border-radius', '');
        setDealingHtml(obj.html());
        obj = null;
    });

    $(document).on('click', '#flat-add-border', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        obj.find('*').each(function () {
            if ($(this).css('background-color') != 'transparent' && $(this).css('background-color') != '') {
                $(this).css('border', '1px solid #efefef');
            }
        });
        //obj.find('.135editor').css('border','1px solid #efefef');
        setDealingHtml(obj.html());
        obj = null;
    });
    $(document).on('click', '#flat-strip-border', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');

        obj.find('*').each(function () {
            //if( $(this).css('background-color') == 'transparent' ||  $(this).css('background-color') == ''  ){ } else{
            $(this).css('border', '');
            $(this).css('border-width', '0');
            //}
        });

        setDealingHtml(obj.html());
        obj = null;
    });

    $(document).on('click', '#insert-style-list .ui-portlet-list > li', function () {
        //data-135editor=\"true\" 多插入一个空行，防止有时无法选择到底部了，或者两个元素中间不方便插入内容。
        if ($(this).hasClass('ignore')) {
            return false;
        }

        var ret = false;
        var num = parseInt($(this).find('.autonum:first').text());

        if (typeof num != "undefined") {
            $(this).find('.autonum:first').find('.autonum:first').text(num + 1);
        }

        var id = $(this).data('id');

        $(this).contents().filter(function () {
            return this.nodeType === 3 && $.trim($(this).text()) == "";
        }).remove();
        $(this).find('p').each(function () {
            if ($.trim($(this).html()) == "&nbsp;") {
                $(this).html('<br/>');
            }
        });
        $(this).find('*').each(function () {
            if ($(this).attr('data-width')) {
                return;
            }

            if (this.style.width && this.style.width != "") {
                $(this).attr('data-width', this.style.width);
            }
        });

        var style_item = $(this).find('.135editor:first');

        //change by lvbin@2017-10-12
        /*
        原来系统会修改<li></li>的class，将其修改成135editor,去掉，直接用原始的。
        */
        if (style_item.size()) {
            // insertHtml( $(this).html() + "<p>&nbsp;</p>"); 包含收藏夹的删除按钮等
            //ret = insertHtml("<fieldset data-id=\"" + id + "\"  style=\"border:0 none;\" class=\"135editor\">" + style_item.html() + "</fieldset><p><br/></p>");
            ret = insertHtml(style_item.html());
        } else { //最外围包装135editor容器
            //ret = insertHtml("<fieldset data-id=\"" + id + "\"  style=\"border:0 none;\" class=\"135editor\">" + $(this).html() + "</fieldset><p><br/></p>");
            ret = insertHtml($(this).html());
        }

        if (ret) {
            if (typeof num != "undefined") {
                $(this).find('.autonum:first').text(num + 1);
            }
            style_click($(this).data('id'));
        }

    });

    $(document).on('click', '#tab-tpl-trigger', function () { // 系统及收藏模板加载
        if ($('#editor-tpls-list').html() == "") {
            $('#editor-tpls-list').html('<img src="/img/ajax/circle_ball.gif"> 正在加载').load('/editor_styles/myTemplates?suffix=-1 #tpl-tab-content', function () {
                $('#editor-tpls-list').find('.col-md-3').removeClass('col-md-3').addClass('col-md-6');
                /*if( $('#favorite-tpl-list-1').find('.editor-style-content').size() ==0 ){
                                        	$('#favorite-tpl-list-li').remove();
                                        }
                                        if( $('#personal-tpl-list-1').find('.editor-style-content').size() ==0 ){
                                        	$('#personal-tpl-list-li').remove();
                                        }*/
                $('#editor-tpls-navtab a:first').tab('show');
            });
        }
    });

    $(document).on('click', '#tab-wxmsg-trigger', function () { // 我的图文加载
        //if($('#editor-msg-list').html()=="")
        {
            if ($('#editor-msg-list').find('.preloading').size() == 0) {
                $('#editor-msg-list').prepend('<div class="preloading"><img src="/img/ajax/circle_ball.gif"> 正在加载</div>');
            } else {
                $('#editor-msg-list').find('.preloading').show();
            }
            /*ajaxActionHtml('/wx_msgs/mine',null,function(html){
                              	//$('#editor-msg-list').html(html);


                              	$('<div>'+html+'</div>').find('.col-md-4').each(function(){
                              		$('.article-msg').appendTo('#editor-msg-list');
                              	});

                              	$('#editor-msg-list').html( $('<div>'+html+'</div>').find('#wxmsg-mine-list').html() );
                              	page_loaded();
                              });*/

            $('#editor-msg-list').html('<div class="preloading"><img src="/img/ajax/circle_ball.gif"> 正在加载</div>').load('/wx_msgs/mine #wxmsg-mine-list', function () {
                page_loaded();
                /*$('.article-msg').tooltip({'title':'点击插入重新编辑'}).on('shown.bs.tooltip', function (e) {
					  // do something…
					e.preventDefault();
	            	e.stopPropagation();
				});*/
            });
        }
    });
    $(document).on('click', '#editor-msg-list .pagelink a', function () {
        var url = $(this).attr('href');
        $('#editor-msg-list').html('<div class="preloading"><img src="/img/ajax/circle_ball.gif"> 正在加载</div>').load(url + ' #wxmsg-mine-list', function () {
            page_loaded();
        });
        return false;
    });

    $(document).on('click', '#tpl-tab-content .template-cover,#tpl-tab-content .editor-style-content', function () { //选中模板后设置编辑器的内容
        var tid = $(this).data('id');
        var obj = $('#template-' + tid);

        obj.contents().filter(function () {
            return this.nodeType === 3 && $.trim($(this).text()) == "";
        }).remove();
        obj.find('p').each(function () {
            if ($.trim($(this).html()) == "&nbsp;") {
                $(this).html('<br/>');
            }
        });

        setEditorHtml(obj.html());

        var id = parseInt(obj.data('id'));

        if (id > 0) {
            $('#template_id').val(id);
            $('#auto_cover_tpl').removeClass('hidden');
        } else {
            $('#template_id').val('');
            $('#auto_cover_tpl').addClass('hidden');
        }

        $('#template_name').val(obj.data('name'));
        $('#dialog-template-name').val(obj.data('name'));
        //$('#feedsModal').modal('hide');
    });

    $(document).on('click', '.delete-msg', function () {
        var $this = $(this);
        var url = $(this).data('url');
        if (confirm('是否确认删除这条图文消息')) {
            ajaxAction(url, null, null, function (request) {
                $this.parents('.article-msg:first').remove();
            });
        }
    });

    $(document).on('click', '#syncToWechat', function () {
        $("ol.sync-article-list").sortable("destroy");
        publishController.open_dialog('/wx_msgs/syncToWechat', {
            title: '图文同步保存到微信',
            selector: '#sync-wxmsg-list',
            width: 900,
            callback: function ($dialog) {
                var adjustment;
                var group_name = "sync-article-group-" + parseInt(Math.random() * 1000);
                $("ol.sync-article-list", $dialog).sortable({
                    group: group_name,
                    pullPlaceholder: false,
                    // animation on drop
                    onDrop: function (item, targetContainer, _super) {
                        var clonedItem = $('<li/>').css({
                            height: 0
                        });
                        item.before(clonedItem);
                        clonedItem.animate({
                            'height': item.height()
                        });

                        item.animate(clonedItem.position(), function () {
                            clonedItem.detach();
                            _super(item);
                        });
                    },

                    // set item relative to cursor position
                    onDragStart: function ($item, container, _super) {
                        var offset = $item.offset(),
                            pointer = container.rootGroup.pointer;

                        adjustment = {
                            left: pointer.left - offset.left,
                            top: pointer.top - offset.top
                        };

                        _super($item, container);
                    },
                    onDrag: function ($item, position) {
                        $item.css({
                            left: position.left - adjustment.left,
                            top: position.top - adjustment.top
                        });
                    }
                });
                $('#btn-sync-wechat', $dialog).click(function () {
                    if ($('#sync-selected > li', $dialog).size() < 1) {
                        alert("请先选择消息");
                        return false;
                    }

                    var id = '';
                    $('#sync-selected > li', $dialog).each(function () {
                        id += $(this).data('id') + ',';
                    });
                    ajaxAction('/wx_msgs/sync', {
                        ids: id
                    }, null, function (request) {
                        if (request.success) {
                            //$dialog.modal('hide');

                            var html = '<p class="alert alert-success">保存成功，扫描查看微信预览效果</p>';
                            html += '<img src="/wx_msgs/preview_qrcode?media_id=' + request.media_id + '&appid=' + request.appid + '" width="200">';
                            html += '<p><a href="/wx_msgs/wechat_preview?media_id=' + request.media_id + '&appid=' + request.appid + '" target="_blank">直接浏览器访问预览</a></p>';
                            $dialog.find('.modal-body').addClass('text-center').html(html);
                        }
                    });
                });
            }
        });
    });

    $(document).on('click', '.article-msg a', function (e) {
        e.stopPropagation();
        // e.preventDefault(); // 阻止冒泡但不屏蔽默认事件
    });

    $(document).on('click', '.article-msg,.article-msg .opr-edit', function () {
        var id = $(this).data('id');
        current_edit_msg_id = id;
        var url = BASEURL + '/wx_msgs/view/' + id + '.json?nolazy=1';
        ajaxAction(url, null, null, function (request) {
            if (request.data) {
                if (request.data.WxMsg.content) {
                    setEditorHtml(request.data.WxMsg.content);
                } else {
                    setEditorHtml("");
                }

            }
        });

    });

    $(document).on('click', '.color-swatch', function () {
        $('.color-swatch').removeClass('active');
        $(this).addClass('active');
        var color = $(this).data('color'); //data-color为前景色，bgcolor为背景色，或者无背景文字的前景色
        var bgcolor = $(this).css('backgroundColor');

        $('#custom-color-text').val(bgcolor).css('backgroundColor', bgcolor);

        if (!color)
            color = '#FFF';
        setBackgroundColor(bgcolor, color, true);
        if (!current_active_135item) {
            $('.editor-template-list li > .135editor').each(function () {
                parseObject($(this), bgcolor, color);
                $(this).attr('data-color', bgcolor);
            });
        }
    });
    /*var default_color = $('.default-color').css('backgroundColor');
                  $('.editor-template-list li > .135editor').each(function(){
                  	parseObject($(this),default_color,'#FFF');
                  	$(this).attr('data-color',default_color);
                  });*/

    $(document).on('click', '#135-random-transform', function () {
        var editor_document = current_editor.selection.document;
        var deg = parseInt(Math.random() * 8);
        var f = Math.random() * 10 > 5 ?
            '+' :
            '-';
        $(editor_document).find('.135editor').each(function (i) {
            if ((i + 1) % 3 == 0) {
                deg = parseInt(Math.random() * 8);
                f = Math.random() * 10 > 5 ?
                    '+' :
                    '-';
            }
            $(this).css('transform', 'rotate(' + f + deg + 'deg)').css('-webkit-transform', 'rotate(' + f + deg + 'deg)');
        });
    });

    $(document).on('click', '#135-random-color', function () {
        var html = getDealingHtml();
        var obj = $('<div>' + html + '</div>');
        var bgcolors = [
            '#5BB75B',
            '#2E8BCC',
            '#F09609',
            '#E51400',
            '#7B4F9D',
            '#E671B8',
            '#008641',
            '#20608E',
            '#FFC40D'
        ];

        var rd = Math.floor(Math.random() * (bgcolors.length));
        var used = [];
        var current_bgcolor = bgcolors[rd];
        var components = obj.find('.135editor').each(function (i) {
            if (i % random_color_step == 0) {
                do {
                    rd = Math.floor(Math.random() * (bgcolors.length));
                } while (jQuery.inArray(rd, used) != -1);

                used[used.length] = rd;
                if (used.length == bgcolors.length) {
                    used = [];
                }

                current_bgcolor = bgcolors[rd];
            }
            $(this).html(parseHtml($(this).html(), current_bgcolor, current_select_color));
        });

        setDealingHtml(obj.html());
        obj = null;
    });

    $('#clear-editor').click(function () {
        if (confirm('是否确认清空内容，清空后内容将无法恢复')) {
            current_edit_msg_id = null;
            setEditorHtml("");
        }
    });

    if (typeof localStorage.loadFeeds == "undefined" || window.localStorage && window.localStorage.loadFeeds == "true") {
        //openFeedsModal();
    }

    $('#preview-editor').click(function () {
        $('#preview-msg-content').html(getEditorHtml());
        publishController.open_html_dialog('preview-msg');
    });

    $('#save-as-template').click(function () {
        clean_135helper();
        saveTemplate(this.form);
    });

    $('#html-to-image').click(function () {
        clean_135helper();
        this.form.action = '/downloads/htmlToImage';
        this.form.submit();
    });
    var client = new ZeroClipboard($('#copy-editor-html'));
    client.on('ready', function (event) {
        client.on('copy', function (event) {

            clean_135helper();
            event.clipboardData.setData('text/html', getEditorHtml());
            event.clipboardData.setData('text/plain', getEditorHtml());
            showSuccessMessage("已成功复制到剪切板");
        });
    });

    if ($.cookie('bgimg')) {
        //$('#bg-img').attr('src',$.cookie('bgimg'));
        $('body').css('background-image', 'url(' + $.cookie('bgimg') + ')');
    } else {
        var bi = parseInt(Math.random() * 5);
        $('#bg-choose div').eq(bi).trigger('click');
    }

    $('#bg-choose .chooser').click(function () {
        //$('#bg-img').attr('src',$(this).data('url'));
        $('body').css('background-image', 'url(' + $(this).data('url') + ')');
        $.cookie('bgimg', $(this).data('url'));
    });

    $('#bg-choose .default').click(function () {
        $('body').css('background-image', 'initial');
        //$('#bg-img').removeAttr('src',"");//
        $.cookie('bgimg', null);
    });

    $('#btn-favor-color').click(function () {

        if (!sso.check_userlogin()) {
            return false;
        }
        var colors = $('#custom-color-text').css('backgroundColor'); //.val();
        var color_array = [];
        color_array[0] = colors;
        var hasfavor = false;

        $('#favor-colors li').each(function () {
            if ($.inArray($(this).css('backgroundColor'), color_array) == -1) {
                colors += ';' + $(this).css('backgroundColor');
            } else {
                hasfavor = true;
            }

        });
        if (hasfavor == false) {
            setFavorColor(colors, function () {
                $('#favor-colors').prepend('<li class="color-swatch" style="background-color:' + $('#custom-color-text').val() + ' ;"><i title="删除" class="fa fa-remove"></i></li>');
            });
        } else {
            alert('此颜色已收藏');
        }

    });

    $(document).on('click', '#favor-colors .fa-remove', function (e) {

        e.preventDefault();
        e.stopPropagation();

        $(this).parent().remove();
        var colors = '';
        $('#favor-colors li').each(function () {
            colors += $(this).css('backgroundColor') + ';';
        });
        setFavorColor(colors);
        return false;
    });
    // client.on( 'error', function(event) { 	ZeroClipboard.destroy(); } );
    /*var bgcolors = ['#FAF9DE','#FFF2E2','#FDE6E0','#E3EDCD','#DCE2F1','#E9EBFE','#EAEAEF','#c9b3a5'];
                  var bi = 0;
                  setInterval(function(){
                  	//$('body').css('background',bgcolors[bi]);

                  	$('#bg-choose div').eq(bi).trigger('click');
                  	//bi++;bi = bi%bgcolors.length;
                  	bi++;bi = bi%5;
                  },600000);*/

});