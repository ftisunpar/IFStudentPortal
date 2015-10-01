package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

public class AIF312 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF305")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF305");			
			return false;
		}
		return true;
	}

}
