<?php
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Mendapatkan Nilai Variable
		$nama_instansi = $_POST['nama_instansi'];
		$alamat_instansi = $_POST['alamat_instansi'];
		
		//Pembuatan Syntax SQL
		$sql = "INSERT INTO tb_instansi (nama_instansi, alamat_instansi) VALUES ('$nama_instansi','$alamat_instansi')";
		
		//Import File Koneksi database
		require_once('../koneksi.php');
		
		//Eksekusi Query database
		if(mysqli_query($con,$sql)){
			echo 'Berhasil Menambahkan Materi';
		}else{
			echo 'Gagal Menambahkan Materi';
		}
		
		mysqli_close($con);
	}
?>