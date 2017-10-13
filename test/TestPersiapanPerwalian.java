
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
 * Kelas untuk mengetes permasalahan prasyarat matakuliah, Jika pengguna belum
 * memiliki riwayat nilai(masih menempuh semester 1), akan ditampilkan pesan
 * “PRASYARAT BELUM TERSEDIA”
 * 
 * @author FTIS\i13054
 *
 */
public class TestPersiapanPerwalian extends FunctionalTest {

	public TestPersiapanPerwalian() throws IOException {
		super();
	}

	/**
	 * Jika pengguna menuju navigasi drawer dan melalukan click terhadap
	 * Persiapan Perwalian akan ditampilkan pesan “PRASYARAT BELUM TERSEDIA”
	 * karena baru semester 1.
	 */
	@Test
	public void testPersiapanPerwalianSmt1() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailSmt1());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassSmt1());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_PERSIAPAN_PERWALIAN);

				FluentList<FluentWebElement> e1 = browser.find(".row");
				if (e1.size() > 1) {
					FluentList<FluentWebElement> e2 = e1.get(1).find("h5");
					if (e2.size() > 0) {
						String cek=e2.get(0).getText();
						Matcher matcher = Pattern.compile(".*prasyarat.+belum.+tersedia.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
						boolean condition = matcher.matches();
						assertTrue("prasyarat belum tersedia",condition);
					} else {
						assertTrue("prasyarat belum tersedia",false);
					}
				} else {
					assertTrue("prasyarat belum tersedia",false);
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
	public void testPersiapanPerwalian() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_PERSIAPAN_PERWALIAN);
				String cek=browser.find(".row").get(0).find("h2").get(0).getText();
				Matcher matcher = Pattern.compile(".*persiapan.+perwalian.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue("tidak dapat masuk ke halaman persiapan perwalian",condition);
				
				cek=browser.find(".table-bordered").get(0).find("th").get(2).getText();
				matcher = Pattern.compile(".*keterangan.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("tidak ada tabel prasyarat mata kuliah",condition);
				
				String[] temp;
				temp = browser.find(".kelulusan-body").get(0).getText().split("\n");
				
				cek=temp[0];
				matcher = Pattern.compile(".*ips.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("IPS tidak ditemukan", condition);
				
				cek=temp[1];
				matcher = Pattern.compile(".*ip.+kumulatif.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("IP Kumulatif tidak ditemukan",condition);
				
				cek=temp[2];
				matcher = Pattern.compile(".*ip.+lulus.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("IP Lulus tidak ditemukan",condition);
				
				cek=temp[4];
				matcher = Pattern.compile(".*sks.+lulus.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("SKS Lulus tidak ditemukan",condition);

			}
		});
	}
}
