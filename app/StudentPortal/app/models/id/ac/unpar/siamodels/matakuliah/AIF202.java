package models.id.ac.unpar.siamodels.matakuliah;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

import java.util.List;

@MataKuliah(kode = "AIF202", nama = "Desain & Analisis Algoritma", sks = 4)
public class AIF202 implements HasPrasyarat, Wajib {

	@Override
	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
		boolean ok = true;
		if (!mahasiswa.hasLulusKuliah("AIF102") && !mahasiswa.hasLulusKuliah("AIF192")) {
			reasonsContainer.add("Tidak memenuhi prasyarat lulus AIF102 atau AIF192");
			ok = false;
		}
		int angkatan = mahasiswa.getTahunAngkatan();
		if (angkatan >= 2012 && angkatan <= 2014) {
			if (!mahasiswa.hasTempuhKuliah("AIF203")) {
				reasonsContainer.add("Angkatan " + angkatan + " tetapi tidak memenuhi prasyarat tempuh AIF103");			
				ok = false;
			}
		}
		return ok;
	}

}
