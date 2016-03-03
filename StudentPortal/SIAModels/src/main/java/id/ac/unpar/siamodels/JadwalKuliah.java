package id.ac.unpar.siamodels;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class JadwalKuliah {
	protected MataKuliah mataKuliah;
	protected Character kelas;
	protected DayOfWeek hari;
	protected LocalTime waktuMulai;
	protected LocalTime waktuSelesai;
	protected String lokasi;
	protected Dosen pengajar;

	/**
	 * Membuat jadwal kuliah baru
	 * @param mataKuliah mata kuliah yang dibuat jadwalnya
	 * @param kelas kelas kuliah atau null jika tidak tersedia
	 * @param pengajar nama pengajar
	 * @param hariString hari dalam Bahasa Indonesia (Senin, Selasa, ...)
	 * @param waktuString rentang waktu kuliah (08.00-09.00 atau 08:00-09:00)
	 * @param lokasi kode ruangan
	 */
	public JadwalKuliah(MataKuliah mataKuliah, Character kelas, Dosen pengajar, String hariString, String waktuString,
			String lokasi) {
		this.mataKuliah = mataKuliah;
		this.kelas = kelas;
		this.waktuMulai = LocalTime.parse(waktuString.substring(0, 5).replace('.', ':'));
		this.waktuSelesai = LocalTime.parse(waktuString.substring(6, 11).replace('.', ':'));
		this.lokasi = lokasi;
		this.pengajar = pengajar;
		this.hari = indonesianToDayOfWeek(hariString);
	}

	public JadwalKuliah() {
		// void
	}
	
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}

	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}

	public Character getKelas() {
		return kelas;
	}

	public void setKelas(Character kelas) {
		this.kelas = kelas;
	}

	public DayOfWeek getHari() {
		return hari;
	}

	public void setHari(DayOfWeek hari) {
		this.hari = hari;
	}

	public LocalTime getWaktuMulai() {
		return waktuMulai;
	}

	public void setWaktuMulai(LocalTime waktuMulai) {
		this.waktuMulai = waktuMulai;
	}

	public LocalTime getWaktuSelesai() {
		return waktuSelesai;
	}

	public void setWaktuSelesai(LocalTime waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public Dosen getPengajar() {
		return pengajar;
	}

	public void setPengajar(Dosen pengajar) {
		this.pengajar = pengajar;
	}

	public String getWaktuString() {
		return waktuMulai + "-" + waktuSelesai;
	}

	/**
	 * Converts Indonesian day names to {@link DayOfWeek} enumeration.
	 * @param indonesian the day name in Indonesian
	 * @return {@link DayOfWeek} object or null if not found.
	 */
	public static DayOfWeek indonesianToDayOfWeek(String indonesian) {
		switch (indonesian.toLowerCase()) {
		case "senin":
			return DayOfWeek.MONDAY;
		case "selasa":
			return DayOfWeek.TUESDAY;
		case "rabu":
			return DayOfWeek.WEDNESDAY;
		case "kamis":
			return DayOfWeek.THURSDAY;
		case "jumat":
			return DayOfWeek.FRIDAY;
		case "sabtu":
			return DayOfWeek.SATURDAY;
		case "minggu":
			return DayOfWeek.SUNDAY;
		default:
			return null;
		}
	}
}
