package controllers;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.time.LocalDate;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import models.display.JadwalDisplay;
import models.display.PrasyaratDisplay;
import models.display.RingkasanDisplay;
import id.ac.unpar.siamodels.JadwalKuliah;
import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.Mahasiswa.Nilai;
import id.ac.unpar.siamodels.MataKuliahFactory;
import id.ac.unpar.siamodels.Semester;
import id.ac.unpar.siamodels.TahunSemester;
import id.ac.unpar.siamodels.matakuliah.interfaces.HasPrasyarat;
import id.ac.unpar.siamodels.prodi.teknikinformatika.*;
import models.support.Scraper;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import play.Logger;

public class Application extends Controller {

	public Result index() throws IOException {
		if (session("npm") == null) {
			return ok(views.html.login.render(""));
		} else {
			return home();
		}
	}

	public Result login() throws IOException {
		if (session("npm") == null) {
			return index();
		} else {
			return home();
		}
	}

	public Result submitLogin() throws IOException {
		Scraper scrap = new Scraper();
		String errorHtml = "<div class='alert alert-danger' role='alert'>"
				+ "<span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span>"
				+ "<span class='sr-only'>Error:</span>";
		DynamicForm dynamicForm = Form.form().bindFromRequest();
		String email = dynamicForm.get("email");
		String pass = dynamicForm.get("pass");
		if (!email.matches("[0-9]{7}+@student.unpar.ac.id")) {
			Logger.info(
					"User: " + email + " gagal login dari " + request().remoteAddress() + " karena salah input E-Mail");
			return ok(views.html.login.render(errorHtml + "Email tidak valid" + "</div>"));
		}
		if (!(email.charAt(0) == '7' && email.charAt(1) == '3')) {
			Logger.info("User: " + email + " gagal login dari " + request().remoteAddress()
					+ " karena bukan mahasiswa teknik informatika");
			return ok(views.html.login.render(errorHtml + " bukan mahasiswa teknik informatika" + "</div>"));
		}
		String npm = "20" + email.substring(2, 4) + email.substring(0, 2) + "0" + email.substring(4, 7);
		String phpsessid = scrap.login(npm, pass);
		if (phpsessid != null) {
			Logger.info("User " + email + " berhasil login dari " + request().remoteAddress());
			session("npm", npm);
			session("email", email);
			session("phpsessid", phpsessid);
			return home();
		} else {
			Logger.info("User: " + email + " gagal login dari " + request().remoteAddress()
					+ " karena input password salah atau bukan mahasiswa aktif");
			return ok(views.html.login
					.render(errorHtml + "Password yang anda masukkan salah atau bukan mahasiswa aktif" + "</div>"));
		}
	}

	public Result home() throws IOException {
		if (session("npm") == null || session("phpsessid") == null) {
			session().clear();
			return index();
		} else {
			Scraper scrap = new Scraper();
			Mahasiswa mhs = new Mahasiswa(session("npm"));
			scrap.requestNamePhotoTahunSemester(session("phpsessid"), mhs);
			Logger.info("User " + session("email") + " mengakses halaman home dari " + request().remoteAddress());
			return ok(views.html.home.render(mhs));
		}
	}

	public Result perwalian() throws IOException {

		if (session("npm") == null || session("phpsessid") == null) {
			session().clear();
			return index();
		} else {
			Mahasiswa mhs = new Mahasiswa(session("npm"));
			Scraper scrap = new Scraper();
			TahunSemester currTahunSemester = scrap.requestNamePhotoTahunSemester(session("phpsessid"), mhs);
			scrap.requestKuliah(session("phpsessid"));
			List<JadwalKuliah> jadwalList = scrap.requestJadwal(session("phpsessid"));
			mhs.setJadwalKuliahList(jadwalList);
			scrap.requestNilai(session("phpsessid"), mhs);
			Logger.info("User " + session("email") + " mengakses halaman prasyarat dari " + request().remoteAddress());
			if (mhs.getRiwayatNilai().size() == 0) {
				List<PrasyaratDisplay> table = null;
				String semester = currTahunSemester.getSemester() + " " + currTahunSemester.getTahun() + "/"
						+ (currTahunSemester.getTahun() + 1);
				return ok(views.html.prasyarat.render(table, semester));
			} else {
				List<PrasyaratDisplay> table = checkPrasyarat();
				String semester = currTahunSemester.getSemester() + " " + currTahunSemester.getTahun() + "/"
						+ (currTahunSemester.getTahun() + 1);
				return ok(views.html.prasyarat.render(table, semester));
			}
		}
	}

	public Result jadwalKuliah() throws IOException {

		if (session("npm") == null || session("phpsessid") == null) {
			session().clear();
			return index();
		} else {
			Scraper scrap = new Scraper();
			Mahasiswa mhs = new Mahasiswa(session("npm"));
			List<JadwalKuliah> jadwalList = scrap.requestJadwal(session("phpsessid"));
			mhs.setJadwalKuliahList(jadwalList);
			TahunSemester currTahunSemester = scrap.requestNamePhotoTahunSemester(session("phpsessid"), mhs);
			JadwalDisplay table = new JadwalDisplay(mhs.getJadwalKuliahList());
			String semester = currTahunSemester.getSemester() + " " + currTahunSemester.getTahun() + "/"
					+ (currTahunSemester.getTahun() + 1);
			Logger.info(
					"User " + session("email") + " mengakses halaman jadwal kuliah dari " + request().remoteAddress());
			return ok(views.html.jadwalKuliah.render(table, semester));
		}
	}

