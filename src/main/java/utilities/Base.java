package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class Base {

	public static JavascriptExecutor executor = null;
	public static WebDriver driver = null;
	public static String browser = null;
	public static String URL = null;
	public static int timeLimit = 0;
	public static String mainDir = System.getProperty("user.dir");

	static {
		try {
			FileInputStream stream = new FileInputStream(mainDir+"\\src\\config.properties");
			Properties properties = new Properties();
			properties.load(stream);
			browser = properties.getProperty("browser");
			URL = properties.getProperty("URL");
			timeLimit = Integer.parseInt(properties.getProperty("timeLimit"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static WebDriver getDriver() 
	{
		if(browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",mainDir+"\\Resources\\chromedriver.exe");
			
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			
			ChromeOptions options = new ChromeOptions();
			options.merge(capabilities);
			
			driver = new ChromeDriver(options);
		}else if(browser.equals("edge")) {
			System.setProperty("webdriver.edge.driver", mainDir + "\\Resources\\msedgedriver.exe");
			driver = new EdgeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeLimit, TimeUnit.SECONDS);
		driver.get(URL);
		executor = (JavascriptExecutor) driver;
		
		return driver;
	}
	
	public static void highLight(WebElement elem) {
		executor.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;')", elem);
	}

	public static void click(WebElement elem) {
		//WebElement elem = driver.findElement(by);
		highLight(elem);
		try {
			takeScreenshot();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		elem.click();
	}
	
	// for text box
	public static void typeText(By by,String value) {
		WebElement elem = driver.findElement(by);
		highLight(elem);
		elem.sendKeys(value);
	}
	
	//for static drop down select by index
	public static void dropDownByIndex(By by, int index) {
		WebElement elem = driver.findElement(by);
		highLight(elem);
		Select select = new Select(elem);
		select.selectByIndex(index);
	}
	
	//for static drop down select by value & visible text
	public static void staticDropDown(By by,String value) {
		WebElement elem = driver.findElement(by);
		highLight(elem);
		Select select = new Select(elem);
		boolean flag = true;
		
		do {
			try {
				if(flag)
					select.selectByValue(value);
				else
					select.deselectByVisibleText(value);
				break;
			}catch(Exception e) {
				if(flag) {
					flag=false;
					continue;
				}else
					e.printStackTrace();
			}
		}while(true);
	}
	
	public static String getHiddenText(String strValue) {
		return (String) executor.executeScript("document.getElementById(\"email\").value()");
	}
	
	public static void takeScreenshot() throws IOException {
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File("F:\\screenshots\\demo.jpg"));
	}
	
	// for quit
	public static void workDone() {
		driver.quit();
	}
}
