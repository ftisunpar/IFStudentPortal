package id.ac.unpar.siamodels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Mahasiswa {
	protected final String npm;
	protected String nama;
	protected final List<Nilai> riwayatNilai;
	
	public Mahasiswa(String npm) throws NumberFormatException {
		super();
		if (!npm.matches("[0-9]{10}")) {
			throw new NumberFormatException("NPM tidak valid!");
		}
		this.npm = npm;
		this.riwayatNilai = new ArrayList<Nilai>();
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNpm() {
		return npm;
	}

	public String getEmailAddress() {
		return npm.substring(4, 6) + npm.substring(2, 4) + npm.substring(7, 10) + "@student.unpar.ac.id";
	}
	
	public List<Nilai> getRiwayatNilai() {
		return riwayatNilai;
	}

	/**
	 * Menghitung IPK mahasiswa sampai saat ini, dengan aturan:
	 * <ul>
	 *   <li>Kuliah yang tidak lulus tidak dihitung
	 *   <li>Jika pengambilan beberapa kali, diambil <em>nilai terbaik</em>.
	 * </ul>
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * @return IPK Lulus
	 * @throws ArrayIndexOutOfBoundsException jika tidak ada nilai
	 */
	public double calculateIPKLulus() throws ArrayIndexOutOfBoundsException {
		List<Nilai> riwayatNilai = getRiwayatNilai();
		if (riwayatNilai.size() == 0) {
			throw new ArrayIndexOutOfBoundsException("Minimal harus ada satu nilai untuk menghitung IPK Lulus");
		}		
		Map<String, Double> nilaiTerbaik = new HashMap<String, Double>();
		int totalSKS = 0;
		// Cari nilai lulus terbaik setiap kuliah
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getNilaiAkhir() == null || nilai.getNilaiAkhir().equals('E')) {
				continue;
			}
			String kodeMK = nilai.getMataKuliah().getKode();
			if (!nilaiTerbaik.containsKey(kodeMK)) {
				totalSKS += nilai.getMataKuliah().getSKS();
				nilaiTerbaik.put(kodeMK, nilai.getMataKuliah().getSKS() * nilai.getAngkaAkhir());
			} else if (nilai.getAngkaAkhir() > nilaiTerbaik.get(kodeMK)) {
				nilaiTerbaik.put(kodeMK, nilai.getMataKuliah().getSKS() * nilai.getAngkaAkhir());
			}
		}
		// Hitung IPK dari nilai-nilai terbaik
		double totalNilai = 0.0;
		for (Double nilaiAkhir: nilaiTerbaik.values()) {
			totalNilai += nilaiAkhir;
		}
		return totalNilai / totalSKS;
	}
	
	/**
	 * Menghitung IPS semester terakhir sampai saat ini, dengan aturan:
	 * <ul>
	 *   <li>Kuliah yang tidak lulus <em>dihitung</em>.
	 * </ul>
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * @return nilai IPS sampai saat ini
	 * @throws ArrayIndexOutOfBoundsException jika belum ada nilai satupun
	 */
	public double calculateIPS() throws ArrayIndexOutOfBoundsException {
		List<Nilai> riwayatNilai = getRiwayatNilai();
		if (riwayatNilai.size() == 0) {
			throw new ArrayIndexOutOfBoundsException("Minimal harus ada satu nilai untuk menghitung IPS");
		}
		int lastIndex = riwayatNilai.size() - 1;
		int semester = riwayatNilai.get(lastIndex).getSemester();
		int tahunAjaran = riwayatNilai.get(lastIndex).getTahunAjaran();
		double totalNilai = 0;
		double totalSKS = 0;
		for (int i = lastIndex; i >= 0; i--) {
			Nilai nilai = riwayatNilai.get(i);
			if (nilai.semester == semester && nilai.tahunAjaran == tahunAjaran) {
				if (nilai.getAngkaAkhir() != null) {
					totalNilai += nilai.getMataKuliah().getSKS() * nilai.getAngkaAkhir();
					totalSKS += nilai.getMataKuliah().getSKS();
				}
			} else {
				break;
			}
		}
		return totalNilai / totalSKS;
	}
	
	/**
	 * Menghitung jumlah SKS lulus mahasiswa saat ini.
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * @return SKS Lulus
	 * @throws ArrayIndexOutOfBoundsException jika tidak ada nilai
	 */
	public int calculateSKSLulus() throws ArrayIndexOutOfBoundsException {
		List<Nilai> riwayatNilai = getRiwayatNilai();
		if (riwayatNilai.size() == 0) {
			throw new ArrayIndexOutOfBoundsException("Minimal harus ada satu nilai untuk menghitung SKS Lulus");
		}		
		Set<String> sksTerhitung = new HashSet<String>();
		int totalSKS = 0;
		// Tambahkan SKS setiap kuliah
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getNilaiAkhir() == null || nilai.getNilaiAkhir().equals('E')) {
				continue;
			}
			String kodeMK = nilai.getMataKuliah().getKode();
			if (!sksTerhitung.contains(kodeMK)) {
				totalSKS += nilai.getMataKuliah().getSKS();
				sksTerhitung.add(kodeMK);
			}			
		}
		return totalSKS;
	}	
	
	/**
	 * Memeriksa apakah mahasiswa ini sudah lulus mata kuliah tertentu. Kompleksitas O(n).
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * Note: jika yang dimiliki adalah {@link MataKuliah}, gunakanlah {@link MataKuliah#getKode()}.
	 * @param kodeMataKuliah kode mata kuliah yang ingin diperiksa kelulusannya.
	 * @return true jika sudah pernah mengambil dan lulus, false jika belum
	 */
	public boolean hasLulusKuliah(String kodeMataKuliah) {
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getMataKuliah().getKode().equals(kodeMataKuliah)) {
				Character na = nilai.getNilaiAkhir();
				if (na != null && na >= 'A' && na <= 'D') {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Memeriksa apakah mahasiswa ini sudah pernah menempuh mata kuliah tertentu. Kompleksitas O(n).
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah ada isinya!
	 * Note: jika yang dimiliki adalah {@link MataKuliah}, gunakanlah {@link MataKuliah#getKode()}.
	 * @param kodeMataKuliah kode mata kuliah yang ingin diperiksa.
	 * @return true jika sudah pernah mengambil, false jika belum
	 */
	public boolean hasTempuhKuliah(String kodeMataKuliah) {
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getMataKuliah().getKode().equals(kodeMataKuliah)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Mendapatkan tahun angkatan mahasiswa ini, berdasarkan NPM nya
	 * @return tahun angkatan
	 */
	public int getTahunAngkatan() {
		return Integer.parseInt(getNpm().substring(0, 4));
	}
	
	@Override
	public String toString() {
		return "Mahasiswa [npm=" + npm + ", nama=" + nama + "]";
	}
	
	/**
	 * Merepresentasikan nilai yang ada di riwayat nilai mahasiswa
	 * @author pascal
	 *
	 */
	public static class Nilai {
		/** Tahun ajaran kuliah ini diambil */
		protected final int tahunAjaran;
		/** Semester kuliah ini diambil, salah satu dari {@link Semester} */
		protected final int semester;
		/** Mata kuliah yang diambil */
		protected final MataKuliah mataKuliah;
		/** Kelas kuliah */
		protected final Character kelas;
		/** Nilai ART */
		protected final Double nilaiART;
		/** Nilai UTS */
		protected final Double nilaiUTS;
		/** Nilai UAS */
		protected final Double nilaiUAS;
		/** Nilai Akhir */
		protected final Character nilaiAkhir;

		public Nilai(int tahunAjaran, int semester, MataKuliah mataKuliah,
				Character kelas, Double nilaiART, Double nilaiUTS, Double nilaiUAS,
				Character nilaiAkhir) {
			super();
			this.tahunAjaran = tahunAjaran;
			this.semester = semester;
			this.mataKuliah = mataKuliah;
			this.kelas = kelas;
			this.nilaiART = nilaiART;
			this.nilaiUTS = nilaiUTS;
			this.nilaiUAS = nilaiUAS;
			this.nilaiAkhir = nilaiAkhir;
		}

		public MataKuliah getMataKuliah() {
			return mataKuliah;
		}

		public Character getKelas() {
			return kelas;
		}

		public Double getNilaiART() {
			return nilaiART;
		}

		public Double getNilaiUTS() {
			return nilaiUTS;
		}

		public Double getNilaiUAS() {
			return nilaiUAS;
		}

		/**
		 * Mengembalikan nilai akhir dalam bentuk huruf (A, B, C, D, ...)
		 * @return nilai akhir dalam huruf, atau null jika tidak ada.
		 */
		public Character getNilaiAkhir() {
			return nilaiAkhir;
		}
		
		/**
		 * Mendapatkan nilai akhir dalam bentuk angka
		 * @return nilai akhir dalam angka, atau null jika {@link #getNilaiAkhir() mengembalikan null}
		 */
		public Double getAngkaAkhir() {
			if (nilaiAkhir == null) {
				return null;
			}
			switch (nilaiAkhir) {
			case 'A':
				return 4.0;
			case 'B':
				return 3.0;
			case 'C':
				return 2.0;
			case 'D':
				return 1.0;
			case 'E':
				return 0.0;
			}
			return null;
		}

		public int getTahunAjaran() {
			return tahunAjaran;
		}

		public int getSemester() {
			return semester;
		}

		@Override
		public String toString() {
			return "Nilai [tahunAjaran=" + tahunAjaran + ", semester="
					+ semester + ", mataKuliah=" + mataKuliah + ", kelas="
					+ kelas + ", nilaiART=" + nilaiART + ", nilaiUTS="
					+ nilaiUTS + ", nilaiUAS=" + nilaiUAS + ", nilaiAkhir="
					+ nilaiAkhir + "]";
		}

		/**
		 * Pembanding antara satu nilai dengan nilai lainnya, secara
		 * kronologis waktu pengambilan.
		 * @author pascal
		 *
		 */
		public static class ChronologicalComparator implements Comparator<Nilai> {

			@Override
			public int compare(Nilai o1, Nilai o2) {
				if (o1.tahunAjaran < o2.tahunAjaran) {
					return -1;
				}
				if (o1.tahunAjaran > o2.tahunAjaran) {
					return + 1;
				}
				if (o1.semester < o2.semester) {
					return -1;
				}
				if (o1.semester > o2.semester) {
					return +1;
				}
				return 0;
			}
		}
	}
}
