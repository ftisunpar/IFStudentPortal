import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import play.test.TestBrowser;
import play.test.WithBrowser;
import play.libs.F.Callback;
import static play.test.Helpers.HTMLUNIT; 
import static play.test.Helpers.running; 
import static play.test.Helpers.testServer;
import static org.fluentlenium.core.filter.FilterConstructor.*;


/**
 * 
 * Kelas untuk mengetes saat pengguna menjalankan aplikasi.
 * 
 * @author FTIS\i13015
 *
 */
public class TestInitialRun extends WithBrowser {
  //basic info
  private WebDriver driver;
  private static int PORT = 9000;
  private String baseURL = String.format("http://localhost:%d", PORT);
  private FileConfReader objFileConfReader = FileConfReader.getObjFileConfReader();

  @Before
  public void setUp() {	
	driver = new FirefoxDriver();
	browser = new TestBrowser(driver, baseURL);
  }
  
  @After
  public void tearDown() {
	  browser.quit();
  }
  
  /**
   * Pengguna menjalankan aplikasi kemudian halaman login akan ditampilkan.
   */
  @Test
  public void testBlankUserPass() {	  
      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
        	  browser.goTo("/");
			  assertEquals("Login", browser.find(".form-control", withName("submit")).get(0).getText());
          }
      });
  }
}
