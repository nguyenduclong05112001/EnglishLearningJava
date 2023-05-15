<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["id"]) &&
    isset($_POST["name"]) &&
    isset($_POST["user"]) &&
    isset($_POST["date"]) &&
    isset($_FILES["avatar"])) {

    $id = $_POST["id"];
    $name = $_POST["name"];
    $user = $_POST["user"];
    $date = $_POST["date"];
    $ran = rand(1, 99999);
    $namechange = $_POST["id"] . $ran;
    $nameimage = $_FILES["avatar"]["name"];
    $extension = pathinfo($nameimage, PATHINFO_EXTENSION);
    $avatar = "../Media/test/" . $namechange . "." . $extension;
    $avatarsavedata = $namechange . "." . $extension;
    move_uploaded_file($_FILES["avatar"]["tmp_name"], $avatar);
    
    $DB = new Apps_Models_TheTestOfUsers();
    $data = $DB->buildparam([
        "field" => "(id,name,iduser,datecreate,avatar) VALUES (?,?,?,?,?)",
        "values" => [$id, $name, $user, $date, 'test/'.$avatarsavedata],
    ])->insert();

    if ($data) {
        echo "success";
    } else {
        echo "errorr";
    }

} else if (isset($_POST["id"]) &&
    isset($_POST["name"]) &&
    isset($_POST["user"]) &&
    isset($_POST["date"])) {

    $id = $_POST["id"];
    $name = $_POST["name"];
    $user = $_POST["user"];
    $date = $_POST["date"];

    $DB = new Apps_Models_TheTestOfUsers();
    $data = $DB->buildparam([
        "field" => "(id,name,iduser,datecreate) VALUES (?,?,?,?)",
        "values" => [$id, $name, $user, $date],
    ])->insert();

    if ($data) {
        echo "success";
    } else {
        echo "errorr";
    }
} else {
    echo "errorr";
}