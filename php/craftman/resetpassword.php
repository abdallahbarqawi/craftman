<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		$email= $_POST['email'];
		
		
		$password= $_POST['password'];	
		$password = password_hash($password,PASSWORD_DEFAULT);
		require_once 'connect.php';
		$sql2 = "select email from user where email ='$email'";
		$response = mysqli_query($con, $sql2);
		
		if(mysqli_num_rows($response) !== 1)
		{
			$result["success"]="2";
			$result["message"]="error";
			echo json_encode($result);
			mysqli_close($con);
		}		
		else{
			$sql = "update user set password = '$password' where email = '$email'";
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