<?php
	if($_SERVER['REQUEST_METHOD']= 'POST'){
		
		$name= $_POST['name'];
		$phone= $_POST['phone']; 
		$id= $_POST['id'];
		
	
		 
		require_once 'connect.php';
		
		
			$sql1 = "select phone from user where phone ='$phone' and id='$id'";
			$response1 = mysqli_query($con, $sql1);
			$row = mysqli_fetch_assoc($response1);
			$phone1 = $row['phone'];
			
			if($phone === $phone1){
				$sql = "update user set name='$name',phone='$phone' where id = '$id'";
				if(mysqli_query($con, $sql))
				{
					
					$result["success"]="1";
					$result["message"]="success";
					echo json_encode($result);
					mysqli_close($con);
				
				
				}
			}
			else{
				$sql2 = "select phone from user where phone ='$phone'";
				$response = mysqli_query($con, $sql2);
				if(mysqli_num_rows($response) === 1)
				{
					$result["success"]="2";
					$result["message"]="error";
					echo json_encode($result);
					mysqli_close($con);
					
				}else{
					$sql = "update user set name='$name' ,phone='$phone' where id = '$id'";
					if(mysqli_query($con, $sql))
					{
						
						$result["success"]="1";
						$result["message"]="success";
						echo json_encode($result);
						mysqli_close($con);
					
					
					}
				}
				
			}
		
			
		 
		
	}
	else{
			$result["success"]="0";
			$result["message"]="error";
			echo json_encode($result);
			mysqli_close($con);
		}
	
?>