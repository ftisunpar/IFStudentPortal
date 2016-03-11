# INFORMATIKA STUDENT PORTAL

## Development - SBT Native Packager

### Setup

Langkah-langkah:

1. Siapkan project IFStudentPortal dengan cara fork dari https://github.com/ftisunpar/IFStudentPortal di directory home user yang anda gunakan. contoh : /home/projectUser/IFStudentPortal/StudentPortal

2. Ubah beberapa file konfigurasi aplikasi anda yakni :
* /home/projectUser/IFStudentPortal/StudentPortal/build.sbt
	tambahkan :
		enablePlugins(JavaServerAppPackaging)
		enablePlugins(UniversalPlugin)
		import com.typesafe.sbt.packager.archetypes.ServerLoader.{SystemV, Upstart}
		serverLoading in Debian := SystemV
* /home/projectUser/IFStudentPortal/StudentPortal/project/plugins.sbt
	tambahkan :
		addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0-RC1")
3. Execute activator project anda /home/projectUser/IFStudentPortal/StudentPortal/activator

4. Dengan menjalankan command "dist" , project anda akan di-package sehingga menghasilkan file zip project anda pada /home/projectUser/IFStudentPortal/StudentPortal/target/universal/ifstudentportal-1.0.zip (misalnya)

5. Unzip file ifstudentportal-1.0.zip tersebut ke directory yang diinginkan (contoh: /home/i13065/ifstudentportal-1.0.zip), dan akan menghasilkan folder project dengan nama 'ifstudentportal-1.0'

6. Di dalam folder /home/i13065/ifstudentportal-1.0 ada beberapa file dan folder. Fokus pada folder bin, di dalamnya ada script untuk menjalankan project IFStudentPortal ini dengan nama 'ifstudentportal'

7. Script tersebut dapat dijalankan hanya jika berada di dalam project tersebut, karena berbagai variable di dalam script tersebut memang sudah diatur oleh native packager (SBT) untuk mengacu pada file-file yang berada di parent directory-nya. Maka, kelompok kami menyarankan untuk menjalankan sebuah script yang memanggil script ifstudentportal tersebut.

8. Buatlah sebuah file dengan nama 'ifstudentportal' di folder /etc/init.d/

9. File tersebut adalah sebuah script berisi :
	#!/bin/bash
	# IFStudentPortal Run Script
	/home/i13065/ifstudentportal-1.0/bin/ifstudentportal -Dhttp.port=80

--END-OF-FILE--

Note:
* '/home/i13065/ifstudentportal-1.0/bin/ifstudentportal' disesuaikan dengan path di mana anda meng-unzip ifstudentportal-1.0.zip.
* option tambahan -Dhttp.port=80 tersebut untuk menjalankan project IFStudentPortal di port 80.

10. Aturlah owner dari script tersebut. Jika anda ingin semua user dapat menjalankan script tersebut, gunakan command 'chmod 777 /etc/init.d/ifstudentportal'. Sesuaikan permission script tersebut untuk menjalankan project IFStudentPortal. Pastikan juga file /etc/init.d/ifstudentportal mempunyai file permission dan owner yang sama dengan /home/i13065/ifstudentportal-1.0/bin/ifstudentportal

11. Sekarang, project anda telah siap dijalankan menggunakan System V dengan command : service ifstudentportal start



NOTE : untuk memudahkan pengerjaan di atas, kelompok kami menyediakan file project dan script sesuai dengan prosedur di atas dengan nama 'ifstudentportal-run-systemv.zip'.

Tata cara pemakaian 'ifstudentportal-run-systemv.zip' :
* Unzip file tersebut sehingga menghasilkan 2 file yaitu 'ifstudentportal-project' dan 'ifstudentportal'
* 'ifstudentportal-project' adalah file project yang kita gunakan. (ifstudentportal-1.0 jika di tutorial install di atas)
* 'ifstudentportal' adalah script yang digunakan untuk menjalankan project dengan System V
* Copy/pindahkan file tersebut ('ifstudentportal-project' dan 'ifstudentportal' ke folder /etc/init.d)
* Jalankan project tersebut dengan menjalankan command 'service ifstudentportal start'
