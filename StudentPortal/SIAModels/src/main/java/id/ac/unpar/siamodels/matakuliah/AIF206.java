package id.ac.unpar.siamodels.matakuliah;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF206", nama = "Sistem Operasi", sks = 4)
public class AIF206 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasTempuhKuliah("AIF102") && !mahasiswa.hasTempuhKuliah("AIF192")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF102 atau AIF192");
			ok = false;
		}
		if (!mahasiswa.hasTempuhKuliah("AIF205")) {
			reasonsContainer.add("Tidak memenuhi prasyarat tempuh AIF205");			
			ok = false;
		}
		return ok;
	}

}
