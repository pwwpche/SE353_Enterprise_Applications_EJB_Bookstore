<%@ page import="Servlets.AdminBook" %>
<%--

  Created by IntelliJ IDEA.
  Book: pwwpche
  Date: 2014/5/3
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Book Administration</title>
    <!-- Bootstrap core CSS -->
    <link href="dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="dist/css/navbar.css" rel="stylesheet">
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="easyui/demo/demo.css">
    <style type="text/css">
        #fm{
            margin:0;
            padding:10px 30px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            color:#666;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:5px;
        }
        .fitem label{
            display:inline-block;
            width:80px;
        }
    </style>

    <title>Book Interface</title>

    <script type="text/javascript">

        var url = "adminBook";
        function newBook(){
            $('#dlg').dialog('open').dialog('setTitle','New Book');
            $("#inputBid").hide();
            $('#fm').form('clear');
            url = 'adminBook?doType=new';
        }
        function editBook(){
            $("#inputBid").hide();

            var row = $('#dg').datagrid('getSelected');
            if (row){
                $('#dlg').dialog('open').dialog('setTitle','Edit Book');
                $('#fm').form('load',row);
                url = 'adminBook?doType=edit';

            }
        }
        function saveBook(){
            console.log("saveBook");
            $('#fm').form('submit',{
                url: url,
                onSubmit: function(){
                    console.log("url="+url);

                    return $(this).form('validate');
                },
                success: function(result){
                    var result = eval('('+result+')');
                    console.log(result);
                    if (result.success){
                        $('#dlg').dialog('close');		// close the dialog
                        $('#dg').datagrid('reload');	// reload the book data
                    } else {
                        $.messager.show({
                            title: 'Error',
                            msg: result.msg
                        });
                    }
                }
            });
        }
        function removeBook(){
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','Are you sure you want to remove this book?',function(r){
                    if (r){
                        console.log(row);

                        $.post('adminBook?doType=remove&bid=' + row.bid,{id:row.id},function(result){
                            console.log(result);
                            if (result.success){
                                $('#dg').datagrid('reload');	// reload the book data
                            } else {
                                $.messager.show({	// show error message
                                    title: 'Error',
                                    msg: result.msg
                                });
                            }
                        },'json');
                    }
                });
            }
        }
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
                    <li><a href="adminUser.jsp">Manage User</a></li>
                    <li  class="active"><a href="adminBooks.jsp">Manage Books</a></li>
                    <li><a href="adminSales.jsp">Manage Sales</a></li>
                    <li><a href="chatRoom.jsp">Chat Room</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="logout.jsp">Sign out</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div><!--/.container-fluid -->
    </div>
    <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">


            <table id="dg" title="My Books" class="easyui-datagrid" style="width:700px;height:250px"
                   url="adminBook"
                   toolbar="#toolbar" pagination="true"
                   rownumbers="true" fitColumns="true" singleSelect="true">
                <thead>
                <tr>
                    <th field="bid" width="50">bid</th>
                    <th field="bookname" width="50">Bookname</th>
                    <th field="catagory" width="50">Catagory</th>
                    <th field="price" width="50">price</th>
                </tr>
                </thead>
            </table>
            <div id="toolbar">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newBook()">New Book</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editBook()">Edit Book</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeBook()">Remove Book</a>
            </div>

            <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"
                 closed="true" buttons="#dlg-buttons">
                <div class="ftitle">Book Information</div>
                <form id="fm" method="post" novalidate>

                    <div class="fitem" id="inputBid">
                        <label>Bid:</label>
                        <input name="bid" class="easyui-validatebox">
                    </div>

                    <div class="fitem" id="inputBook">
                        <label>Bookname:</label>
                        <input name="bookname" class="easyui-validatebox" required="true" >
                    </div>

                    <div class="fitem">
                        <label>Catagory:</label>
                        <input name="catagory" class="easyui-validatebox" validType="catagory" required="true">
                    </div>
                    <div class="fitem">
                        <label>Price:</label>
                        <input type="text" name="price" id=""/>
                    </div>
                </form>
            </div>
            <div id="dlg-buttons">
                <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveBook()">Save</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
            </div>

        </div>

    </div>

</div>


</body>
</html>