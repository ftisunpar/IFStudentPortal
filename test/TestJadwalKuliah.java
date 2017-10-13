import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.running;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes permasalahan jadwal kuliah, jika jadwal kuliah pengguna
 * sudah tersedia akan ditampilkan jadwal kuliah dalam bentuk kalendar yang
 * sudah diurutkan berdasarkan hari
 * 
 * @author FTIS\i13006
 *
 */
public class TestJadwalKuliah extends FunctionalTest {

	public TestJadwalKuliah() throws IOException {
		super();
	}

	/**
	 * Jika pengguna menuju navigasi drawer dan melalukan click terhadap jadwal
	 * kuliah akan ditampilkan halaman jadwal kuliah dalam bentuk kalendar yang
	 * sudah diurutkan berdasarkan hari
	 */
	@Test
	public void testJadwalKuliahValid() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_JADWAL_KULIAH);
				FluentList<FluentWebElement> e1 = browser.find(".jadwal-cell");
				String cek=browser.find(".row").get(0).find("h2").get(0).getText();
				Matcher matcher = Pattern.compile(".*jadwal.+kuliah.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue(condition);
				if (!(e1.size() > 0)) {
					assertTrue("Mahasiswa terdaftar prs tetapi tidak memiliki jadwal kuliah",false);
				}
			}
		});
	}

	/**
	 * Jika pengguna menuju navigasi drawer dan melalukan click terhadap jadwal
	 * kuliah namun belum FRS, cuti studi, atau jadwal kuliah pengguna belum
	 * tersedia, Maka ditampilkan "JADWAL KULIAH BELUM TERSEDIA".
	 */
	@Test
	public void testJadwalKuliahBelumFRS() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_JADWAL_KULIAH);
				FluentList<FluentWebElement> e1 = browser.find(".row").get(1).find("h5");	
				if (e1.size() > 0) {
					String cek=e1.get(0).getText();
					assertEquals("JADWAL KULIAH BELUM TERSEDIA", e1.get(0).getText());
					Matcher matcher = Pattern.compile(".*jadwal.+belum.+tersedia.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
					boolean condition = matcher.matches();
					assertTrue(condition);
				} else {
					assertTrue("Mahasiswa memiliki jadwal kuliah",false);
				}
			}
		});
	}
}
