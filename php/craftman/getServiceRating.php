<?php

	require_once 'connect2.php';
	$work_id = $_POST['id'];
	$sSQL= 'SET CHARACTER SET utf8'; 
	 mysqli_query($con, $sSQL); 
	 

  
	$sql = "SELECT rate 
				from rating where service_work_id = '$work_id' and type ='0' ";
	$stmt = $con->prepare($sql);
	
	$stmt->execute();
	$stmt->bind_result($rate);
	$items = array();
	while($stmt->fetch())
	{
		$temp = array();
		$temp['rating'] = $rate;
		
	
		array_push($items, $temp);
	}
	$json = json_encode($items); 
	 print($json);
	

?>