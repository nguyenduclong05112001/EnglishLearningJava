<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["username"]) &&
    isset($_POST["pointofday"]) &&
    isset($_POST["chain"])) {

    $username = $_POST["username"];
    $pointofday = $_POST["pointofday"];
    $chain = $_POST["chain"];

    $DB = new Apps_Models_Achievementofuser();
    $data = $DB->buildparam(
        [
            "field" => "pointofday = ?,chain = ?",
            "where" => "username = ?",
            "values" => [
                $pointofday,
                $chain,
                $username,
            ],
        ]
    )->update();

    if ($data) {
        echo "Success";
    } else {
        echo "Errorr";
    }
} else {
    echo "Not POST";
}
