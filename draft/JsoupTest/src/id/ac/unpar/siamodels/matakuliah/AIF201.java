package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

public class AIF201 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasLulusKuliah("AIF101") && !mahasiswa.hasLulusKuliah("AIF191")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF101 atau AIF191");
			ok = false;
		}
		return ok;
	}

}
