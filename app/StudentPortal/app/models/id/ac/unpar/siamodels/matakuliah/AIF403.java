package models.id.ac.unpar.siamodels.matakuliah;

import java.util.List;

import models.id.ac.unpar.siamodels.Mahasiswa;
import models.id.ac.unpar.siamodels.MataKuliah;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import models.id.ac.unpar.siamodels.matakuliah.interfaces.Wajib;

@MataKuliah(kode = "AIF403", nama = "Komputer & Masyarakat", sks = 2)
public class AIF403 implements HasPrasyarat, Wajib {

      	@Override
      	public boolean checkPrasyarat(Mahasiswa mahasiswa, List<String> reasonsContainer) {
      		int sksLulus = mahasiswa.calculateSKSLulus();
      		if (sksLulus < 70) {
      			reasonsContainer.add("SKS Lulus " + sksLulus + ", belum mencapai syarat minimal 70");			
      			return false;
      		}
      		return true;
      	}
      	
}
