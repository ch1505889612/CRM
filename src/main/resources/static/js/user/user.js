layui.use(['table','layer','jquery'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url : +'/user/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "userListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#userListBar',fixed:"right",align:"center"}
        ]],//处理删除谋一项最后一条数据的bug
        done:function (res,curr,count) {
            //如果是异步请求数据方式,res即为接口的返回信息
            // 如果是直接赋值的方式,res即为:{data:[],count:99},data为当前页数据、count为数据总长度
          //  console.log(res);
            //得到当前页码
           // console.log(curr);
            //得到数据总量
           // console.log(count);
            if (res.data.length==0&&curr!=1){
                deptTable.reload({
                    page:{
                        curr:(curr-1)
                    }
                })
            }
        }
    });

    /**
     * 条件查询
     */
    $(".search_btn").click(function (){
        tableIns.reload({
            where:{
                userName:$("input[name=userName]").val(),
                email:$("input[name=email]").val(),
                phone:$("input[name=phone]").val()
            },
            page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    })


    /**
     * 绑定头部工具栏
     */
    table.on('toolbar(users)',function(obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        console.log(checkStatus)
        console.log(checkStatus.data)

        switch (obj.event) {
            case 'add':
                // 点击添加按钮，打开添加营销机会的对话框
                openAddUpdate();
                break;
            case 'del':
                   deletes(checkStatus.data);
                break;
        }
    })



    function openAddUpdate(id) {
         var url=+"/user/addUpdateUser";
         var title="<h2>用户模块--增加</h2>"
         if (id!=null){
             url=+"/user/addUpdateUser?id="+id
             title="<h2>用户模块--修改</h2>"
         }
        layui.layer.open({
            title:title,
            type:2, //iframe
            content: url,
            area:["500px","620px"],
            maxmin:true
        });
    }
    function deletes(data){

        // 判断用户是否选择了要删除的记录
        if (data.length == 0) {
            layer.msg("请选择要删除的记录！");
            return;
        }
        // 询问用户是否确认删除
        layer.confirm("您确定要删除选中的记录吗？",{
            btn:["确认","取消"],
        },function (index) {

            // 关闭确认框
            layer.close(index);
            var ids=[];
            //向数组中添加数据
            for (let x in data) {
                ids.push(data[x].id)
            }
            $.ajax({
                type: "post",
                url:  + "/user/delete",
                data: {"ids":ids.toString()}, // 参数传递的是数组
                dataType: "json",
                success: function (result) {
                    if (result.code == 200) {
                        layer.msg(result.msg, {icon: 1});
                        // 加载表格
                        tableIns.reload();
                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                }
            });

        })
    }



    /**
     * 绑定行内工具栏
     */
    table.on('tool(users)', function(obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        console.log(data.id)
        switch (layEvent) {
            case 'edit':
                openAddUpdate(data.id);
                break;
            case 'del':
                // 询问用户是否确认删除
                layer.confirm("您确定要删除选中的记录吗？",{
                    btn:["确认","取消"],
                },function (index) {
                    // 关闭确认框
                    layer.close(index);
                    $.ajax({
                        type: "post",
                        url:  + "/user/delete",
                        data: {"ids": data.id}, // 参数传递的是数组
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 200) {
                                layer.msg(result.msg, {icon: 1});
                                // 加载表格
                                tableIns.reload();
                            } else {
                                layer.msg(result.msg, {icon: 2});
                            }
                        }
                    });
                });

                break;
        }
    })

})