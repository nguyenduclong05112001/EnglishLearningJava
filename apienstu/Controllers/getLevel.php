<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

    $DB = new Apps_Models_Level();
    $data = $DB->buildparam([])->select();

    if($data){
        echo json_encode($data);
    }else {
        echo null;
    }
