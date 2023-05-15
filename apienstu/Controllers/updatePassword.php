<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["username"]) &&
    isset($_POST["password"])) {
        $password = $_POST["password"];
        $user = $_POST["username"];

        $DB = new Apps_Models_User();
        $data = $DB->buildparam(
            [
                "field" => "password = ?",
                "where" => "username = ?",
                "values" => [
                    $general->encodePass($password),
                    $user,
                ],
            ]
        )->update();

        if ($data) {
            echo "Success";
        } else {
            echo "Errorr";
        }
}else {
    echo "Not POST";
}
