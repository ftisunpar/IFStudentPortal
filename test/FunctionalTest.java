import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import play.test.TestBrowser;
import play.test.TestServer;
import play.test.WithBrowser;

import static play.test.Helpers.testServer;

import java.io.IOException;

/**
 * Super class untuk kelas testing Terdapat attribute WebDriver (untuk pengujian
 * ini digunakan FireFox), TestBrowser, Port, serta Url yang digunakan untuk
 * pengujian
 * 
 * @author FTIS\i13015
 */
public class FunctionalTest extends WithBrowser {
	// basic info
	protected TestServer server;
	protected WebDriver driver;
	protected TestBrowser browser;

	protected static int PORT = 9000;
	protected String baseURL = String.format("http://localhost:%d", PORT);

	protected FileConfReader objFileConfReader ;
	
	public static String URL_HOME = "/";
	public static String URL_PERSIAPAN_PERWALIAN = "/perwalian";
	public static String URL_JADWAL_KULIAH = "/jadwalkuliah";
	public static String URL_SYARAT_KELULUSAN = "/kelulusan";
	public static String URL_LOG_OUT = "/logout";
	
	public FunctionalTest() throws IOException{
		this.objFileConfReader = FileConfReader.getObjFileConfReader();
	}
	
	/**
	 * Setup sebelum browser dijalankan
	 */
	@Before
	public void setUp() {
		server = testServer(PORT);
		driver = new FirefoxDriver();
		browser = new TestBrowser(driver, baseURL);
	}

	/**
	 * Setup setelah browser dijalankan
	 */
	@After
	public void tearDown() {
		browser.quit();
	}

}