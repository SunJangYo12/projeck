<?php

$db_host = "localhost";
$db_user = "root";
$db_pass = "#pesawat";
$db_name = "hacking";

try {
	$db =new PDO("mysql:host=$db_host;dbname=$db_name",$db_user,$db_pass);
}
catch (PDOException $e){
	die ("error : ".$e->getMessage());
}

?>
