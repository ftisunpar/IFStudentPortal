import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.running;

import java.io.IOException;

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
				browser.goTo("/");
				assertEquals("Login", browser.find(".form-control", withName("submit")).get(0).getText());
			}
		});
	}
}
