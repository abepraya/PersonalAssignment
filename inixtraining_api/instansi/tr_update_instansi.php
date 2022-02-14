<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		//Mendapatkan Nilai Dari Variable 
		$id_instansi = $_POST['id_instansi'];
		$nama_instansi = $_POST['nama_instansi'];
		$alamat_instansi = $_POST['alamat_instansi'];
		
		//import file koneksi database 
		require_once('../koneksi.php');
		
		//Membuat SQL Query
		$sql = "UPDATE tb_instansi SET nama_instansi = '$nama_instansi', alamat_instansi = '$alamat_instansi' WHERE id_instansi = $id_instansi;";
		
		//Meng-update Database 
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Update Data Materi';
		}else{
			echo 'Gagal Update Data Materi';
		}
		
		mysqli_close($con);
	}
?>

