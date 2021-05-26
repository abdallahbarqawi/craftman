<?php


	require_once 'connect2.php';
	$cat_id = $_POST['category'];
	$sSQL= 'SET CHARACTER SET utf8'; 
	 mysqli_query($con, $sSQL); 
	 

  
	$sql = "SELECT s.id,s.title, s.description, s.price, s.image,s.certificate, c.name, u.phone, a.name
			from  work s , category c, user u, address a
			where s.category_id = c.id and s.user_id = u.id and s.Address_id = a.id and s.category_id = '$cat_id'";
	$stmt = $con->prepare($sql);
	
	$stmt->execute();
	$stmt->bind_result($id, $title, $description, $price, $image, $certificate, $name, $phone, $address);
	$items = array();
	while($stmt->fetch())
	{
		$temp = array();
		$temp['id'] = $id;
		$temp['title'] = $title;
		$temp['description'] = $description;
		$temp['price'] = $price;
		$temp['image'] = $image;
		$temp['certificate'] = $certificate;
		$temp['name'] = $name;
		$temp['phone'] = $phone;
		$temp['address'] = $address;
	
		array_push($items, $temp);
	}
	$json = json_encode($items); 
	 print($json);
	

?>