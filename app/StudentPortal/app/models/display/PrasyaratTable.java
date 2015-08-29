package models.display;

import models.id.ac.unpar.siamodels.MataKuliah;

public class PrasyaratTable {
	public MataKuliah mk;
	public String status;
	
	public PrasyaratTable(MataKuliah mk, String status){
		this.mk = mk;
		this.status = status;
	}
}
