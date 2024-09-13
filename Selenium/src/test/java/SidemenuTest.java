import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class SidemenuTest {
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
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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

    @Test(testName = "026", enabled = true)
    public void VerifyTheTwitterLogoIsVisible() {
        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
    }
    @Test(testName = "027", groups = {"smoke"}, enabled = true)
    public void VerifyTheTwitterLogoIsALinkToTheHomepage() {
        driver.get(utils.dummyUrl);
        driver.findElement(By.xpath("//img[@alt='Logo']")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("section.flex-1 #main-textarea")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url, "the url is: " + driver.getCurrentUrl());
    }
    @Test(testName = "028", enabled = true)
    public void VerifyTheLinksExistInTheSidemenu() {
        Assert.assertTrue(driver.findElement(By.id("leftSidebarLinks")).isDisplayed());
    }
    @Test(testName = "029", groups = {"smoke"}, enabled = true)
    public void VerifyTheTweetButtonIsVisible() {
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        Assert.assertTrue(leftContainer.findElement(By.xpath("//button[text()='Tweet']")).isDisplayed());
//        Assert.assertTrue(leftContainer.findElement(By.cssSelector(".sidebar-tweet")).isDisplayed());
    }
    @Test(testName = "030", enabled = true)
    public void VerifyTheProfileMenuIsVisible() {
        Assert.assertTrue(driver.findElement(By.cssSelector(".userProfile")).isDisplayed());
    }
    @Test(testName = "031", enabled = true, priority = 10)
    public void VerifyTheSidemenuDisappearsInScreensBelow640pxWidth() {
        driver.manage().window().setSize(new Dimension(600, 900));
        Assert.assertFalse(driver.findElement(By.id("leftSidebar")).isDisplayed(), "leftSidebar is displayed");
    }
}
