var count = 0;
var coverimg = "";
var jcrop_api;
var ntrueWidth, ntrueHeight;
var strimgurl = "";
var img_x, img_y, img_w, img_h;
var current_edit_msg_id;
var strcontent = "";

$(function() {
  $("#validateForm").validate();
  $("#dialog").dialog({
    autoOpen: false,
    modal: true,
    buttons: {
      "确认": function() {
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
});

function getContent() {
  if (jcrop_api) {
    console.log("jcrop destroy");
    jcrop_api.destroy();
  }

  if (strcontent.length > 0) {
    $("#dialog").dialog("open");
  } else {
    alert("请勿提交空内容！");
  }
}

//获取图片原始尺寸
function getNaturalWidth(imgsrc) {
  var image = new Image();
  image.src = imgsrc;
  var naturalWidth = image.width;
  return naturalWidth;
}

function getNaturalHeight(imgsrc) {
  var image = new Image();
  image.src = imgsrc;
  var naturalHeight = image.height;
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
  }, function() {
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
}

$(function() {
  strcontent = $("#content").html();
  $("#ContentIMG").on("click", "fieldset", function(e) {
    if (jcrop_api) {
      console.log("jcrop destroy");
      jcrop_api.destroy();
    }
    coverimg = $($(this).html()).attr("src");
    ntrueWidth = getNaturalWidth(coverimg);
    ntrueHeight = getNaturalHeight(coverimg);
    $("#imgcrop").removeAttr("src");
    //$("#imgcrop").removeAttr("style");
    strimgurl = coverimg;
    $("#imgcrop").attr("src", coverimg);
  });
});

function CoverIMGCrop() {
  $(function($) {
    initJcrop();
  });
}

function GetContent() {
  //先销毁jcrop
  if (jcrop_api) {
    jcrop_api.destroy();
  }
  //首先删除原来的元素
  $("#IMGHeader").empty();
  var ueContent = $("#content").html();
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
  $("#ContentIMG").append($("IMGHeader").html());
}
