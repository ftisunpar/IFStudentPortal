import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
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
 * Kelas untuk mengetes saat pengguna logout saat sudah berhasil masuk ke aplikasi.
 * 
 * @author FTIS\i13015
 *
 */
public class TestLogout extends WithBrowser {
  //basic info
  private WebDriver driver;
  private static int PORT = 9000;
  private String baseURL = String.format("http://localhost:%d", PORT);

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
   * Pengguna logout, pengguna akan diarahkan kembali ke halaman login
   */
  @Test
  public void testLogout() {
	  running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
		  public void invoke(TestBrowser browser) {
			  browser.goTo("/");
			  browser.find(".form-control", withId("email-input")).get(0).text("7313006@student.unpar.ac.id");
			  browser.find(".form-control", withId("pw-input")).get(0).text("pass valid");
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/logout");
			  assertEquals("Login", browser.find(".form-control", withName("submit")).get(0).getText());
		  }
	  });
  }
  
}
