<?php
		//login page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			// store data from android 
			$email = $_POST['email'];	
			$password = $_POST['password'];
			

			require_once 'connect.php';
			
			// select the user data from the database
		$sql = "select * from  user where email ='$email' and state = 1";
			
			
			$response = mysqli_query($con, $sql);
			
			$result = array();
			$result['login'] = array();
			
			if(mysqli_num_rows($response) === 1)
			{
				
				$row = mysqli_fetch_assoc($response);
				
				if(password_verify($password, $row['password'])){
					$index['id'] = $row['id'];
					$index['name']= $row['name'];
					$index['email'] = $row['email'];
					$index['type'] = $row['type'];
					
					
					array_push($result['login'],$index);
					
					$result["success"]="1";
					$result["message"]="success";
					echo json_encode($result);
					mysqli_close($con);
				}else{
					$result["success"]="0";
					$result["message"]="error";
					echo json_encode($result);
					
					}
			}
			
		
		
		
	}


?>