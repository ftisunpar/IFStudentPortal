import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileConfReader {
	  private Properties prop = new Properties();
	  private InputStream input = null;
	  private String email_invalid;
	  private String email_notStudentUnpar;
	  private String pass_invalid;
	  private String email_valid;
	  private String pass_wrong;
	  private String pass_valid;
	  
	  private static FileConfReader objFileConfReader;
	  
	  FileConfReader(){
		  try {
  			input = new FileInputStream("conf/functionaltest.conf");
  			prop.load(input);
  			email_invalid=prop.getProperty("email.invalid");
  			email_notStudentUnpar=prop.getProperty("email.notStudentUnpar");
  			pass_invalid=prop.getProperty("password.invalid");
  			email_valid=prop.getProperty("email.valid");
  			pass_wrong=prop.getProperty("password.wrong");
  			pass_valid=prop.getProperty("password.valid");
  		} catch (IOException ex) {
  			ex.printStackTrace();
  		} finally {
  			if (input != null) {
  				try {
  					input.close();
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
  		}
	  }
	  
	  public String getEmailInvalid(){
		  return this.email_invalid;
	  }
	  
	  public String getEmailNotStudentUnpar(){
		  return this.email_notStudentUnpar;
	  }
	  
	  public String getPassInvalid(){
		  return this.pass_invalid;
	  }
	  
	  public String getEmailValid(){
		  return this.email_valid;
	  }

	  public String getPassWrong(){
		  return this.pass_wrong;
	  }
	  
	  public String getPassValid(){
		  return this.pass_valid;
	  }
	  
	  public static FileConfReader getObjFileConfReader(){
		  if(objFileConfReader==null){
			  objFileConfReader= new FileConfReader();
			  return objFileConfReader;
		  }
		  return objFileConfReader;
	  }

}
