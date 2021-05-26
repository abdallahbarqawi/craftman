<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			$user_id= $_POST['id'];
			$service_id= $_POST['service_id'];
			$report= $_POST['report'];
			$type=  $_POST['type'];
			
			require_once 'connect.php';
			$sSQL= 'SET CHARACTER SET utf8'; 
			mysqli_query($con, $sSQL);
			
			
			
			$sql = "INSERT into report(report, service_work_id,user_id, type) values ('$report','$service_id', '$user_id','$type')";
			
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


?>