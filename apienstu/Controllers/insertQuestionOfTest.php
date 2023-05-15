<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["idtest"]) &&
    isset($_POST["content"]) &&
    isset($_POST["answer1"]) &&
    isset($_POST["answer2"]) &&
    isset($_POST["answer3"]) &&
    isset($_POST["answer4"]) &&
    isset($_POST["goodanswer"])) {

    $idtest = $_POST["idtest"];
    $content = $_POST["content"];
    $answer1 = $_POST["answer1"];
    $answer2 = $_POST["answer2"];
    $answer3 = $_POST["answer3"];
    $answer4 = $_POST["answer4"];
    $goodanswer = $_POST["goodanswer"];

    $DB = new Apps_Models_Questionoftest();
    $data = $DB->buildparam([
        "field" => "(idtest,content,answer1,answer2,answer3,answer4,goodanswer) VALUES (?,?,?,?,?,?,?)",
        "values" => [$idtest, $content, $answer1, $answer2, $answer3, $answer4, $goodanswer],
    ])->insert();

    if ($data) {
        echo "success";
    } else {
        echo "errorr";
    }

} else {
    echo "errorr";
}
