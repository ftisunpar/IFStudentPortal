# INFORMATIKA STUDENT PORTAL

## Development - SBT Native Packager

### Setup

Langkah-langkah:

1. Siapkan project **IFStudentPortal** dengan cara fork dari https://github.com/ftisunpar/IFStudentPortal di directory home user yang anda gunakan. contoh : **/home/projectUser/IFStudentPortal/StudentPortal**.

2. Ubah beberapa file konfigurasi aplikasi anda yakni
  * **/home/projectUser/IFStudentPortal/StudentPortal/build.sbt** tambahkan:
     <pre><code>enablePlugins(JavaServerAppPackaging)
                enablePlugins(UniversalPlugin)
                import com.typesafe.sbt.packager.archetypes.ServerLoader.{SystemV, Upstart}
                serverLoading in Debian := SystemV</code></pre>
  * **/home/projectUser/IFStudentPortal/StudentPortal/project/plugins.sbt** tambahkan :
		<pre><code>addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0-RC1")</code></pre>
3. Execute activator project anda <pre><code>/home/projectUser/IFStudentPortal/StudentPortal/activator</code></pre>

4. Dengan menjalankan command <pre><code>dist</code></pre>project anda akan di-package sehingga menghasilkan file zip project anda pada contoh: <pre><code>/home/projectUser/IFStudentPortal/StudentPortal/target/universal/ifstudentportal-1.0.zip</code></pre>

5. Unzip file **ifstudentportal-1.0.zip** tersebut ke directory yang diinginkan contoh: <pre><code>/home/i13065/ifstudentportal-1.0.zip</code></pre>dan akan menghasilkan folder project dengan nama **ifstudentportal-1.0**.

6. Di dalam folder <pre><code>/home/i13065/ifstudentportal-1.0</code></pre> ada beberapa file dan folder. Fokus pada folder bin, di dalamnya ada script untuk menjalankan project **IFStudentPortal** ini dengan nama **ifstudentportal**.

7. Script tersebut dapat dijalankan hanya jika berada di dalam project tersebut, karena berbagai variable di dalam script tersebut memang sudah diatur oleh native packager (SBT) untuk mengacu pada file-file yang berada di parent directory-nya. Maka, kelompok kami menyarankan untuk menjalankan sebuah script yang memanggil script ifstudentportal tersebut.

8. Buatlah sebuah file dengan nama **ifstudentportal** pada folder **/etc/init.d/**.

9. File tersebut adalah sebuah script berisi :
	<pre><code>#!/bin/bash
	# IFStudentPortal Run Script
	/home/i13065/ifstudentportal-1.0/bin/ifstudentportal -Dhttp.port=80</code></pre>

**NOTE:**
* **/home/i13065/ifstudentportal-1.0/bin/ifstudentportal** disesuaikan dengan path di mana anda meng-unzip **ifstudentportal-1.0.zip**.
* option tambahan <pre><code>-Dhttp.port=80</code></pre> digunakan untuk menjalankan project **IFStudentPortal** di port **80**.

10. Aturlah owner dari script tersebut. Jika anda ingin semua user dapat menjalankan script tersebut, gunakan command <pre><code>chmod 777 /etc/init.d/ifstudentportal</code></pre>Sesuaikan permission script tersebut untuk menjalankan project **IFStudentPortal**. Pastikan juga file <pre><code>/etc/init.d/ifstudentportal</code></pre> mempunyai file permission dan owner yang sama dengan <pre><code>/home/i13065/ifstudentportal-1.0/bin/ifstudentportal</code></pre>

11. Sekarang, project anda telah siap dijalankan menggunakan System V dengan command : <pre><code>service ifstudentportal start</code></pre>



**NOTE:** untuk memudahkan pengerjaan di atas, kelompok kami menyediakan file project dan script sesuai dengan prosedur di atas dengan nama **ifstudentportal-run-systemv.zip**.

Tata cara pemakaian **ifstudentportal-run-systemv.zip** :
* Unzip file tersebut sehingga menghasilkan 2 file yaitu
**ifstudentportal-project** dan **ifstudentportal**
* **ifstudentportal-project** adalah file project yang kita gunakan. (ifstudentportal-1.0 jika di tutorial install di atas)
* **ifstudentportal** adalah script yang digunakan untuk menjalankan project dengan System V
* Copy/pindahkan file **ifstudentportal-project** dan **ifstudentportal** ke folder <pre><code>/etc/init.d</code></pre>
* Jalankan project tersebut dengan menjalankan command <pre><code>service ifstudentportal start</code></pre>
