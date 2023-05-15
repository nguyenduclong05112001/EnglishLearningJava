<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idtest"])) {
    $idtest = $_POST["idtest"];
    $DB = new Apps_Models_TheTestOfUsers();
    $data = $DB->buildparam([
        "where" => "id = ?",
        "values" => [$idtest],
    ])->delete();

    if ($data) {
        echo "success";
    } else {
        echo "errorr";
    }
} else {
    echo "errorr";
}
