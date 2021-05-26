<?php

	define('DB_HOST','localhost');
	define('DB_USER','id16791523_abdallah');
	define('DB_PASS','c8H[Xo+c5@FD@V+k');
	define('DB_NAME','id16791523_carftsman');
	

	$con = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
	
	if(mysqli_connect_errno()){
		
		die('Unable to connect to DATABASE '.mysqli_connect_errno());
	}
	


?>