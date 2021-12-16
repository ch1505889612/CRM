layui.use(['table','layer','jquery'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 营销机会列表展示
     */
    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url :  + '/sale_chance/list', // 访问数据的地址
        cellMinWidth : 95,
        page : true, // 开启分页
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称', align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人', align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'assignMan', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d)
                {
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
        {fixed:"right",title: '操作', templet:'#saleChanceListBar',align:"center", minWidth:150}]],
        //处理删除谋一项最后一条数据的bug
        done:function (res,curr,count) {
            //如果是异步请求数据方式,res即为接口的返回信息
            // 如果是直接赋值的方式,res即为:{data:[],count:99},data为当前页数据、count为数据总长度
            console.log(res);
            //得到当前页码
            console.log(curr);
            //得到数据总量
            console.log(count);
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
            where: { //设定异步数据接口的额外参数，任意设
                customerName:$("input[name=customerName]").val(),
                createMan:$("input[name=createMan]").val(),
                state:$("#state").val()
            }
            ,page: {
                curr: 1 //重新从第 1 页开始
            }
        })
    })

    /**
     * 绑定头部工具栏
     */
    table.on('toolbar(saleChances)',function(obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        console.log(checkStatus)
        console.log(checkStatus.data)
        switch (obj.event) {
            case 'add':
                // 点击添加按钮，打开添加营销机会的对话框
                openAddOrUpdateSaleChanceDialog()
                break;
            case 'del':
                deleteSaleChange(checkStatus.data);
                break;
        }
    })


    function openAddOrUpdateSaleChanceDialog(id) {
        var title = "<h2>营销机会管理添加</h2>";
        var url =  + "/sale_chance/addOrUpdateSaleChancePage";
        if (id!=null){
             title = "<h2>营销机会管理修改</h2>";
             url =  + "/sale_chance/addOrUpdateSaleChancePage?id="+id;
        }
        layui.layer.open({
            title:title,
            type:2, //iframe
            content: url,
            area:["500px","620px"],
            maxmin:true
        });
    }
    /**
     * 绑定行内工具栏
     */
    table.on('tool(saleChances)', function(obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        console.log(data.id)
        switch (layEvent) {
            case 'edit':
                openAddOrUpdateSaleChanceDialog(data.id);
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
                        url:  + "/sale_chance/delete",
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

    function deleteSaleChange(data){
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
                url:  + "/sale_chance/delete",
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
     * 格式化分配状态
     * 0 - 未分配
     * 1 - 已分配
     * 其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }
    /**
     * 格式化开发状态
     * 0 - 未开发
     * 1 - 开发中
     * 2 - 开发成功
     * 3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value){
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }
});
