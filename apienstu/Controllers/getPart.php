<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idlevel"])){
    $idlevel = $_POST["idlevel"];

    $DB = new Apps_Models_Part();
    $data = $DB->buildparam([
        "where" => "idlevel = ?",
        "values" => [$idlevel]
    ])->select();

    if($data){
        echo json_encode($data);
    }else {
        echo json_encode(null);
    }
}else {
    echo json_encode(null);
}

    