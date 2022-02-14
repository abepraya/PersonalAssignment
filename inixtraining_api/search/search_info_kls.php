<?php 

	$id_kls = $_GET['id_kls'];

	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "SELECT k.id_kls, k.tgl_mulai_kls, k.tgl_akhir_kls, m.nama_mat ,COUNT(p.id_pst) as 'count_id_pst'
			FROM tb_detail_kelas dk
			JOIN tb_kelas k ON (dk.id_kls = k.id_kls)
			JOIN tb_peserta p ON (dk.id_pst = p.id_pst)
			JOIN tb_materi m ON (k.id_mat = m.id_mat)
            WHERE k.id_kls = $id_kls
			GROUP BY k.id_kls;";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"k.id_kls"=>$row['id_kls'],
			"k.tgl_mulai_kls"=>$row['tgl_mulai_kls'],
            "k.tgl_akhir_kls"=>$row['tgl_akhir_kls'],
            "m.nama_mat"=>$row['nama_mat'],
            "count_id_pst"=>$row['count_id_pst']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>