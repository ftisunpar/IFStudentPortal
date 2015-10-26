package models.display;

import models.id.ac.unpar.siamodels.MataKuliah;

public class PrasyaratDisplay {
	private MataKuliah mataKuliah;
	private String[] status;
	
	public PrasyaratDisplay(MataKuliah mataKuliah, String[] status){
		this.mataKuliah = mataKuliah;
		this.status = status;
	}
	
	public MataKuliah getMataKuliah(){
		return mataKuliah;
	}
	
	public String[] getStatus(){
		return status;
	}
}
