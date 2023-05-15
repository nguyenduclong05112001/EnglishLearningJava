<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idstory"])){
    $idstory = $_POST["idstory"];

    $DB = new Apps_Models_AnswerOfStory();
    $data = $DB->buildparam([
        "where" => "idstory = ?",
        "values" => [$idstory]
    ])->select();

    if($data){
        echo json_encode($data);
    }else {
        echo json_encode(null);
    }
}else {
    echo json_encode(null);
}