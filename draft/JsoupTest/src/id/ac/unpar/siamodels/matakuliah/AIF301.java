package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

public class AIF301 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF204") && !mahasiswa.hasTempuhKuliah("AIF294")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF204 atau AIF294");
			ok = false;
		}
		return ok;
	}

}
