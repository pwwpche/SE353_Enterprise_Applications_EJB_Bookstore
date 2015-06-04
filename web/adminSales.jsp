<%--
  Created by IntelliJ IDEA.
  User: pwwpche
  Date: 2014/5/4
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>

    <!-- Bootstrap core CSS -->
    <link href="dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="dist/css/navbar.css" rel="stylesheet">

    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="easyui/demo/demo.css">
    <script type="text/javascript" src="easyui/jquery.easyui.min.js" ></script>
    <script>
        var emptyStr = {"total":0,"rows":[]};
        var itemsStr = {"total":10,"rows":[{"sid":1,"uid":3,"day":2,"month":2,"year":2002},{"sid":2,"uid":3,"day":2,"month":2,"year":2002},{"sid":5,"uid":3,"day":2,"month":2,"year":2002},{"sid":11,"uid":3,"day":2,"month":2,"year":2002},{"sid":14,"uid":3,"day":2,"month":2,"year":2002},{"sid":17,"uid":3,"day":2,"month":2,"year":2002},{"sid":19,"uid":11,"day":2,"month":2,"year":2001},{"sid":20,"uid":9,"day":2,"month":2,"year":2003},{"sid":21,"uid":12,"day":2,"month":2,"year":2005},{"sid":22,"uid":15,"day":2,"month":2,"year":2002}]};
        var statisStr = {"total":2,"rows":[{"sDay":2,"sumprice":96.0},{"sDay":15,"sumprice":147.0}]};
        function createQuery(){

            $("#infoGrid").datagrid("loadData",emptyStr);
            var selectType = $('#queryCondition').combo('getValue');
            console.log("selectType=" + selectType);
            console.log("QueryString=" + $("#QueryString").val());
            $.ajax({
                type: "post",
                dataType: "json",
                url: "adminSales",
                data:{
                    type : selectType,
                    dataString : $("#QueryString").val(),
                    item: "items"
                },
                success:function(data){

                        $("#infoGrid").datagrid("loadData", data);
                    console.log("InfoGrid loaded");
                },
                error: function(data){
                    $("#infoGrid").datagrid("loadData", itemsStr);
                }
            });
            function typeConvert(type){
                console.log(type);
                var result = "no";
                if(type == 1)
                    result =  "uid";
                if(type == 2)
                    result =  "sDay";
                if(type == 3)
                    result =  "sMonth";
                if(type == 4)
                    result =  "sYear";
                console.log(result);
                return result;
            }
            $.ajax({
                type: "post",
                dataType: "json",
                url: "adminSales",
                data:{
                    type : selectType,
                    dataString : $("#QueryString").val(),
                    item: "statistics"
                },
                success:function(data){
                    $("#statisGrid").datagrid({
                        title:"Sale Statistics",
                        width:700,
                        height:200,
                        nowrap: true,
                        autoRowHeight: true,
                        striped: true,
                        collapsible:true,
                        sortOrder: 'desc',
                        remoteSort: false,
                        idField:'bid',
                        columns: [[
                            {
                                field: typeConvert(selectType),
                                width: 100,
                                title: typeConvert(selectType)
                            },
                            {
                                field: "sumprice",
                                width: 100,
                                title: "Total income"
                            }

                        ]]
                    });
                    $("#statisGrid").datagrid("loadData", data);
                    console.log("StatisGrid loaded");
                },
                error: function(data){
                    $("#statisGrid").datagrid({
                        title:"Sale Statistics",
                        width:700,
                        height:200,
                        nowrap: true,
                        autoRowHeight: true,
                        striped: true,
                        collapsible:true,
                        sortOrder: 'desc',
                        remoteSort: false,
                        idField:'bid',
                        columns: [[
                            {
                                field: typeConvert(selectType),
                                width: 100,
                                title: typeConvert(selectType)
                            },
                            {
                                field: "sumprice",
                                width: 100,
                                title: "Total income"
                            }

                        ]]
                    });
                    $("#statisGrid").datagrid("loadData", statisStr);
                }
            });

        }

        $(document).ready(function(){

            $("#queryCondition").combo({
                required:true,
                editable:false,
                width: 100
            });
            $('#conditionList').appendTo($('#queryCondition').combo('panel'));

            $('#conditionList input').click(function(){
                var v = $(this).val();
                var s = $(this).next('span').text();
                $('#queryCondition').combo('setValue', v).combo('setText', s).combo('hidePanel');
            });
        });
    </script>
</head>

<body>
<div class="container">
<div class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">

            <a class="navbar-brand" href="#">Admin</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li ><a href="adminUser.jsp">Manage User</a></li>
                <li><a href="adminBooks.jsp">Manage Books</a></li>
                <li class="active"><a href="adminSales.jsp">Manage Sales</a></li>
                <li><a href="chatRoom.jsp">Chat Room</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="logout.jsp">Sign out</a></li>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</div>

<div class="row">
    <div class="col-lg-4">
        <div>
            <select id="queryCondition" width="100">Input Query Condition</select>
        </div>
        <div id="conditionList">
            <div style="color:#99BBE8;background:#fafafa;padding:5px;">Choose Query Type</div>
            <input type="radio" name="lang" value="01"><span>user</span><br/>
            <input type="radio" name="lang" value="02"><span>day</span><br/>
            <input type="radio" name="lang" value="03"><span>month</span><br/>
            <input type="radio" name="lang" value="04"><span>year</span><br/>
            <input type="radio" name="lang" value="05"><span>catagory</span>
        </div>

        Input Query String<br><input type="text" name="" id="QueryString"/>
        <button onclick="createQuery()">Go</button>
    </div>

    <div class="col-lg-8" >
        <table id="infoGrid" title="Sales Information" class="easyui-datagrid" style="width:700px;height:250px"
               toolbar="#toolbar" pagination="true"
               rownumbers="true" fitColumns="true" singleSelect="true">
            <thead>
            <tr>
                <th field="sid" width="50">Sale ID</th>
                <th field="year" width="50">year</th>
                <th field="month" width="50">month</th>
                <th field="day" width="50">day</th>
            </tr>
            </thead>
        </table>

        <table id="statisGrid" title="Sale Statistics" class="easyui-datagrid" style="width:700px;height:250px"
               toolbar="#toolbar" pagination="true"
               rownumbers="true" fitColumns="true" singleSelect="true">

        </table>
        <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
             closed="true" buttons="#dlg-buttons">
            <div class="ftitle">Book Information</div>

        </div>
        <div id="dlg-buttons">
            <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="createQuery()">Create Query</a>
        </div>
    </div>
</div>
</div>

</body>
</html>
