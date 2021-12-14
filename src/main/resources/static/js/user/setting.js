layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 提交表单数据
     */
      form.on("submit(saveBtn)",function (data) {
          /**
           * 向后台发送数据
           */
          $.post(ctx+"/user/sayUpdate",data.field,function (data) {
               if (data.code==200){
                   layer.msg(data.msg,{icon:1},function () {
                       $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                       $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                       $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                       window.parent.location.href=ctx+"/index";
                   })
               }else {
                   layer.msg(data.msg,{icon: 2});
               }
          })
      })
});