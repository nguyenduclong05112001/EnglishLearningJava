<?php
include '../Apps/Config.php';
$general = new Apps_Libs_General();

if (isset($_POST["username"]) && 
        isset($_POST["name"]) && 
        isset($_FILES["avatar"])){
            $name = $_POST["name"];
            $ran = rand(1,99999);
            $user = $_POST["username"];
            $userAvatar = $_POST["username"] . $ran;
            $nameimage = $_FILES["avatar"]["name"];
            $extension = pathinfo($nameimage, PATHINFO_EXTENSION);
            $avatar = "../Media/" . $userAvatar . "." . $extension;
        
            $DBS = new Apps_Models_User();
            $avatarold = $DBS->buildparam([
                "select" => "avatar",
                "where" => "username = ?",
                "values" => [$user]
            ])->selectone();
        
            if (file_exists("../Media/" . $avatarold["avatar"])) {
                unlink("../Media/" . $avatarold["avatar"]);
            }
        
            move_uploaded_file($_FILES["avatar"]["tmp_name"], $avatar);
        
            $avatarsavedata = $userAvatar . "." . $extension;

            $DB = new Apps_Models_User();
            $data = $DB->buildparam(
                [
                    "field" => "avatar = ?,name = ?",
                    "where" => "username = ?",
                    "values" => [
                        $avatarsavedata,
                        $name,
                        $user,
                    ],
                ]
            )->update();

            if ($data) {
                echo "Success";
            } else {
                echo "Errorr";
            }

}else if(isset($_POST["username"]) && 
            isset($_POST["name"])){

                $name = $_POST["name"];
                $user = $_POST["username"];

                $DB = new Apps_Models_User();
                $data = $DB->buildparam(
                    [
                        "field" => "name = ?",
                        "where" => "username = ?",
                        "values" => [
                            $name,
                            $user,
                        ],
                    ]
                )->update();
    
                if ($data) {
                    echo "Success";
                } else {
                    echo "Errorr";
                }
}else {
    echo "Not POST";
}