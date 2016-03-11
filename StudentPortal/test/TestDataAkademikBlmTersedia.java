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

import java.util.Arrays;

import static org.fluentlenium.core.filter.FilterConstructor.*;

/**
 * 
 * Kelas untuk mengetes jika pengguna masih menempuh semester 1
 * akan ditampilkan pesan "DATA AKADEMIK BELUM TERSEDIA"
 * 
 * @author FTIS\i13013
 *
 */
public class TestDataAkademikBlmTersedia extends WithBrowser {
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
   * Jika pengguna belum memiliki riwayat nilai misalnya jika baru menempuh semester 1
   * akan ditampilkan "DATA AKADEMIK BELUM TERSEDIA"
   */
  @Test
  public void testDataAkademkNotAvailable() {
      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
        	  browser.goTo("/");
			  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/ringkasan");
			  
			  
			  FluentList<FluentWebElement> e1 = browser.find("div",withClass("row"));

			  FluentList<FluentWebElement> e2 = browser.find("h2",withClass("text-center"));
			  
			  System.out.println("hasil : " + e2.getText());
			  assertEquals("RINGKASAN DATA AKADEMIK",
					e2.getText());
			  
			  System.out.println("hasil : " + e1.get(1).find("h5").get(0).getText());
			  assertEquals("DATA AKADEMIK BELUM TERSEDIA",
					  e1.get(1).find("h5").get(0).getText());
          }
      });
  }
}