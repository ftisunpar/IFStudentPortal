package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

public class AIF208 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		if (!mahasiswa.hasTempuhKuliah("AIF201")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF201");			
			return false;
		}
		return true;
	}

}
