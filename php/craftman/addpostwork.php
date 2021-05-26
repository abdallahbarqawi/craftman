<?php
	//register page for android 
	if ($_SERVER["REQUEST_METHOD"] = "POST")
	{
		
			$title= $_POST['title'];
			$description= $_POST['description'];
			$category= $_POST['category'];
			$id= $_POST['id'];
			$address= $_POST['address'];
			$price= $_POST['price'];
			$work_image = $_POST["work_image"];
			$certificate_image = $_POST["certificate_image"];
            
            $rand_work = rand(000000,999999);
            $rand_cert = rand(000000,999999);
            $image_work_url="$rand_work";
            $image_cert_url="$rand_cert";
            $url_work = "$image_work_url.jpg";
            $url_cert = "$image_cert_url.jpg";
            $uploadpath = "Images/$image_work_url.jpg";
            $uploadpath1 = "Images/$image_cert_url.jpg";
                      
			
			
			
			
			require_once 'connect.php';
			$sSQL= 'SET CHARACTER SET utf8'; 
			mysqli_query($con, $sSQL);
			
			$sql3 = "select id from address where name ='$address'";
			$response1 = mysqli_query($con, $sql3);
			$row = mysqli_fetch_assoc($response1);
			$address1 = $row['id'];
			
			$sql4 = "select id from category where name ='$category'";
			$response2 = mysqli_query($con, $sql4);
			$row1 = mysqli_fetch_assoc($response2);
			$category1 = $row1['id'];
			
			
			$sql = "INSERT into work(title, description, price, image, certificate, user_id, category_id, address_id) values ('$title','$description','$price','$url_work','$url_cert', '$id', '$category1', '$address1' )";
			
			if(mysqli_query($con, $sql))
				{
				    file_put_contents($uploadpath,base64_decode($work_image));
				    file_put_contents($uploadpath1,base64_decode($certificate_image));
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