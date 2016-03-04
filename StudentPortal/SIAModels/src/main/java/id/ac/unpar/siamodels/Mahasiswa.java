package id.ac.unpar.siamodels;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Mahasiswa {
	protected final String npm;
	protected String nama;
	protected final List<Nilai> riwayatNilai;
	protected URL photoURL;
	protected List<JadwalKuliah> jadwalKuliahList;
	
	public Mahasiswa(String npm) throws NumberFormatException {
		super();
		if (!npm.matches("[0-9]{10}")) {
			throw new NumberFormatException("NPM tidak valid: " + npm);
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
	
	public URL getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(URL photoURL) {
		this.photoURL = photoURL;
	}
	
	public List<JadwalKuliah> getJadwalKuliahList() {
		return jadwalKuliahList;
	}

	public void setJadwalKuliahList(List<JadwalKuliah> jadwalKuliahList) {
		this.jadwalKuliahList = jadwalKuliahList;
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
	 * @return IPK Lulus, atau {#link {@link Double#NaN}} jika belum mengambil satu kuliah pun.
	 */
	public double calculateIPKLulus() throws ArrayIndexOutOfBoundsException {
		return calculateIPKTempuh(true);
	}

	/**
	 * Menghitung IPK mahasiswa sampai saat ini, dengan aturan:
	 * <ul>
	 *   <li>Perhitungan kuliah yang tidak lulus ditentukan parameter
	 *   <li>Jika pengambilan beberapa kali, diambil <em>nilai terbaik</em>.
	 * </ul>
	 * @param lulusSaja set true jika ingin membuang mata kuliah tidak lulus 
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * @return IPK Lulus, atau {#link {@link Double#NaN}} jika belum mengambil satu kuliah pun.
	 */
	public double calculateIPKTempuh(boolean lulusSaja) throws ArrayIndexOutOfBoundsException {
		List<Nilai> riwayatNilai = getRiwayatNilai();
		if (riwayatNilai.size() == 0) {
			return Double.NaN;
		}		
		Map<String, Double> nilaiTerbaik = new HashMap<String, Double>();
		int totalSKS = 0;
		// Cari nilai lulus terbaik setiap kuliah
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getNilaiAkhir() == null) {
				continue;
			}
			if (lulusSaja && nilai.getNilaiAkhir().equals('E')) {
				continue;
			}
			String kodeMK = nilai.getMataKuliah().kode();
			Double angkaAkhir = nilai.getAngkaAkhir();
			int sks = nilai.getMataKuliah().sks();
			if (!nilaiTerbaik.containsKey(kodeMK)) {
				totalSKS += sks;
				nilaiTerbaik.put(kodeMK, sks * angkaAkhir);
			} else if (sks * angkaAkhir > nilaiTerbaik.get(kodeMK)) {
				nilaiTerbaik.put(kodeMK, sks * angkaAkhir);
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
		TahunSemester tahunSemester = riwayatNilai.get(lastIndex).getTahunSemester();
		double totalNilai = 0;
		double totalSKS = 0;
		for (int i = lastIndex; i >= 0; i--) {
			Nilai nilai = riwayatNilai.get(i);
			if (nilai.tahunSemester.equals(tahunSemester)) {
				if (nilai.getAngkaAkhir() != null) {
					totalNilai += nilai.getMataKuliah().sks() * nilai.getAngkaAkhir();
					totalSKS += nilai.getMataKuliah().sks();
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
	 */
	public int calculateSKSLulus() throws ArrayIndexOutOfBoundsException {
		return calculateSKSTempuh(true);
	}
	
	/**
	 * Menghitung jumlah SKS tempuh mahasiswa saat ini.
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * @param lulusSaja set true jika ingin membuang SKS tidak lulus
	 * @return SKS tempuh
	 */
	public int calculateSKSTempuh(boolean lulusSaja) throws ArrayIndexOutOfBoundsException {
		List<Nilai> riwayatNilai = getRiwayatNilai();
		Set<String> sksTerhitung = new HashSet<String>();
		int totalSKS = 0;
		// Tambahkan SKS setiap kuliah
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getNilaiAkhir() == null) {
				continue;
			}
			if (lulusSaja && nilai.getNilaiAkhir().equals('E')) {
				continue;
			}
			String kodeMK = nilai.getMataKuliah().kode();
			if (!sksTerhitung.contains(kodeMK)) {
				totalSKS += nilai.getMataKuliah().sks();
				sksTerhitung.add(kodeMK);
			}			
		}
		return totalSKS;
	}
	
	/**
	 * Mendapatkan seluruh tahun semester di mana mahasiswa ini tercatat
	 * sebagai mahasiswa aktif, dengan strategi memeriksa riwayat nilainya.
	 * Jika ada satu nilai saja pada sebuah tahun semester, maka dianggap
	 * aktif pada semester tersebut.
	 * @return kumpulan tahun semester di mana mahasiswa ini aktif
	 */
	public Set<TahunSemester> calculateTahunSemesterAktif() {
		Set<TahunSemester> tahunSemesterAktif = new TreeSet<TahunSemester>();
		List<Nilai> riwayatNilai = getRiwayatNilai();
		for (Nilai nilai: riwayatNilai) {
			tahunSemesterAktif.add(nilai.getTahunSemester());
		}
		return tahunSemesterAktif;
	}
	
	/**
	 * Memeriksa apakah mahasiswa ini sudah lulus mata kuliah tertentu. Kompleksitas O(n).
	 * Sebelum memanggil method ini, {@link #getRiwayatNilai()} harus sudah mengandung nilai per mata kuliah!
	 * Note: jika yang dimiliki adalah {@link MataKuliah}, gunakanlah {@link MataKuliah#kode()}.
	 * @param kodeMataKuliah kode mata kuliah yang ingin diperiksa kelulusannya.
	 * @return true jika sudah pernah mengambil dan lulus, false jika belum
	 */
	public boolean hasLulusKuliah(String kodeMataKuliah) {
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getMataKuliah().kode().equals(kodeMataKuliah)) {
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
	 * Note: jika yang dimiliki adalah {@link MataKuliah}, gunakanlah {@link MataKuliah#kode()}.
	 * @param kodeMataKuliah kode mata kuliah yang ingin diperiksa.
	 * @return true jika sudah pernah mengambil, false jika belum
	 */
	public boolean hasTempuhKuliah(String kodeMataKuliah) {
		for (Nilai nilai: riwayatNilai) {
			if (nilai.getMataKuliah().kode().equals(kodeMataKuliah)) {
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
		/** Tahun dan Semester kuliah ini diambil */
		protected final TahunSemester tahunSemester;
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

		public Nilai(TahunSemester tahunSemester, MataKuliah mataKuliah,
				Character kelas, Double nilaiART, Double nilaiUTS, Double nilaiUAS,
				Character nilaiAkhir) {
			super();
			this.tahunSemester = tahunSemester;
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
		 * Mengembalikan nilai akhir dalam bentuk huruf (A, B, C, D, ..., atau K)
		 * @return nilai akhir dalam huruf, atau null jika tidak ada.
		 */
		public Character getNilaiAkhir() {
			return nilaiAkhir;
		}
		
		/**
		 * Mendapatkan nilai akhir dalam bentuk angka
		 * @return nilai akhir dalam angka, atau null jika {@link #getNilaiAkhir() mengembalikan 'K' atau null}
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
			case 'K':
				return null;
			}
			return null;
		}

		public TahunSemester getTahunSemester() {
			return tahunSemester;
		}

		public int getTahunAjaran() {
			return tahunSemester.getTahun();
		}
		
		public Semester getSemester() {
			return tahunSemester.getSemester();
		}

		@Override
		public String toString() {
			return "Nilai [tahunSemester=" + tahunSemester + ", mataKuliah=" + mataKuliah + ", kelas=" + kelas
					+ ", nilaiART=" + nilaiART + ", nilaiUTS=" + nilaiUTS + ", nilaiUAS=" + nilaiUAS + ", nilaiAkhir="
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
				return o1.getTahunSemester().compareTo(o2.getTahunSemester());
			}
		}
	}
}
