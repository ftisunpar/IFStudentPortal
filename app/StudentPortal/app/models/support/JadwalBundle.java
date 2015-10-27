/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models.support;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN7
 */
public class JadwalBundle {
    private final List<JadwalKuliah> jadwalKuliah;
    private final List<JadwalUjian> jadwalUTS;
    private final List<JadwalUjian> jadwalUAS;
    
    public JadwalBundle(){
        jadwalKuliah = new ArrayList<JadwalKuliah>();
        jadwalUTS = new ArrayList<JadwalUjian>();
        jadwalUAS = new ArrayList<JadwalUjian>();
    }

    public List<JadwalKuliah> getJadwalKuliah() {
        return jadwalKuliah;
    }

    public List<JadwalUjian> getJadwalUTS() {
        return jadwalUTS;
    }

    public List<JadwalUjian> getJadwalUAS() {
        return jadwalUAS;
    }
    
}
