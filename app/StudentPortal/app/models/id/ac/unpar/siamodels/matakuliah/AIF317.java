package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.PilihanWajib;

import java.util.List;

public class AIF317 implements HasPrasyarat, PilihanWajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF210")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF210");
			return false;
		}
		return true;
	}

}
