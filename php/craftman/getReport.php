<?php


	require_once 'connect2.php';
	
	$sSQL= 'SET CHARACTER SET utf8'; 
	 mysqli_query($con, $sSQL); 
	 


  
	$sql = "SELECT r.id, r.report, r.type, u.name
			from  report r , user u
		 where r.user_id= u.id";
	$stmt = $con->prepare($sql);
	
	$stmt->execute();
	$stmt->bind_result($id, $report, $type, $name);
	$items = array();
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id;
		$temp['report'] = $report;
		$temp['type'] = $type;
		$temp['name'] = $name;
	
	
		array_push($items, $temp);
	}
	$json = json_encode($items); 
	 print($json);
	

?>