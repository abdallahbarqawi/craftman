<?php
	if($_SERVER['REQUEST_METHOD'] = 'POST'){
		 
		 $id= $_POST['id'];

		 
		 require_once 'connect.php';
		 
		 $sql = "SELECT u.id, u.name, u.email,u.state,u.phone, a.name as address
			from   user u, address a
			where u.Address_id =a.id and u.type = 1 and u.id = '$id'";
		 
		 $response = mysqli_query($con, $sql);
		 
		 $result = array();
		 $result['read'] = array();
		 
		if(mysqli_num_rows($response) === 1)
		{
			
			if($row = mysqli_fetch_assoc($response))
			{
				$h['id'] =$row['id']; 
				$h['name'] =$row['name']; 
				$h['email'] =$row['email'];
				$h['phone'] =$row['phone'];
				$h['address'] =$row['address'];
				
				array_push($result['read'],$h);
				$result["success"]="1";
				$result["message"]="success";
				echo json_encode($result);
			}
			
		
		
		}
		else{
				$result["success"]="0";
				$result["message"]="error";
				echo json_encode($result);
				mysqli_close($con);
				}
	}









?>