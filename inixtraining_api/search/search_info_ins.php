<?php 

	$id_ins = $_GET['id_ins'];

	//Import File Koneksi Database
	require_once('../koneksi.php');
	
	//Membuat SQL Query
	$sql = "select i.nama_ins, k.id_kls, m.nama_mat, k.tgl_mulai_kls, count(p.id_pst) as jml_pst 
		from tb_detail_kelas dk join tb_peserta p
		on (dk.id_pst = p.id_pst) 
		join tb_kelas k
		on (dk.id_kls = k.id_kls)
		join tb_materi m
		on (m.id_mat = k.id_mat)
		join tb_instruktur i
		on (k.id_ins = i.id_ins)
		where i.id_ins = $id_ins
		group by k.id_kls;";
	
	//Mendapatkan Hasil
	$r = mysqli_query($con,$sql);
	
	//Membuat Array Kosong 
	$result = array();
	
	while($row = mysqli_fetch_array($r)){
		
		//Memasukkan Nama dan ID kedalam Array Kosong yang telah dibuat 
		array_push($result,array(
			"i.nama_ins"=>$row['nama_ins'],
			"k.id_kls"=>$row['id_kls'],
            "m.nama_mat"=>$row['nama_mat'],
            "k.tgl_mulai_kls"=>$row['tgl_mulai_kls'],
            "jml_pst"=>$row['jml_pst']
		));
	}
	
	//Menampilkan Array dalam Format JSON
	echo json_encode(array('result'=>$result));
	
	mysqli_close($con);
?>