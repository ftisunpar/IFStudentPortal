import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.running;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes saat pengguna menjalankan aplikasi.
 * 
 * @author FTIS\i13015
 *
 */
public class TestInitialRun extends FunctionalTest {

	public TestInitialRun() throws IOException {
		super();
	}

	/**
	 * Pengguna menjalankan aplikasi kemudian halaman login akan ditampilkan.
	 */
	@Test
	public void testBlankUserPass() {
		running(server, new Runnable() {
			@Override
			public void run() {
				browser.goTo(FunctionalTest.URL_HOME);
				String cek=browser.find(".form-control", withName("submit")).get(0).getText();
				Matcher matcher = Pattern.compile(".*login.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(cek);
				boolean condition = matcher.matches();
				assertTrue(condition);
				//assertEquals("Login", browser.find(".form-control", withName("submit")).get(0).getText());
			}
		});
	}
}
