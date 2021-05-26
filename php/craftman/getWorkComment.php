<?php

	require_once 'connect2.php';
	$work_id = $_POST['id'];
	
	$sSQL= 'SET CHARACTER SET utf8'; 
	 mysqli_query($con, $sSQL); 
	 

  
	$sql = "SELECT comment 
				from comments where service_work_id = '$work_id' and type ='1' ";
	$stmt = $con->prepare($sql);
	
	$stmt->execute();
	$stmt->bind_result($comment);
	$items = array();
	while($stmt->fetch())
	{
		$temp = array();
		$temp['comment'] = $comment;
		
	
		array_push($items, $temp);
	}
	$json = json_encode($items); 
	 print($json);
	

?>