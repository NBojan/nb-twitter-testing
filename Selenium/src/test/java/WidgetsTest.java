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

public class WidgetsTest {
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

    @Test(testName = "040", enabled = true, priority = 1)
    public void VerifyTheSearchBarIsVisible() {
        WebElement rightContainer = driver.findElement(By.id("rightSidebar"));
        Assert.assertTrue(rightContainer.findElement(By.cssSelector("input[name='search']")).isDisplayed());
    }

    @Test(testName = "041", enabled = true, priority = 2 )
    public void VerifyTheWhatIsHappeningBoxIsVisible() {
        WebElement rightContainer = driver.findElement(By.id("rightSidebar"));
        Assert.assertTrue(rightContainer.findElement(By.xpath("//h4[contains(text(), 'happening')]")).isDisplayed());
    }

    @Test(testName = "042", enabled = true, priority = 3 )
    public void VerifyTheFollowSuggestionsBoxIsVisible() {
        WebElement rightContainer = driver.findElement(By.id("rightSidebar"));
        Assert.assertTrue(rightContainer.findElement(By.xpath("//h4[text()='Who to follow?']")).isDisplayed());
    }

    @Test(testName = "043", enabled = true, priority = 10)
    public void VerifyTheWholeContainerIsNotVisibleBelow1024pxScreenWidth() {
        driver.manage().window().setSize(new Dimension(800, 1200));
        WebElement rightContainer = driver.findElement(By.id("rightSidebar"));
        Assert.assertFalse(rightContainer.isDisplayed());
    }
}
