<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["searchtext"])) {

    $search = $_POST["searchtext"];

    $DB = new Apps_Models_TheTestOfUsers();
    $data = $DB->buildparam([
        "where" => "id = ?",
        "values" => [$search],
    ])->select();

    if($data){
        echo json_encode($data);
    }else {
        $DB = new Apps_Models_TheTestOfUsers();
        $data = $DB->buildparam([
        "where" => "name LIKE '%".$search."%'",
    ])->select();
        
        if($data){
            echo json_encode($data);
        }else {
            echo json_encode(null);
        }
    }

}else {
    echo json_encode(null);
}