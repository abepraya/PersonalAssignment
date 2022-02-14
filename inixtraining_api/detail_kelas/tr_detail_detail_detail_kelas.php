<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$id_detail_kls = $_GET['id_detail_kls'];
	
	//Importing database
	require_once('../koneksi.php');
	
	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM tb_detail_kelas WHERE id_detail_kls=$id_detail_kls";
	
	//Mendapatkan Hasil 
	$r = mysqli_query($con,$sql);
	
	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
			"id_detail_kls"=>$row['id_detail_kls'],
			"id_kls"=>$row['id_kls'],
			"id_pst"=>$row['id_pst'],
		));

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>