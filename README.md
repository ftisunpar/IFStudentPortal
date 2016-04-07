# INFORMATIKA STUDENT PORTAL

## Development

### Setup

Kebutuhan:

* JDK 1.8

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

### Building

Langkah-langkah (pada direktori proyek _Play_ `IFStudentPortal/StudentPortal`):

1. Activator Dist `./activator dist`
2. Hasil deployment ada di `target/universal/ifstudentportal-1.0.zip`.
3. Copy file tersebut ke server tujuan

## Deployment

### HTTPS, Dengan Command Line

Aplikasi bisa dijalankan dengan command line, berbekal project lengkap dari Git.

1. Masuk ke direktori `StudentPortal/certs`
2. Buat file `password` dan isi dengan string acak
3. Generate certificate `./gencerts.sh`
4. Copy file `IFStudentPortal-CA.crt` ke direktori `StudentPortal/public/certs` supaya bisa diunduh pengguna.
5. Jalankan aplikasi dengan `sudo ./activator-https run`. Akses HTTP tetap pada port 9000, sedangkan HTTPS pada 443 (default)
