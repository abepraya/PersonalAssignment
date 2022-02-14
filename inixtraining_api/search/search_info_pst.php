<?php 
	//Mendapatkan Nilai Dari Variable ID Pegawai yang ingin ditampilkan
	$id_pst = $_GET['id_pst'];
	
	//Importing database
	require_once('../koneksi.php');
	
	//Membuat SQL Query dengan pegawai yang ditentukan secara spesifik sesuai ID
	$sql = "select p.id_pst, p.nama_pst, dk.id_detail_kls, dk.id_kls, m.nama_mat 
		from tb_peserta p join tb_detail_kelas dk on (p.id_pst = dk.id_pst)
		join tb_kelas k on (k.id_kls = dk.id_kls)
		join tb_materi m on (m.id_mat = k.id_mat) 
		where p.id_pst=$id_pst";
	
	//Mendapatkan Hasil 
	$r = mysqli_query($con,$sql);
	
	//Memasukkan Hasil Kedalam Array
	$result = array();
	while($row = mysqli_fetch_array($r)) {
	array_push($result,array(
			"p.id_pst"=>$row['id_pst'],
			"p.nama_pst"=>$row['nama_pst'],
			"dk.id_detail_kls"=>$row['id_detail_kls'],
			"dk.id_kls"=>$row['id_kls'],
			"m.nama_mat"=>$row['nama_mat'],
		));
	}

	//Menampilkan dalam format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>