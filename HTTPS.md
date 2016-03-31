# HTTPS

## Generate Certificate

### Setup

Langkah-langkah membuat certificate untuk domain https://studentportal-if.ftis.unpar :
 
1. Masuk ke direktori proyek `cd IFStudentPortal`
2. Masuk ke direktori proyek _Play_ `cd StudentPortal`
3. Masuk ke direktori _scripts_ `cd scripts`
4. Ubahlah isi pada file `genserver.sh` :
  * Ubah CN=localhost:9000 menjadi CN=studentportal-if.ftis.unpar 
  * Ubah `-ext SAN="DNS:localhost"` menjadi `-ext SAN="DNS:studentportal-if.ftis.unpar"`
  * Save
5. Generate certificate `./gencerts.sh`
6. Pindahkan file yang digenerate ke `StudentPortal/certs`
7. Import `IFStudentPortal-CA.crt` pada browser
8. Tambahkan `IFStudentPortal-CA.crt` ke `StudentPortal/public/certs`

### Running

Langkah-langkah run HTTPS (pada direktori proyek _Play_ `IFStudentPortal/StudentPortal`):

1. Activator Run `./activator-https run`
2. Buka [https://localhost:9000](https://localhost:9000) di browser
