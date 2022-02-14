<?php 
 //Mendapatkan Nilai ID
 $id_instansi = $_GET['id_instansi'];
 
 //Import File Koneksi Database
 require_once('../koneksi.php');
 
 //Membuat SQL Query
 $sql = "DELETE FROM tb_instansi WHERE id_instansi=$id_instansi;";

 
 //Menghapus Nilai pada Database 
 if(mysqli_query($con,$sql)){
 echo 'Berhasil Menghapus Materi';
 }else{
 echo 'Gagal Menghapus Materi';
 }
 
 mysqli_close($con);
 ?>