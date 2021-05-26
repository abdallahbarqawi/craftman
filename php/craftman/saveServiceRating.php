<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			$id= $_POST['id'];
			$rate= $_POST['rating'];
			$type= "0";
			
			require_once 'connect.php';
			$sSQL= 'SET CHARACTER SET utf8'; 
			mysqli_query($con, $sSQL);
			
			// select the user data from the database
			$sql = "select * from  rating where service_work_id ='$id'";
			
			$response = mysqli_query($con, $sql);
			
			if(mysqli_num_rows($response) === 1)
			{
				$sql = "update rating set rate = '$rate' where service_work_id = '$id' and type = '0'";
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
			}else{
				$sql = "INSERT into rating(rate, service_work_id, type) values ('$rate','$id','$type')";
			
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