<?php

	require_once 'connect2.php';
	
	$sSQL= 'SET CHARACTER SET utf8'; 
	 mysqli_query($con, $sSQL); 
	 

  
	$sql = "SELECT u.id, u.name, u.email,u.state,u.phone, a.name
			from   user u, address a
			where u.Address_id =a.id and u.type = 1";
	$stmt = $con->prepare($sql);
	
	$stmt->execute();
	$stmt->bind_result($id, $name, $email, $state, $phone, $address);
	$items = array();
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id;
		$temp['name'] = $name;
		$temp['email'] = $email;
		$temp['state'] = $state;
		$temp['phone'] = $phone;
		$temp['address'] = $address;
	
	
		array_push($items, $temp);
	}
	$json = json_encode($items); 
	 print($json);
	

?>