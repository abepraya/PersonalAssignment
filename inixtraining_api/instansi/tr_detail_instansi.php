<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$id_instansi = $_GET['id_instansi'];
	
	//Importing database
	require_once('../koneksi.php');
	
	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "SELECT * FROM tb_instansi  WHERE id_instansi=$id_instansi";
	
	//Mendapatkan Hasil 
	$r = mysqli_query($con,$sql);
	
	//Memasukkan Hasil Kedalam Array
	$result = array();
	$row = mysqli_fetch_array($r);
	array_push($result,array(
			"id_instansi"=>$row['id_instansi'],
			"nama_instansi"=>$row['nama_instansi'],
			"alamat_instansi"=>$row['alamat_instansi']
		));

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>