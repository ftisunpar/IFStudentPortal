import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import play.test.TestBrowser;
import play.test.TestServer;
import play.test.WithBrowser;

import static play.test.Helpers.testServer;

public class FunctionalTest extends WithBrowser {
  //basic info
  protected TestServer server;
  protected WebDriver driver;
  protected TestBrowser browser;
  
  protected static int PORT = 9000;
  protected String baseURL = String.format("http://localhost:%d", PORT);
  
  protected FileConfReader objFileConfReader = FileConfReader.getObjFileConfReader();

  @Before
  public void setUp() {	
	server = testServer(PORT);
	driver = new FirefoxDriver();
	browser = new TestBrowser(driver,baseURL);
  }
  
  @After
  public void tearDown() {
	  browser.quit();
  }
  
}