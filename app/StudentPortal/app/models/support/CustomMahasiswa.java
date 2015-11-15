package models.support;

import java.util.ArrayList;
import java.util.List;

import models.id.ac.unpar.siamodels.Mahasiswa;

public class CustomMahasiswa extends Mahasiswa{
	protected List<JadwalKuliah> jadwalList;
	protected String photoPath;
	
	public CustomMahasiswa(String npm) throws NumberFormatException {
		super(npm);
		this.jadwalList = new ArrayList<JadwalKuliah>();
	}
	
	public String getPhotoPath(){
    	return photoPath;
    }
	
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	public List<JadwalKuliah> getJadwalList(){
    	return jadwalList;
    }
	
	public void setJadwalList(List<JadwalKuliah> jadwalList){
    	this.jadwalList = jadwalList;
    }

}
