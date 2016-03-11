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
 * Kelas untuk mengetes permasalahan jadwal kuliah, jika Jika pengguna belum melakukan FRS, 
 * cuti studi, atau jadwal kuliah pengguna belum tersedia, akan ditampilkan pesan 
 * “JADWAL KULIAH BELUM TERSEDIA”
 * 
 * @author FTIS\i13054
 *
 */
public class TestJadwalKuliah2 extends WithBrowser {
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
   * namun belum FRS, cuti studi, atau jadwal kuliah pengguna belum tersedia,
   * Maka ditampilkan "JADWAL KULIAH BELUM TERSEDIA".
   */
  @Test
  public void testJadwalKuliahBelumFRS() {
      running(testServer(9000), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {  
        	  browser.goTo("/");
        	  browser.find(".form-control", withId("email-input")).get(0).text(objFileConfReader.getEmailValid());
			  browser.find(".form-control", withId("pw-input")).get(0).text(objFileConfReader.getPassValid());
			  browser.find(".form-control", withName("submit")).get(0).click();
			  browser.goTo("/jadwalkuliah");
			  FluentList<FluentWebElement> e1 = browser.find(".row").get(1).find("h5");
			  if(e1.size()>0){
				  assertEquals("JADWAL KULIAH BELUM TERSEDIA", 
						  browser.find(".row").get(1).find("h5").get(0).getText());
			  }
			  else{
				  assertEquals("JADWAL KULIAH BELUM TERSEDIA", "testGagal");
			  }
			  
				  
			  
				  
          }
      });
  }
}
