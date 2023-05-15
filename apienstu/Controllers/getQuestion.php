<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idpart"])){
    $idpart = $_POST["idpart"];

    $DB = new Apps_Models_Question();
    $data = $DB->buildparam([
        "where" => "idpart = ?",
        "values" => [$idpart]
    ])->select();

    if($data){
        echo json_encode($data);
    }else {
        echo json_encode(null);
    }
}else {
    echo json_encode(null);
}