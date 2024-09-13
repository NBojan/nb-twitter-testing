import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class HeaderTest {
    WebDriver driver;
    Utils utils = new Utils();

    public void login() {
        String validEmail = "mytesting@testing.com";
        String validPass = "Testing123";
        driver.get(utils.registerUrl);
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Sign Up']")).isDisplayed());
        driver.findElement(By.name("email")).sendKeys(validEmail);
        driver.findElement(By.name("password")).sendKeys(validPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
    }
    public void dummyLogin() {
        driver.get(utils.registerUrl);
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Sign Up']")).isDisplayed());
        driver.findElement(By.xpath("//button[text()='Use Dummy Account']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
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
        driver.manage().window().maximize();
        login();
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//        driver.manage().window().maximize();
//        login();
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(utils.url);
    }

    @Test(testName = "046", enabled = true, priority = 1)
    public void ValidateTheLinkIsGoingBackToTheHomepage() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        mainContainer.findElement(By.linkText("Home")).click();
        Assert.assertEquals(driver.getCurrentUrl(), utils.url, "url is: " + driver.getCurrentUrl());
    }
    @Test(testName = "047", enabled = true, priority = 2)
    public void ValidateTheThemeButtonIsVisible() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        Assert.assertTrue(mainContainer.findElement(By.id("themeButton")).isDisplayed());
    }
    @Test(testName = "048", enabled = true, priority = 3)
    public void VerifyThereIsASubmenuButtonOnScreensBelow640pxWidth() {
        driver.manage().window().setSize(new Dimension(500, 800));
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        Assert.assertTrue(mainContainer.findElement(By.id("sidebarButton")).isDisplayed());
    }
}
