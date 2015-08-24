package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

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
