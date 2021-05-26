<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
		$name= $_POST['name'];
		$email= $_POST['email'];
		$phone= $_POST['phone'];
		$password= $_POST['password'];
		$address= $_POST['address'];
		$type= "1";
		$stat= "1";
		
		
		$password = password_hash($password,PASSWORD_DEFAULT);
		
		
		require_once 'connect.php';
		
		$sql3 = "select id from address where name ='$address'";
		$response1 = mysqli_query($con, $sql3);
		$row = mysqli_fetch_assoc($response1);
		$address1 = $row['id'];
			
			
		$sql2 = "select email from user where email ='$email'";
		$response = mysqli_query($con, $sql2);
		if(mysqli_num_rows($response) === 1)
		{
			$result["success"]="2";
			$result["message"]="error";
			echo json_encode($result);
			mysqli_close($con);
		}		
		else{
			$sql = "INSERT INTO user(name, email, password, phone, type, Address_id, state) values ('$name','$email','$password','$phone','$type','$address1','$stat')";
			if(mysqli_query($con, $sql))
				{
					$result["success"]="1";
					$result["message"]="success";
					echo json_encode($result);
					mysqli_close($con);
				}else{
					$result["success"]="0";
					$result["message"]="error";
					echo json_encode($result);
					mysqli_close($con);
				}
		}
					
	}


?>