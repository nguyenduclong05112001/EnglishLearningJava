<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["username"]) &&
    isset($_POST["password"])) {

    $user = $_POST["username"];
    $pass = $_POST["password"];

    $DB = new Apps_Models_User();
    $data = $DB->buildparam(
        [
            "where" => "username = ? and password = ?",
            "values" => [
                $user,
                $general->encodePass($pass)
            ]
        ]
    )->selectone();

    if ($data) {
        echo json_encode($data);
    } else {
        echo json_encode(null);
    }

} else {
    echo json_encode(null);
}
?>