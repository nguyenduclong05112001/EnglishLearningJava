<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if(isset($_POST["username"])){
    $user = $_POST["username"];

    $DB = new Apps_Models_User();
    $data = $DB->buildparam(
        [
            "where" => "username = ?",
            "values" => [
                $user
            ]
        ]
    )->selectone();

    if ($data) {
        echo json_encode($data);
    } else {
        echo json_encode(null);
    }
}else {
    echo json_encode(null);
}