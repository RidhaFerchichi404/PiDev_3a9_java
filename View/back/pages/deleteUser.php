<?php
 include_once 'c:/xampp/htdocs/projetuser/Controller/userC.php';
 $userC = new userC();
 if(isset($_GET['id'])){
     $userC->deleteUser($_GET['id']);
 
    header('Location:Dashboard.php');
    }

 ?>