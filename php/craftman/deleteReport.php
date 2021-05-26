<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		$id = $_POST['id'];
		require_once 'connect.php';

		$sql = "delete from report where id = '$id'";
		if(mysqli_query($con, $sql)){
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