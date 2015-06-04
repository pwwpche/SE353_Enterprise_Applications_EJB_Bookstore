<%@ page import="Util.CookieManager" %>
<%@ page import="Util.MySessionListener" %>
<%--
  Created by IntelliJ IDEA.
  User: pwwpche
  Date: 2014/5/2
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Bootstrap core CSS -->
    <link href="dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="dist/css/navbar.css" rel="stylesheet">
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="easyui/demo/demo.css">
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>


    <title>User Interface</title>

    <script>
        //Shopping Cart
        var cartData = {"total": 0, "rows": []};
        var totalCost = 0;

        $(document).ready(function () {
            $('#cartcontent').datagrid({
                singleSelect: true,
                url: 'addCart?actionType=getAll',
                columns: [[
                    {field: 'bookname', title: 'Name', width: 100},
                    {
                        field: 'quantity', title: 'quantity', width: 30, sortable: true,
                        sorter: function (a, b) {
                            return (a > b ? 1 : -1);
                        }
                    },
                    {
                        field: 'price', title: 'Price', width: 40, sortable: true,
                        sorter: function (a, b) {
                            return (a > b ? 1 : -1);
                        }
                    },
                    {
                        field: 'opt', title: 'Operation', width: 0, align: 'center',
                        formatter: function (value, row, index) {
                            cartData = $("#cartcontent").datagrid("getData");
                            totalCost = 0;
                            for (var i = 0; i < cartData.total; i++) {
                                totalCost += (cartData.rows[i].price) * (cartData.rows[i].quantity);
                            }
                            $('div.cart .total').html('Total: ' + totalCost);
                            console.log(cartData);
                            return '<button class="removeCart" onclick="removeCartSelected(' + index + ')">Remove</button>';
                        }
                    }
                ]]
            });

            $('#clientCart').datagrid({
                title: 'Books overview',
                iconCls: 'icon-save',
                width: 700,
                height: 350,
                nowrap: true,
                autoRowHeight: true,
                striped: true,
                collapsible: true,
                url: 'userBook',
                sortOrder: 'desc',
                remoteSort: false,
                idField: 'bid',
                columns: [[
                    {field: 'bid', title: 'bid', width: 0},
                    {field: 'bookname', title: 'Name', width: 200},
                    {
                        field: 'catagory', title: 'Catagory', width: 100, sortable: true,
                        sorter: function (a, b) {
                            return (a > b ? 1 : -1);
                        }
                    },
                    {
                        field: 'price', title: 'Price', width: 100, sortable: true,
                        sorter: function (a, b) {
                            return (a > b ? 1 : -1);
                        }
                    },
                    {
                        field: 'Detail', title: 'Check Detail', width: 100, sortable: true,
                        formatter: function (value, row, index) {
                            console.log("add to cart index=", index);
                            return '<button class="showDetail" onclick="showDetail(' + index + ')">Show Detail</button>';
                        }

                    },
                    {
                        field: 'opt', title: 'Operation', width: 0, align: 'center',
                        formatter: function (value, row, index) {
                            console.log("add to cart index=", index);
                            return '<button class="addCart" onclick="getCartSelected(' + index + ')">Add to cart</button>';
                        }
                    }
                ]],
                pagination: true,
                rownumbers: true
            });
        });

        function success(data) {
            console.log(data);
        }

        function getCartSelected(index) {
            console.log("select+" + index);
            $("#clientCart").datagrid("selectRow", index);
            var selected = $('#clientCart').datagrid('getSelected');
            console.log("selected = " + selected);
            if (selected) {
                addProduct(index, selected.bid, selected.bookname, parseFloat(selected.price));
            }
        }

        function showDetail(index) {
            $("#clientCart").datagrid("selectRow", index);
            var selected = $('#clientCart').datagrid('getSelected');
            console.log("selected = " + selected);
            if (selected) {
                $.ajax({
                    url: "userBook",
                    type: "post",
                    dataType: "json",
                    data: {
                        data : JSON.stringify({
                            doType: "BookDetail",
                            bid: selected.bid
                        })
                    },
                    success: function (data) {
                        var dataJson = data;
                        alert("BookDetail\n"
                                + "Bookname : " + dataJson.bookname + "\n"
                                + "Catagory : " + dataJson.catagory + "\n"
                                + "price : " + dataJson.price + "\n"
                        );
                        console.log(data);
                    },
                    error: function (data) {
                        alert("upload failed");
                        console.log(data);
                    }
                });
            }
        }


        function removeCartSelected(index) {
            $("#cartcontent").datagrid("selectRow", index);
            var selected = $('#cartcontent').datagrid('getSelected');
            if (selected) {
                removeProduct(index, selected.bid)
            }
        }

        function addProduct(index, bid, name, price) {
            upload("add", bid);
            console.log("adding");
            console.log($("#cartcontent").datagrid("getData"));
            cartData = $("#cartcontent").datagrid("getData");
            if (isNaN(cartData.total)) {
                cartData = {"total": 0, "rows": []};
            }
            function add() {
                console.log("in add");
                for (var i = 0; i < cartData.total; i++) {
                    var row = cartData.rows[i];
                    if (row.bid == bid) {
                        row.quantity += 1;
                        return;
                    }
                }
                console.log("create new");
                cartData.total += 1;
                cartData.rows.push({
                    index_id: index,
                    bid: bid,
                    bookname: name,
                    quantity: 1,
                    price: price
                });
            }

            add();
            totalCost += price;
            $('#cartcontent').datagrid('loadData', cartData);
            console.log("loaded");
            $('div.cart .total').html('Total: ' + totalCost);
            console.log("div update");
        }

        function removeProduct(index, bid) {
            upload("remove", bid);
            cartData = $('#cartcontent').datagrid('getData');
            console.log(cartData);

            function remove() {
                console.log(cartData);
                for (var i = 0; i < cartData.total; i++) {
                    var row = cartData.rows[i];
                    if (row.bid == bid) {
                        cartData.rows[i].quantity -= 1;
                        totalCost -= row.price;
                        if (row.quantity == 0) {
                            cartData.total -= 1;
                            cartData.rows.splice(i, 1);
                        }
                        return;
                    }
                }

            }
            remove();
            console.log("remove done");
            $('#cartcontent').datagrid('loadData', cartData);
            $('div.cart .total').html('Total: ' + totalCost);
            console.log("div update");
        }

        function upload(action, bid) {
            var myAction = arguments[0] ? arguments[0] : "";
            var params = {};
            if (myAction == "getAll") {
                params = {
                    actionType: "getAll"
                };
            } else if (myAction == "add") {
                params = {
                    actionType: "add",
                    bid: bid
                };
            } else if (myAction == "remove") {
                params = {
                    actionType: "remove",
                    bid: bid
                };
            } else {
                params = {
                    actionType: "saveOrder",
                    rowData: JSON.stringify($('#cartcontent').datagrid('getData'))
                };
            }

            $.ajax({
                url: "addCart",
                type: "post",
                dataType: "json",
                data: params,
                success: function (data) {
                    if (myAction == "saveOrder"){
                        alert("Order saved");
                        $("#cartcontent").datagrid("reload");
                        totalCost = 0;
                        $('div.cart .total').html('Total: ' + totalCost);
                    }

                },
                error: function (data) {
                    alert("upload failed");
                    console.log(data);
                }
            });
        }

    </script>
</head>


<body>

<div class="container">
    <div class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Book view</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="chatRoom.jsp">Chat Room</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="active"><a href="logout.jsp">Sign out</a></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
        <!--/.container-fluid -->
    </div>
    <div class="row">
        <div class="col-lg-8">
            <p></p>
            <table id="clientCart" singleSelect="true"></table>
        </div>
        <div class="col-lg-4">
            <div class="cart">
                <p>Shopping Cart</p>
                <table id="cartcontent" style="width:400px;height:auto;">
                    <thead>
                    <tr>
                        <th field="id" width=0></th>
                        <th field="index_id" width=0></th>
                        <th field="bookname" width=140>Book Name</th>
                        <th field="quantity" width=60 align="right">Quantity</th>
                        <th field="price" width=60 align="right">Price</th>
                        <th field="Operation" width=60 align="right">
                        </th>
                    </tr>
                    </thead>
                </table>
                <p class="total">Total: $0</p>
                <button onclick="upload('saveOrder')">Done</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
