package models.support;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import id.ac.unpar.siamodels.Dosen;
import id.ac.unpar.siamodels.JadwalKuliah;
import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.MataKuliahFactory;
import id.ac.unpar.siamodels.Semester;
import id.ac.unpar.siamodels.TahunSemester;

public class Scraper {
	private final String BASE_URL = "https://studentportal.unpar.ac.id/";
	private final String LOGIN_URL = BASE_URL + "home/index.login.submit.php";
	private final String CAS_URL = "https://cas.unpar.ac.id/login";
	private final String ALLJADWAL_URL = BASE_URL + "includes/jadwal.all.php";
	private final String JADWAL_URL = BASE_URL + "includes/jadwal.aktif.php";
	private final String NILAI_URL = BASE_URL + "includes/nilai.sem.php";
	private final String TOEFL_URL = BASE_URL + "includes/nilai.toefl.php";
	private final String LOGOUT_URL = BASE_URL + "home/index.logout.php";
	private final String HOME_URL = BASE_URL + "main.php";

	public void init() throws IOException {
		Connection baseConn = Jsoup.connect(BASE_URL);
		baseConn.timeout(0);
		baseConn.validateTLSCertificates(false);
		baseConn.method(Connection.Method.GET);
		baseConn.execute();
	}

	public String login(String npm, String pass) throws IOException {
		init();
		Mahasiswa logged_mhs = new Mahasiswa(npm);
		String user = logged_mhs.getEmailAddress();
		Connection conn = Jsoup.connect(LOGIN_URL);
		conn.data("Submit", "Login");
		conn.timeout(0);
		conn.validateTLSCertificates(false);
		conn.method(Connection.Method.POST);
		Response resp = conn.execute();
		Document doc = resp.parse();
		String lt = doc.select("input[name=lt]").val();
		String execution = doc.select("input[name=execution]").val();
		/* CAS LOGIN */
		Connection loginConn = Jsoup.connect(CAS_URL);
		loginConn.cookies(resp.cookies());
		loginConn.data("username", user);
		loginConn.data("password", pass);
		loginConn.data("lt", lt);
		loginConn.data("execution", execution);
		loginConn.data("_eventId", "submit");
		loginConn.timeout(0);
		loginConn.validateTLSCertificates(false);
		loginConn.method(Connection.Method.GET);
		resp = loginConn.execute();
		if (resp.body().contains("Data Akademik")) {
			Map<String, String> phpsessid = resp.cookies();
			doc = resp.parse();
			return phpsessid.get("PHPSESSID");
		} else {
			return null;
		}
	}

	public TahunSemester requestNamePhotoTahunSemester(String phpsessid, Mahasiswa mhs) throws IOException {
		Connection connection = Jsoup.connect(HOME_URL);
		connection.cookie("PHPSESSID", phpsessid);
		connection.timeout(0);
		connection.validateTLSCertificates(false);
		connection.method(Connection.Method.GET);
		Response resp = connection.execute();
		Document doc = resp.parse();
		String nama = doc.select("p[class=student-name]").text();
		mhs.setNama(nama);
		Element photo = doc.select(".student-photo img").first();
		String photoPath = photo.absUrl("src");
		mhs.setPhotoURL(new URL(photoPath));
		String curr_sem = doc.select(".main-info-semester a").text();
		String[] sem_set = parseSemester(curr_sem);
		TahunSemester currTahunSemester = new TahunSemester(Integer.parseInt(sem_set[0]),
				Semester.fromString(sem_set[1]));
		return currTahunSemester;
	}

	public List<MataKuliah> requestKuliah(String phpsessid) throws IOException {
		Connection connection = Jsoup.connect(ALLJADWAL_URL);
		connection.cookie("PHPSESSID", phpsessid);
		connection.timeout(0);
		connection.validateTLSCertificates(false);
		connection.method(Connection.Method.GET);
		Response resp = connection.execute();
		Document doc = resp.parse();
		Elements jadwal = doc.select("tr");
		String prev = "";
		List<MataKuliah> mkList;
		mkList = new ArrayList<MataKuliah>();
		for (int i = 1; i < jadwal.size() - 1; i++) {
			Elements row = jadwal.get(i).children();
			if (!row.get(1).text().equals("")) {
				String kode = row.get(1).text();
				String nama = row.get(2).text();
				String sks = row.get(3).text();
				if (!kode.equals(prev)) {
					MataKuliah curr = MataKuliahFactory.getInstance().createMataKuliah(kode, Integer.parseInt(sks),
							nama);
					mkList.add(curr);
				}
				prev = kode;
			}
		}
		return mkList;
	}

	public List<JadwalKuliah> requestJadwal(String phpsessid) throws IOException {
		Connection connection = Jsoup.connect(JADWAL_URL);
		connection.cookie("PHPSESSID", phpsessid);
		connection.timeout(0);
		connection.validateTLSCertificates(false);
		connection.method(Connection.Method.GET);
		Response resp = connection.execute();
		Document doc = resp.parse();
		Elements jadwalTable = doc.select(".portal-full-table");
		List<JadwalKuliah> jadwalList = new ArrayList<JadwalKuliah>();

		/* Kuliah */
		if (jadwalTable.size() > 0) {
			Elements tableKuliah = jadwalTable.get(0).select("tbody tr");
			String kode = new String();
			String nama = new String();
			for (Element elem : tableKuliah) {
				if (elem.className().contains("row")) {
					if (!(elem.child(1).text().isEmpty() && elem.child(2).text().isEmpty())) {
						kode = elem.child(1).text();
						nama = elem.child(2).text();
					}
					MataKuliah currMk = MataKuliahFactory.getInstance().createMataKuliah(kode,
							Integer.parseInt(elem.child(3).text()), nama);
					try {
						String kelasString = elem.child(4).text();
						String hariString = elem.child(7).text();
						String waktuString = elem.child(8).text();
						if (hariString != null & hariString.length() != 0
								&& waktuString != null & waktuString.length() != 0) {
							jadwalList.add(
									new JadwalKuliah(currMk, kelasString.length() == 0 ? null : kelasString.charAt(0),
											new Dosen(null, elem.child(5).text()), hariString, waktuString,
											elem.child(9).text()));
						}
					} catch (IndexOutOfBoundsException e) {
						// void. do not add jadwal.
					}
				}
			}
		}
		return jadwalList;
	}

