
<body>
    <div class="main-show">
        <div class="container-header">
            <form action="#" method="GET">
                <div class="header-btnadd">
                    <input type="button" name="btnadd" class="btn-handle" value="Add Account" id="btnadd" onclick="Showinsert()">
                </div>
                <div class="header-txtsearch">
                    <input type="search" name="txtsearch" placeholder="serach here" id="txtsearch">
                </div>
                <div class="header-btnsearch">
                    <input type="submit" name="btnsearch" value="Search" id="btnsearch" class="btn-handle">
                </div>
            </form>
        </div>

        <div id="container-insert" class="container-inserts">
            <?php
include_once './Insert.php';
?>
        </div>

        <div id="container-table" class="container-tables active">
            <table>
                <thead>
                <td class="col1">NO</td>
                <td class="col2">Username</td>
                <td class="col3">Password</td>
                <td class="col4">Type</td>
                <td class="col5">Handle</td>
                </thead>
                <tbody>
                    <?php
$item_per_page = 5;
$curren_page = !empty($_GET["page"]) ? $_GET["page"] : 1;
$totalitem = 0;
$totalpage;
$offset = ($curren_page - 1) * $item_per_page;

$account = new Apps_Model_Account();

$result = null;

$search = $page->getGet("txtsearch");

if ($search) {
    $result = $account->buildparam([
        "where" => "username like '%" . $page->getGet("txtsearch") . "%'",
        "other" => "LIMIT " . $item_per_page . " OFFSET " . $offset,
    ])->select();
    $totalresult = $account->buildparam([
        "where" => "username like '%" . $page->getGet("txtsearch") . "%'",
    ])->select();
    foreach ($totalresult as $a) {
        $totalitem++;
    }
    $totalpage = ceil($totalitem / $item_per_page);
} else {
    $result = $account->buildparam([
        "other" => "LIMIT " . $item_per_page . " OFFSET " . $offset,
    ])->select();
    $totalresult = $account->buildparam([])->select();
    foreach ($totalresult as $a) {
        $totalitem++;
    }
    $totalpage = ceil($totalitem / $item_per_page);
}
$i = 1;
foreach ($result as $value) {
    ?>
                    <form action="#" method="post" enctype="multipart/form-data">
                        <tr class="row-data" id="<?php echo 'row' . $i ?>">
                            <td class="col1"><?php echo $i ?></td>
                            <td class="col2">
                                <p id="col-2" class="data"><?php echo $value["username"] ?></p>
                                <input type="text" name="usernameaccount" readonly="readonly" value="<?php echo $value["username"] ?>" id="col-2-in" class="input">
                            </td>
                            <td class="col3">
                                <p id="col-3" class="data"><?php echo $value["password"] ?></p>
                                <input type="text" name="passwordaccount" value="<?php echo $value["password"] ?>" id="col-3-in" class="input">
                            </td>
                            <td class="col4">
                                <p id="col-4" class="data"><?php echo $value["type"] ?></p>
                                <input type="number" readonly="readonly" min="1" max="3" name="typeaccount" value="<?php echo $value["type"] ?>" id="col-4-in" class="input">
                            </td>
                            <td id="td-handle" class="col6">
                                <p id="col-5" class="data">
                                    <input type="button" id="btn-edit" value="Edit" name="btnedit" class="btn-handle" onclick="AddClass(<?php echo $i ?>);">
                                    <input type="button" id="btn-delete" value="Delete" name="btndelete" class="btn-handle" onclick="Deletedata('<?php echo $value["username"] ?>')">
                                </p>
                                <p id="col-6-in" class="input">
                                    <input type="submit" id="btn-save" value="Save" name="btnsave" class="btn-handle" onclick="Editdata();">
                                    <input type="button" id="btn-cancel" value="Cancel" name="btncancel" class="btn-handle" onclick="DropClass(<?php echo $i ?>);">
                                </p>
                            </td>
                        </tr>
                    </form>
                    <?php
$i++;
}
?>
                <tr id="tr_pagination">
                    <td colspan="6" id="td_pagination"><?php include_once '../HandleAdmin/PageNumber.php';?></td>
                </tr>
                </tbody>
            </table>

            <?php
if ($page->getPOST("btnsave")) {

    try {
        $account->buildparam(
            [
                "field" => "password=?,type=?",
                "where" => "username=?",
                "values" => [
                    md5($page->getPOST("passwordaccount")),
                    $page->getPOST("typeaccount"),
                    $page->getPOST("usernameaccount"),
                ],
            ]
        )->update();
        echo '<script language="javascript">';
        echo "location.replace('http://localhost/StudentManage/Admin/ManageAccount/Show.php');";
        echo '</script>';
    } catch (Exception $ex) {
        echo '<script language="javascript">';
        echo "alert('Update Failed')";
        echo '</script>';
    }
}
?>
        </div>

        <script language="javascript" src="http://code.jquery.com/jquery-2.0.0.min.js"></script>
        <script type="text/javascript">
                                        function AddClass(id,radio) {
                                            document.getElementById("row" + id).classList.add("on-edit");
                                        }
                                        function DropClass(id) {
                                            document.getElementById("row" + id).classList.remove("on-edit");
                                        }
                                        function Deletedata(user) {
                                            option = confirm("Do you want delete this Account?");
                                            if (!option) {
                                                return;
                                            }
                                            console.log(user);
                                            $.post('./Delete.php', {
                                                'user': user
                                            }, function (data) {
                                                alert(data);
                                                location.reload();
                                            });
                                        }
                                        function Showinsert() {
                                            document.getElementById("container-table").classList.remove("active");
                                            document.getElementById("container-insert").classList.add("active");
                                        }
        </script>
    </div>
</body>