	public Result kelulusan() throws IOException {
		if (session("npm") == null || session("phpsessid") == null) {
			session().clear();
			return index();
		} else {
			Scraper scrap = new Scraper();
			Mahasiswa mhs = new Mahasiswa(session("npm"));
			scrap.requestNilai(session("phpsessid"), mhs);
			scrap.requestNilaiTOEFL(session("phpsessid"), mhs);
			Logger.info(
					"User " + session("email") + " mengakses halaman Data akademik dari " + request().remoteAddress());
			if (mhs.getRiwayatNilai().size() == 0) {
				RingkasanDisplay display = null;
				return ok(views.html.ringkasan.render(display));
			} else {
				Mahasiswa currMahasiswa = mhs;
				RingkasanDisplay display = new RingkasanDisplay(String.format("%.2f", currMahasiswa.calculateIPS()),
						String.format("%.2f", currMahasiswa.calculateIPLulus()), currMahasiswa.calculateSKSLulus());
				Kelulusan str = new Kelulusan();
				ArrayList<String> arrString = new ArrayList<>();
				str.checkPrasyarat(currMahasiswa, arrString);
				display.setData(arrString);
				List<Nilai> riwayatNilai = currMahasiswa.getRiwayatNilai();
				int lastIndex = riwayatNilai.size() - 1;
				Semester semester = riwayatNilai.get(lastIndex).getSemester();
				int tahunAjaran = riwayatNilai.get(lastIndex).getTahunAjaran();
				int totalSKS = 0;
				for (int i = lastIndex; i >= 0; i--) {
					Nilai nilai = riwayatNilai.get(i);
					if (nilai.getSemester() == semester && nilai.getTahunAjaran() == tahunAjaran) {
						if (nilai.getAngkaAkhir() != null) {
							totalSKS += nilai.getMataKuliah().sks();
						}
					} else {
						break;
					}
				}
				String semTerakhir = semester + " " + tahunAjaran + "/" + (tahunAjaran + 1);
				display.setDataSemTerakhir(semTerakhir, totalSKS);
				String pilWajibLulus = new String();
				String pilWajibBelumLulus = new String();
				for (int i = 0; i < display.getPilWajib().length; i++) {
					if (mhs.hasLulusKuliah(display.getPilWajib()[i])) {
						pilWajibLulus += display.getPilWajib()[i] + ";";
					} else {
						pilWajibBelumLulus += display.getPilWajib()[i] + ";";
					}
				}
				if (!pilWajibLulus.isEmpty()) {
					display.setPilWajibLulus(pilWajibLulus.split(";"));
				} else {
					display.setPilWajibLulus(new String[] {});
				}
				if (!pilWajibBelumLulus.isEmpty()) {
					display.setPilWajibBelumLulus(pilWajibBelumLulus.split(";"));
				} else {
					display.setPilWajibBelumLulus(new String[] {});
				}
				return ok(views.html.ringkasan.render(display));
			}
		}
	}

	public Result tentang() throws IOException {
		if (session("npm") == null || session("phpsessid") == null) {
			session().clear();
			return index();
		} else {
			Logger.info("User " + session("email") + " mengakses halaman info dari " + request().remoteAddress());
			return ok(views.html.tentang.render());
		}
	}

	public Result logout() throws IOException {
		Logger.info("User " + session("email") + " telah logout dari " + request().remoteAddress());
		session().clear();
		return index();
	}

	private List<PrasyaratDisplay> checkPrasyarat() throws IOException {
		Scraper scrap = new Scraper();
		Mahasiswa mhs = new Mahasiswa(session("npm"));
		scrap.requestNilai(session("phpsessid"), mhs);
		List<PrasyaratDisplay> table = new ArrayList<PrasyaratDisplay>();
		List<MataKuliah> mkList = scrap.requestKuliah(session("phpsessid"));
		String MATAKULIAH_REPOSITORY_PACKAGE = "id.ac.unpar.siamodels.matakuliah";
		List<Object> mkKnown = new ArrayList<Object>();
		List<MataKuliah> mkUnknown = new ArrayList<MataKuliah>();
		for (MataKuliah mk : mkList) {
			try {
				Class<?> mkClass = Class.forName(MATAKULIAH_REPOSITORY_PACKAGE + "." + mk.kode());
				Object matakuliah = mkClass.newInstance();
				mkKnown.add(matakuliah);
			} catch (ClassNotFoundException e) {
				mkUnknown.add(mk);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for (Object mk : mkKnown) {
			if (mk instanceof HasPrasyarat) {
				List<String> reasons = new ArrayList<String>();
				((HasPrasyarat) mk).checkPrasyarat(mhs, reasons);
				if (!reasons.isEmpty()) {
					String status = new String();
					for (String reason : reasons) {
						status += reason + ";";
					}
					table.add(
							new PrasyaratDisplay(
									MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),
											MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA),
									status.split(";")));
				} else {
					if (mhs.hasLulusKuliah(mk.getClass().getSimpleName())) {
						table.add(new PrasyaratDisplay(
								MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),
										MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA),
								new String[] { "sudah lulus" }));
					} else {
						table.add(new PrasyaratDisplay(
								MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),
										MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA),
								new String[] { "memenuhi syarat" }));
					}
				}
			} else {
				if (mhs.hasLulusKuliah(mk.getClass().getSimpleName())) {
					table.add(new PrasyaratDisplay(
							MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),
									MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA),
							new String[] { "sudah lulus" }));
				} else {
					table.add(new PrasyaratDisplay(
							MataKuliahFactory.getInstance().createMataKuliah(mk.getClass().getSimpleName(),
									MataKuliahFactory.UNKNOWN_SKS, MataKuliahFactory.UNKNOWN_NAMA),
							new String[] { "tidak memiliki prasyarat" }));
				}
			}
		}
		for (MataKuliah mk : mkUnknown) {
			table.add(new PrasyaratDisplay(mk, new String[] { "data prasyarat tidak tersedia" }));
		}
		return table;
	}
}
