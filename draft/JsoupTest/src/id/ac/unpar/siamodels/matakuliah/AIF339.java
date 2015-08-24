package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

public class AIF339 implements HasPrasyarat {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF104")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF104");
			ok = false;
		}
		if (!mahasiswa.hasTempuhKuliah("AIF208")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF208");			
			ok = false;
		}
		return ok;
	}

}
