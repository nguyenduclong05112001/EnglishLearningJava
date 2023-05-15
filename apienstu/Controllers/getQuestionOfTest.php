<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idtest"])){
    $idtest = $_POST["idtest"];

    $DB = new Apps_Models_Questionoftest();
    $data = $DB->buildparam([
        "where" => "idtest = ?",
        "values" => [$idtest]
    ])->select();

    if($data){
        echo json_encode($data);
    }else {
        echo json_encode(null);
    }
}else {
    echo json_encode(null);
}