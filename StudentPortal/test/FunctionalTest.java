import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import play.test.TestBrowser;
import play.test.TestServer;
import play.test.WithBrowser;

import static play.test.Helpers.testServer;

/**
 * Super class untuk kelas testing
 * Terdapat attribute WebDriver (untuk pengujian ini digunakan FireFox), TestBrowser, Port, serta Url yang digunakan untuk pengujian
 * @author FTIS\i13015
 */
public class FunctionalTest extends WithBrowser {
  //basic info
  protected TestServer server;
  protected WebDriver driver;
  protected TestBrowser browser;
  
  protected static int PORT = 9000;
  protected String baseURL = String.format("http://localhost:%d", PORT);
  
  protected FileConfReader objFileConfReader = FileConfReader.getObjFileConfReader();
  
  /**
   * Setup sebelum browser dijalankan
   */
  @Before
  public void setUp() {	
	server = testServer(PORT);
	driver = new FirefoxDriver();
	browser = new TestBrowser(driver,baseURL);
  }
  
  /**
   * Setup setelah browser dijalankan
   */
  @After
  public void tearDown() {
	  browser.quit();
  }
  
}