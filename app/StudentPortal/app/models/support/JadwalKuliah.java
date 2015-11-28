package models.support;

import models.id.ac.unpar.siamodels.MataKuliah;

public class JadwalKuliah extends Jadwal{
    private String dosen;
    private String hari;

    public JadwalKuliah(MataKuliah mataKuliah, char kelas, String dosen, String hari, String waktu, String ruang) {
        super(mataKuliah, kelas, waktu, ruang);
        this.dosen = dosen;
        this.hari = hari;
    }
    
    public JadwalKuliah(){
        super();
    }
        
    public String getDosen() {
        return dosen;
    }

    public String getHari() {
        return hari;
    }

}
