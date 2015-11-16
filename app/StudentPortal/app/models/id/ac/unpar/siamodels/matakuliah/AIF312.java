package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;

import java.util.List;

@MataKuliah(kode = "AIF312", nama = "Keamanan Informasi", sks = 2)
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
