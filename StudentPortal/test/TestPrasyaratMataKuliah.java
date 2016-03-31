
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;

import static play.test.Helpers.running;

import java.io.IOException;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes permasalahan prasyarat matakuliah, Jika pengguna belum
 * memiliki riwayat nilai(masih menempuh semester 1), akan ditampilkan pesan
 * “PRASYARAT BELUM TERSEDIA”
 * 
 * @author FTIS\i13054
 *
 */
public class TestPrasyaratMataKuliah extends FunctionalTest {

	public TestPrasyaratMataKuliah() throws IOException {
		super();
	}

	/**
	 * Jika pengguna menuju navigasi drawer dan melalukan click terhadap
	 * prasyarat matakuliah akan ditampilkan pesan “PRASYARAT BELUM TERSEDIA”
	 * karena baru semester 1.
	 */
	@Test
	public void testUserAndPassSmt1() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo("/");
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailSmt1());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassSmt1());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo("/prasyarat");

				FluentList<FluentWebElement> e1 = browser.find(".row");
				if (e1.size() > 1) {
					FluentList<FluentWebElement> e2 = e1.get(1).find("h5");
					if (e2.size() > 0) {
						assertEquals("PRASYARAT BELUM TERSEDIA", e2.get(0).getText());
					} else {
						assertEquals("PRASYARAT BELUM TERSEDIA", "Test Gagal");
					}
				} else {
					assertEquals("PRASYARAT BELUM TERSEDIA", "Test Gagal");
				}
			}
		});
	}

	/**
	 * Jika pengguna menuju navigasi drawer dan melalukan click terhadap
	 * prasyarat matakuliah akan ditampilkan halaman prasyarat matakuliah
	 * beserta tabel prasyarat mata kuliah beserta status pengambilannya
	 */
	@Test
	public void testPrasyaratMataKuliah() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo("/");
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo("/prasyarat");

				assertEquals("PEMERIKSAAN PRASYARAT MATA KULIAH",
						browser.find(".row").get(0).find("h2").get(0).getText());
				assertEquals("Keterangan", browser.find(".table-bordered").get(0).find("th").get(2).getText());
			}
		});
	}
}
