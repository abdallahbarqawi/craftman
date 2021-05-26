<?php
		//login page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			// store data from android 
			$phone = $_POST['phone'];	
		
			

			require_once 'connect.php';
			
			// select the user data from the database
		$sql2 = "select phone from user where phone ='$phone'";
		$response = mysqli_query($con, $sql2);
		if(mysqli_num_rows($response) === 1)
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


?>