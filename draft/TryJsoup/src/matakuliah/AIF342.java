package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

public class AIF342 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasLulusKuliah("AIF341")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF341");			
			return false;
		}
		return true;
	}

}
