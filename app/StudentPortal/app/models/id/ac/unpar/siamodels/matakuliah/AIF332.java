package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

@MataKuliah(kode = "AIF332", nama = "Topik Khusus Informatika 2", sks = 3)
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
