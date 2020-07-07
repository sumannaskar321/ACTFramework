

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utilities.Base;

public class Script_01_Test {

	public static void main(String[] args) {
		
		WebDriver driver = Base.getDriver();
		
		Base.click(driver.findElement(By.xpath("(//a[contains(text(),'Input Forms')])[1]")));
		//driver.findElement(By.xpath("(//a[contains(text(),'Input Forms')])[1]"));
		Base.workDone();
	}

}
