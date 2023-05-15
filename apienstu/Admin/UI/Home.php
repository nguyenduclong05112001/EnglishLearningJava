<?php 
    session_start();
    include '../../Apps/Config.php';
    $data = null;
    if (isset($_SESSION["username"])) {
        $username = $_SESSION["username"];
        
        $DB = new Apps_Models_User();
            $data = $DB->buildparam([
                "where" => "username = ? and role = ?",
                "values" => [$username, 1],
            ])->selectone();

            if (!$data) {
                header("Location: /apienstu/admin/login.php");
            }
    }else{
        header("Location: /apienstu/admin/login.php");
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/home.css">
    <title>Home</title>
</head>

<body>
<!-- Phần header -->
    <div class="header-container">
      <div class="header-logo">
          <a href="./HandleAdmin/UpdateInfo.php">
          <?php
            if($data["avatar"] !== null){
                ?>
              <img src="<?php echo "../../Media/".$data["avatar"]?>" alt="No Image">
                <?php
            } else {
                ?>
            <img src="../../Media/default/avatar.png" alt="No Image">
                <?php
            }
          ?>
        </a>
        <a href="./HandleAdmin/UpdateInfo.php">
        <p><?php echo $data["name"] ?></p>
        </a>
      </div>
      <div class="header-handle">
        <div class="handle-edit">
          <a href="./HandleAdmin/UpdateInfo.php">
              <img src="../Media/image/Edit.png" alt="">
          </a>
        </div>
        <div class="handle-logout">
            <a href="../Login/Logout.php">
              <img src="../Media/image/logout.png" alt="">
          </a>
        </div>
      </div>
    </div>


    <!--
        Phần Menu
    -->
    <!--Created by- https://bootsnipp.com/ishwarkatwe-->
    <div class="content-body">
    <div class="sidebar-container">
        <ul class="sidebar-navigation">
        <li class="header">Thống kê</li>
          <li>
            <a href="#" class="menu-item active" onclick="changeMenu(event,1);">
              <i class="fa fa-home" aria-hidden="true"></i> Thống kê
            </a>
          </li>

          <li class="header">Học tập</li>
          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,2);">
              <i class="fa fa-home" aria-hidden="true"></i> Cấp độ
            </a>
          </li>

          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,3);">
              <i class="fa fa-home" aria-hidden="true"></i> Chủ đề
            </a>
          </li>

          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,4);">
              <i class="fa fa-home" aria-hidden="true"></i> Nội dung học tập
            </a>
          </li>
          
          <li class="header">Truyện ngắn</li>
          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,5);">
              <i class="fa fa-home" aria-hidden="true"></i> Truyện
            </a>
          </li>

          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,6);">
              <i class="fa fa-home" aria-hidden="true"></i> Câu hỏi
            </a>
          </li>

          <li class="header">Bài kiểm tra</li>
          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,7);">
              <i class="fa fa-home" aria-hidden="true"></i> Bài kiểm tra
            </a>
          </li>

          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,8);">
              <i class="fa fa-home" aria-hidden="true"></i> Nội dung
            </a>
          </li>

          <li class="header">Tài khoản</li>
          <li>
            <a href="#" class="menu-item" onclick="changeMenu(event,9);">
              <i class="fa fa-home" aria-hidden="true"></i> Tài khoản
            </a>
          </li>
        </ul>
      </div>
    
    <!-- Phaanf Content -->
      <div class="content">
        <div id="content-1" class="content-item active">
            <iframe class="item-contents" src="../Statistical/content.php" scrolling="no" frameBorder="0"></iframe>
        </div>

        <div id="content-2" class="content-item">
            <iframe class="item-contents" src="../learnmanage/level.php" scrolling="no" frameBorder="0"></iframe>
        </div>
      </div>
    </div>
    <script src="../js/menu.js"></script>
</body>
</html>x`