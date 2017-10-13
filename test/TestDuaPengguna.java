import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import play.test.TestBrowser;
import static play.test.Helpers.running;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * Kelas untuk melakukan pengujian dua pengguna menjalankan aplikasi secara
 * bersamaan.
 * 
 * @author FTIS\i13013
 *
 */
public class TestDuaPengguna extends FunctionalTest {

	public TestDuaPengguna() throws IOException {
		super();
	}

	private WebDriver driver2 = new FirefoxDriver();
	private TestBrowser browser2 = new TestBrowser(driver2, baseURL);

	@After
	public void tearDown() {
		browser.quit();
		this.browser2.quit();
	}

	@Test
	public void testDuaPengguna() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser.find(".form-control", withName("submit")).get(0).click();
				browser.goTo(FunctionalTest.URL_PERSIAPAN_PERWALIAN);
				String cek=browser.find(".row").get(0).find("h2").get(0).getText();
				Matcher matcher = Pattern.compile(".*persiapan perwalian.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue("Tidak dapat masuk persiapan perwalian",condition);
				browser2.goTo(FunctionalTest.URL_HOME);
				browser2.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
				browser2.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
				browser2.find(".form-control", withName("submit")).get(0).click();
				browser2.goTo(FunctionalTest.URL_JADWAL_KULIAH);
				FluentList<FluentWebElement> e1 = browser2.find(".jadwal-cell");
				cek=browser2.find(".row").get(0).find("h2").get(0).getText();
				matcher = Pattern.compile(".*jadwal.+kuliah.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				condition = matcher.matches();
				assertTrue("Tidak dapat masuk ke jadwal kuliah",condition);
			}
		});
	}
}