	public void requestNilai(String phpsessid, Mahasiswa logged_mhs) throws IOException {
		Connection connection = Jsoup.connect(NILAI_URL);
		connection.cookie("PHPSESSID", phpsessid);
		connection.data("npm", logged_mhs.getNpm());
		connection.data("thn_akd", "ALL");
		connection.timeout(0);
		connection.validateTLSCertificates(false);
		connection.method(Connection.Method.POST);
		Response resp = connection.execute();
		Document doc = resp.parse();
		Elements mk = doc.select("table");

		for (Element tb : mk) {
			Elements tr = tb.select("tr");
			String[] sem_set = this.parseSemester(tr.get(0).text());
			String thn = sem_set[0];
			String sem = sem_set[1];

			for (Element td : tr) {
				if (td.className().contains("row")) {
					String kode = td.child(1).text();
					int sks = Integer.parseInt(td.child(3).text());
					String nama_mk = td.child(2).text();
					MataKuliah curr_mk = MataKuliahFactory.getInstance().createMataKuliah(kode, sks, nama_mk);
					Character kelas, NA;
					Double ART, UTS, UAS;
					try {
						kelas = td.child(4).text().charAt(0);
					} catch (IndexOutOfBoundsException e) {
						kelas = null;
					}
					try {
						ART = Double.valueOf(td.child(5).text());
					} catch (NumberFormatException e) {
						ART = null;
					}
					try {
						UTS = Double.valueOf(td.child(6).text());
					} catch (NumberFormatException e) {
						UTS = null;
					}
					try {
						UAS = Double.valueOf(td.child(7).text());
					} catch (NumberFormatException e) {
						UAS = null;
					}
					try {
						NA = td.child(9).text().charAt(0);
					} catch (IndexOutOfBoundsException e) {
						NA = null;
					}

					if (NA != null) {
						TahunSemester tahunSemesterNilai = new TahunSemester(Integer.parseInt(thn),
								Semester.fromString(sem));
						logged_mhs.getRiwayatNilai()
								.add(new Nilai(tahunSemesterNilai, curr_mk, kelas, ART, UTS, UAS, NA));
					}
				}
			}
		}
	}

	public void requestNilaiTOEFL(String phpsessid, Mahasiswa mahasiswa) throws IOException {
		SortedMap<LocalDate, Integer> nilaiTerakhirTOEFL = new TreeMap<>();
		Connection connection = Jsoup.connect(TOEFL_URL);
		connection.cookie("PHPSESSID", phpsessid);
		connection.data("npm", mahasiswa.getNpm());
		connection.data("thn_akd", "ALL");
		connection.timeout(0);
		connection.validateTLSCertificates(false);
		connection.method(Connection.Method.POST);
		Response resp = connection.execute();
		Document doc = resp.parse();
		Elements nilaiTOEFL = doc.select("table").select("tbody").select("tr");
		if (!nilaiTOEFL.isEmpty()) {
			for (int i = 0; i < nilaiTOEFL.size(); i++) {
				Element nilai = nilaiTOEFL.get(i).select("td").get(1);
				Element tgl_toefl = nilaiTOEFL.get(i).select("td").get(2);
				String[] tanggal = tgl_toefl.text().split(" ");
				switch (tanggal[1].toLowerCase()) {
				case "januari":
					tanggal[1] = "1";
					break;
				case "februari":
					tanggal[1] = "2";
					break;
				case "maret":
					tanggal[1] = "3";
					break;
				case "april":
					tanggal[1] = "4";
					break;
				case "mei":
					tanggal[1] = "5";
					break;
				case "juni":
					tanggal[1] = "6";
					break;
				case "juli":
					tanggal[1] = "7";
					break;
				case "agustus":
					tanggal[1] = "8";
					break;
				case "september":
					tanggal[1] = "9";
					break;
				case "oktober":
					tanggal[1] = "10";
					break;
				case "november":
					tanggal[1] = "11";
					break;
				case "desember":
					tanggal[1] = "12";
					break;
				}

				LocalDate localDate = LocalDate.of(Integer.parseInt(tanggal[2]), Integer.parseInt(tanggal[1]),
						Integer.parseInt(tanggal[0]));

				nilaiTerakhirTOEFL.put(localDate, Integer.parseInt(nilai.text()));
			}
		}
		mahasiswa.setNilaiTOEFL(nilaiTerakhirTOEFL);
	}

	public void logout() throws IOException {
		Connection logoutConn = Jsoup.connect(LOGOUT_URL);
		logoutConn.timeout(0);
		logoutConn.validateTLSCertificates(false);
		logoutConn.method(Connection.Method.GET);
		logoutConn.execute();
	}

	public String[] parseSemester(String sem_raw) {
		String[] sem_set = sem_raw.split("/")[0].split("-");
		return new String[] { sem_set[1].trim(), sem_set[0].trim() };
	}
}
