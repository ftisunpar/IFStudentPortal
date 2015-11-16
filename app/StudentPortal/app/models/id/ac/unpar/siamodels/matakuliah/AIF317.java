package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.PilihanWajib;

import java.util.List;

@MataKuliah(kode = "AIF317", nama = "Desain Antarmuka Grafis", sks = 0)
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
