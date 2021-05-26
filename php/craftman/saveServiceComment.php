<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			$id= $_POST['id'];
			$comment= $_POST['comment'];
			$type= "0";
			
			require_once 'connect.php';
			$sSQL= 'SET CHARACTER SET utf8'; 
			mysqli_query($con, $sSQL);
			
			
			
			$sql = "INSERT into comments(comment, service_work_id, type) values ('$comment','$id','$type')";
			
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