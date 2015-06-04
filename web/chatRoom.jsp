<%@ page import="Servlets.AdminUser" %>
<%--
<%@ page import="Servlets.adminUser" %>
  Created by IntelliJ IDEA.
  User: pwwpche
  Date: 2014/5/2
  Time: 20:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Bootstrap core CSS -->
    <link href="dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="dist/css/navbar.css" rel="stylesheet">

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Administration</title>
    <script type="text/javascript" src="easyui/jquery.min.js"></script>
    <script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="easyui/demo/demo.css">
    <style type="text/css">
        #fm {
            margin: 0;
            padding: 10px 30px;
        }

        .ftitle {
            font-size: 14px;
            font-weight: bold;
            color: #666;
            padding: 5px 0;
            margin-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        .fitem {
            margin-bottom: 5px;
        }

        .fitem label {
            display: inline-block;
            width: 80px;
        }

        .contentDiv {
            width: 100%;
            height: 400px;
            border-color: #dddddd;
            border-width: thin;
            border-style: solid;
            border-radius: 4px;
            display: block;
            padding: 5px;
            overflow: auto;
        }

        .username {
            font-weight: bold;
            color: #555599;
        }
    </style>

    <script type="text/javascript">
        $(document).ready(function () {
            var wsocket;
            var textarea = document.getElementById("textArea");

            function connect() {
                wsocket = new WebSocket("ws://localhost:8080/BookStore_war_exploded/chat");
                console.log("wsocket done");
                wsocket.onmessage = processMessage;
                activateUser();
            }

            function processMessage(event) {
                console.log("Processing");
                console.log(event.data);
                var line = "";
                try {
                    var msg = JSON.parse(event.data);
                    if (msg.type == "TextMessage") {
                        console.log("Text Msg : " + msg.message);
                        var d = document.createElement('div');
                        var suser = document.createElement('span');
                        var smessage = document.createElement('span');

                        $(suser).addClass("username").text(msg.name + " : ").appendTo($(d));
                        $(smessage).text(msg.message).appendTo($(d));
                        $(d).appendTo("#chatBox");
                        $("#chatBox").scrollTop($("#chatBox")[0].scrollHeight);

                    } else if (msg.type == "SystemMessage") {
                        console.log("System Msg : " + msg.name + " " + msg.action);
                        if (msg.action == "active") {
                            var d = document.createElement('div');
                            $(d).addClass("username user").text(msg.name).attr("data-user", msg.name).appendTo("#nicknamesBox");
                        } else if (msg.action == "inactive") {
                            $(".user[data-user=" + msg.name + "]").remove();
                        }
                    } else if (msg.type == "ListMessage") {
                        console.log("ListMessage");
                        console.log(msg);
                    } else {
                        console.log("error msg");
                    }
                } catch (e) {
                    console.log("error msg");
                    console.log(e.data);
                }


            }

            function activateUser() {
                var sysMsg = {};
                sysMsg.type = "SystemMessage";
                sysMsg.name = "<%=session.getAttribute("username") == null ? "John Doe" : session.getAttribute("username").toString()%>";
                sysMsg.action = "join"
                jsonStr = JSON.stringify(sysMsg);
                //wsocket.send(jsonStr);
                sendByWS(jsonStr);
            }

            function sendByWS(msg) {
                waitForSocketConnection(wsocket, function () {
                    console.log("message sent");
                    wsocket.send(msg);
                    console.log(msg);
                });
            }

            // Make the function wait until the connection is made...
            function waitForSocketConnection(socket, callback) {
                setTimeout(
                        function () {
                            if (socket.readyState === 1) {
                                console.log("Connection is made")
                                if (callback != null) {
                                    callback();
                                }
                                return;

                            } else {
                                console.log("wait for connection...")
                                waitForSocketConnection(socket, callback);
                            }

                        }, 5); // wait 5 milisecond for the connection...
            }


            $("#btnSend").click(function(){
                sendMsg();
            });
            function sendMsg() {
                var msgContent = document.getElementById("txtMessage").value;
                var textMsg = {
                    type: "TextMessage",
                    message: msgContent,
                    name: "<%=session.getAttribute("username") == null ? "John Doe" : session.getAttribute("username").toString()%>"
                };
                jsonStr = JSON.stringify(textMsg);
                sendByWS(jsonStr);
            }

            window.addEventListener("load", connect, false);
        });
    </script>

</head>
<body>

<div class="container">
    <div class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Admin Panel</a>
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
        <div class="col-xs-9">
            <div><h3>Chat</h3></div>
            <div class="contentDiv" id="chatBox"></div>
        </div>
        <div class="col-xs-3">
            <div><h3>Users</h3></div>
            <div class="contentDiv" id="nicknamesBox">
            </div>
        </div>
    </div>
    <div class="row" style="margin-top:10px;">
        <div class="col-xs-7"><input type="text" id="txtMessage" class="form-control"
                                     placeholder="Type your message here."/></div>
        <div class="col-xs-2">
            <button id="btnSend" class="btn btn-primary" style="width:100%;">Send</button>
        </div>
    </div>

</div>
</body>
</html>