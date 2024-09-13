import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTest {
    WebDriver driver;
    Utils utils = new Utils();

    public void logout() {
        WebElement leftContainer = driver.findElement(By.cssSelector("#leftSidebar"));
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Sign Out']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Log In']")).isDisplayed());
    }

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {
        if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        } else {
            throw new Exception("Incorrect Browser");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(utils.registerUrl);
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Sign Up']")).isDisplayed());
    }

    @Test(testName = "019", groups = {"smoke"}, enabled = true, priority = 1)
    public void ValidateLoginByUsingValidEmailAndPassword() {
        String validEmail = "mytesting@testing.com";
        String validPass = "Testing123";
        driver.findElement(By.name("email")).sendKeys(validEmail);
        driver.findElement(By.name("password")).sendKeys(validPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        logout();
    }

    @Test(testName = "020", groups = {"smoke"}, enabled = true, priority = 2)
    public void ValidateRedirectToTheDashboardAfterLogin() throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        Thread.sleep(1000);
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url,"url is different that the homepage url");
        logout();
    }

    @Test(testName = "021", enabled = true, priority = 3)
    public void SubmitButtonIsDisabledIfOnlyOneFieldIsFilled() {
        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "022", enabled = true, priority = 4)
    public void SubmitButtonIsDisabledIfWhiteSpacesAreFilledInBothFields() {
        driver.findElement(By.name("email")).sendKeys(" ");
        driver.findElement(By.name("password")).sendKeys(" ");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "023", enabled = true, priority = 5)
    public void ValidateErrorMessageUsingValidEmailButWrongPassword() {
        String validEmail = "mytesting@testing.com";
        String invalidPass = "wrongPassForSure";
        String errMsg = "Invalid email or password";
        driver.findElement(By.name("email")).sendKeys(validEmail);
        driver.findElement(By.name("password")).sendKeys(invalidPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "024", groups = {"smoke"}, enabled = true, priority = 6)
    public void ValidateLinkToSwitchToSignUpIsVisible() {
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Sign Up']")).isDisplayed());
    }

    @Test(testName = "025", groups = {"smoke"}, enabled = true, priority = 7)
    public void ValidateLinkToSwitchToSignUpSwitchesToTheRegisterForm() {
        driver.findElement(By.xpath("//span[text()='Sign Up']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Log In']")).isDisplayed());
    }
}
