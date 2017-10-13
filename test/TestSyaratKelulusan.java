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
 * Kelas untuk melakukan pengujian data akademik mahasiswa. Jika pengujian
 * berhasil jika menampilkan IPS semester terakhir, IPK, SKS lulus, sisa SKS
 * kelulusan, dan ringkasan data mengenai mata kuliah pilihan wajib
 * 
 * @author FTIS\i13013
 *
 */
public class TestSyaratKelulusan extends FunctionalTest {

	public TestSyaratKelulusan() throws IOException {
		super();
	}

//	/**
//	 * Jika pengguna sudah memiliki riwayat nilai, akan ditampilkan ringkasan
//	 * syarat kelulusan mahasiswa berupa IPS semester terakhir, IPK, SKS lulus,
//	 * sisa SKS kelulusan, dan ringkasan
//	 */
	@Test
	public void testSyaratKelulusan() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_SYARAT_KELULUSAN);

				FluentList<FluentWebElement> e1 = browser.find("h2", withClass("text-center"));
				String cek=e1.get(0).getText();
				Matcher matcher = Pattern.compile(".*syarat.+kelulusan.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue("Tidak terdapat judul Syarat Kelulusan",condition);

				e1 = browser.find("div", withClass("kelulusan-body"));
				cek=e1.get(0).find("p").getText();
				matcher = Pattern.compile(".*memenuhi.+lulus.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("Tidak terdapat kata-kata anda sudah atau belum memenuhi syarat lulus",condition);	
				FluentList<FluentWebElement> e2 = browser.find("li");
				if(cek.toLowerCase().contains("belum")){
					if(e2.size()<=0){
						assertTrue("Belum memenuhi syarat lulus, tetapi tidak ada mata kuliah belum lulus yang ditampilkan",false);
					}
				}else{
					if(e2.size()>0){
						assertTrue("Sudah memenuhi syarat lulus, tetapi ada mata kuliah belum lulus yang ditampilkan",false);
					}
				}
			}
		});
	}

	/**
	 * Jika pengguna belum memiliki riwayat nilai misalnya jika baru menempuh
	 * semester 1 akan ditampilkan "DATA AKADEMIK BELUM TERSEDIA"
	 */
	@Test
	public void testSyaratKelulusanNotAvailable() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_SYARAT_KELULUSAN);

				FluentList<FluentWebElement> e1 = browser.find("h2", withClass("text-center"));

				String cek=e1.get(0).getText();
				Matcher matcher = Pattern.compile(".*syarat.+kelulusan.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue("Tidak terdapat judul Syarat Kelulusan",condition);

				FluentList<FluentWebElement> e2 = browser.find("div", withClass("row"));
			
				cek=e2.get(1).find("h5").get(0).getText();
				matcher = Pattern.compile(".*akedemik.+belum.+tersedia.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("Data Akademik Tersedia",condition);
			}
		});
	}
}