package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF401", nama = "Skripsi 1", sks = 4)
public class AIF401 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasLulusKuliah("AIF302")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF302");
			ok = false;
		}
		int sksLulus = mahasiswa.calculateSKSLulus();		
		if (sksLulus < 108) {
			reasonsContainer.add("SKS Lulus " + sksLulus + ", belum mencapai syarat minimal 108");			
			return false;
		}
		return ok;
	}

}
