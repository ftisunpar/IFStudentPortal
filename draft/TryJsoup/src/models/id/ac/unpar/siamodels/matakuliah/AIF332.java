package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

public class AIF332 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		// Note: Topik Khusus Informatika 2: Ruby on Rails
		if (!mahasiswa.hasTempuhKuliah("AIF315")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF315");			
			return false;
		}
		return true;
	}

}
