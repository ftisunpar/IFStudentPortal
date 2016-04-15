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
2. Jalankan salah satu dari:
    * `./ifstudentportal -Dhttps.port=9443 -Dplay.server.https.keyStore.path=../conf/IFStudentPortal.jks -Dplay.server.https.keyStore.password="$(cat ../conf/password.conf)" -Dapplication.secret="$(cat ../conf/password.conf)"`
    * `sudo ./ifstudentportal -Dhttps.port=443 -Dplay.server.https.keyStore.path=../conf/IFStudentPortal.jks -Dplay.server.https.keyStore.password="$(cat ../conf/password.conf)" -Dapplication.secret="$(cat ../conf/password.conf)"`

### Autostart

Di Ubuntu, buat file `/etc/init.d/ifstudentportal` yang isinya seperti di bawah (lihat keterangan juga di bawah):

```bash
#!/bin/bash
#
### BEGIN INIT INFO
# Provides:          ifstudentportal
# Required-Start:
# Required-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start and stop the ifstudentportal server daemon
# Description:       Start and stop the ifstudentportal server daemon
### END INIT INFO
#

# Get function from functions library
. /lib/lsb/init-functions
# Start the service IFStudentPortal
PIDNUM=""
HOMEDIR="/home/pascal/IFStudentPortal/StudentPortal/target/universal/stage/"
PIDFILE="RUNNING_PID"
PIDDIR="$HOMEDIR$PIDFILE"
BINFILE="bin/ifstudentportal"
BINDIR="$HOMEDIR$BINFILE"

start() {
    if [ ! -f $PIDDIR ]
    then
        $BINDIR -Dhttps.port=443 -Dplay.server.https.keyStore.path=$HOMEDIR/conf/IFStudentPortal.jks -Dplay.server.https.keyStore.password="$(cat $HOMEDIR/conf/password.conf)" -Dapplication.secret="$(cat $HOMEDIR/conf/password.conf)" &
        echo "IFStudentPortal Started"
    else
        echo "IFStudentPortal is already Running"
    fi
}
# Restart the service IFStudentPortal
stop() {
    if [ -f $PIDDIR ]
    then
        PIDNUM=$(cat $PIDDIR)
        kill $PIDNUM
        ### Now, delete the pid file ###
        rm -f $PIDDIR
        echo "IFStudentPortal Stopped"
    else
        echo "IFStudentPortal is not Running"
    fi
}
status() {
    if [ -f $PIDDIR ]
    then
        echo "IFStudentPortal is Running"
    else
        echo "IFStudentPortal is not Running"
    fi
}
restart() {
    if [ -f $PIDDIR ]
    then 
        stop
        start
    else
        echo "IFStudentPortal is not Running"
    fi
}

### main logic ###
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  status)
        status
        ;;
  restart|reload|condrestart)
        restart
        ;;
  *)
        echo $"Usage: $0 {start|stop|restart|reload|status}"
        exit 1
esac
exit 0
```

Keterangan:

1. Ganti `$HOMEDIR` dengan path yang sesuai
2. `chmod` file tersebut dengan 744 (sebaiknya hanya boleh dieksekusi oleh `root`)
3. Ketikkan perintah `sudo update-rc.d ifstudentportal` untuk mendaftarkan ke startup

