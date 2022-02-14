<?php 
	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT kls.id_kls, kls.tgl_mulai_kls, kls.tgl_akhir_kls, mat.nama_mat, ins.nama_ins
			FROM `tb_kelas` kls JOIN `tb_materi` mat
			ON kls.id_mat = mat.id_mat
			JOIN `tb_instruktur` ins
			ON kls.id_ins = ins.id_ins
			ORDER BY kls.tgl_mulai_kls;";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"id_kls"=>$row['id_kls'],
			"tgl_mulai_kls"=>$row['tgl_mulai_kls'],
			"tgl_akhir_kls"=>$row['tgl_akhir_kls'],
			"nama_ins"=>$row['nama_ins'],
			"nama_mat"=>$row['nama_mat']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>