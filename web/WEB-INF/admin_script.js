/**
 * Created by pwwpche on 2014/5/3.
 */

$('#dg').datagrid({
    url: "adminUser?doType=loadUsers",
    title: "User List",
    loadMsg: "Loading...",
    idField: 'uid',
    singleSelect: true,
    striped: true,
    rownumbers: true,
    columns: [[
        { title: 'UID', field: 'uid', hidden: true },
        { title: 'Username', field: 'username', width: 100 },
        { title: 'Password', field: 'password', width: 35},
        { title: 'RegDate', field: 'regDate', align: "center", width: 90 },
        { title: 'email', field: 'email', width: 120 }
    ]],
    rowStyler: function (index, row, css) {
        if (row.UserId != "") {
            return 'font-weight:bold;';
        }
    },
    onLoadSuccess: function (data) {
        if (data.rows.length > 0) {
            $('#staffGird').datagrid("selectRow", 0);
        }
    }
});