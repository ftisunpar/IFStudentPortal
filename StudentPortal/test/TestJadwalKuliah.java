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
 * Kelas untuk mengetes permasalahan jadwal kuliah, jika jadwal kuliah pengguna sudah tersedia
 * akan ditampilkan jadwal kuliah dalam bentuk kalendar yang sudah diurutkan berdasarkan hari
 * 
 * @author FTIS\i13006
 *
 */
public class TestJadwalKuliah extends WithBrowser {
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
   * Jika pengguna menuju navigasi drawer dan melalukan click terhadap jadwal kuliah
   * akan ditampilkan halaman jadwal kuliah dalam bentuk kalendar yang sudah diurutkan berdasarkan hari
   */
  @Test
  public void testJadwalKuliahValid() {
      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {  
        	  browser.goTo("/");
        	  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/jadwalkuliah");
			  assertEquals("JADWAL KULIAH", 
					  browser.find(".row").get(0).find("h2").get(0).getText());
			  
			  FluentList<FluentWebElement> e1 = browser.find("#Senin");
			  System.out.println(e1.get(0).getText());
			  
			  assertEquals("Senin", 
					  browser.find("#Senin").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
			  assertEquals("Selasa", 
					  browser.find("#Selasa").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
			  assertEquals("Rabu", 
					  browser.find("#Rabu").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
			  assertEquals("Kamis", 
					  browser.find("#Kamis").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
			  assertEquals("Jumat", 
					  browser.find("#Jumat").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
			  assertEquals("Sabtu", 
					  browser.find("#Sabtu").get(0).find("thead").get(0).find("tr").get(0).find("th").get(0).getText());
          }
      });
  }
  
}
