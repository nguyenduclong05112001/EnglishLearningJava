<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["name"]) &&
    isset($_POST["username"]) &&
    isset($_POST["password"])) {

    $name = $_POST["name"];
    $user = $_POST["username"];
    $pass = $_POST["password"];

    $DB = new Apps_Models_User();
    $data = $DB->buildparam(
        [
            "field" => "(`username`,`name`,`password`) values (?,?,?)",
            "values" => [
                $user,
                $name,
                $general->encodePass($pass),
            ],
        ]
    )->insert();

    if ($data) {
        $data = $DB->buildparam(
            [
                "where" => "username = ?",
                "values" => [
                    $user
                ]
            ]
        )->selectone();

        echo json_encode($data);
    } else {
        echo json_encode(null);
    }

} else {
    echo json_encode(null);
}
