# INFORMATIKA STUDENT PORTAL

## Development

### Setup

Kebutuhan:

* JDK 1.8
* Git

Langkah-langkah:

1. Clone repository `git clone https://github.com/ftisunpar/IFStudentPortal`
2. Masuk ke direktori proyek `cd IFStudentPortal`
3. Submodule update `git submodule update --init -- StudentPortal/SIAModels`
4. Masuk ke direktori proyek _Play_ `cd StudentPortal`
5. Create Eclipse project `./activator eclipse`
6. _Import project_ di Eclipse (File > Import > Existing Projects into Workspace)

### Running

Langkah-langkah (pada direktori proyek _Play_ `IFStudentPortal/StudentPortal`):

1. Activator Run `./activator run`
2. Buka [http://localhost:9000](http://localhost:9000) di browser

### Testing

_Saat ini hanya bisa dilakukan di Linux_

Langkah-langkah (pada direktori proyek _Play_ `IFStudentPortal/StudentPortal`):

1. Install [Firefox](https://www.mozilla.org/en-US/firefox/new/)
2. Copy `functionaltest-template.conf` pada direktori `conf/` ke `functionaltest.conf` dan sesuaikan isinya dengan username/password yang benar
3. Activator Test `./activator test`

## Production

### Building

Kebutuhan:

* Linux
* JDK 1.8
* Git

Langkah-langkah:

1. Clone repository `git clone https://github.com/ftisunpar/IFStudentPortal`
2. Masuk ke direktori proyek `cd IFStudentPortal`
3. Submodule update `git submodule update --init -- StudentPortal/SIAModels`
4. Masuk ke direktori konfigurasi proyek _Play_ `cd StudentPortal/conf`
5. Buat password acak: ketik `cat > password.conf`, ketikkan huruf/angka acak, dan tekan Ctrl+D
6. Pindah ke direktori sertifikat `cd ../certs`
7. Jalankan script untuk membuat sertifikat `./gencerts.sh`
8. Naik satu level direktori `cd ..` dan eksekusi build `./activator stage`

Hasil build ada di `StudentPortal/target/universal/stage`

### Running

1. Masuk ke direktori `StudentPortal/target/universal/stage/bin`
2. Jalankan `./ifstudentportal -Dhttps.port=9443 -Dhttps.keystore.path=conf/IFStudentPortal.jks -Dhttps.keystore.password="$(cat ../conf/password.conf)"` (TODO: sertifikat masih pakai default, application secret not set)
