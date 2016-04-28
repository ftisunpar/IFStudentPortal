package models.display;

import id.ac.unpar.siamodels.MataKuliah;

public class PrasyaratDisplay {
	public MataKuliah mataKuliah;
	public String[] status;

	public PrasyaratDisplay(MataKuliah mataKuliah, String[] status) {
		this.mataKuliah = mataKuliah;
		this.status = status;
	}
}
