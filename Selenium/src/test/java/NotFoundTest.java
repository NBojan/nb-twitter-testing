import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class NotFoundTest {
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
    @BeforeTest
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        login();
    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.navigate().refresh();
    }

    @Test(testName = "106", enabled = true, priority = 1)
    public void ValidateNotFoundPageAppearsWithMessageAfterSearchingForANonExistentUser() {
        String msg = "Could not find that user";
        driver.get("https://nb-twitter.vercel.app/profile/whatwhatwhat");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement message = driver.findElement(By.xpath("//h4[contains(text(), 'Could not find')]"));
        Assert.assertTrue(message.getText().contains(msg));
        Assert.assertTrue(mainContainer.findElement(By.linkText("Home")).isDisplayed());
    }

    @Test(testName = "107", enabled = true, priority = 2)
    public void ValidateNotFoundPageAppearsWithMessageAfterEnteringWrongUrl() {
        String msg = "Could not find what you were looking for";
        driver.get("https://nb-twitter.vercel.app/whatwhatwhat");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement message = driver.findElement(By.xpath("//h4[contains(text(), 'Could not find')]"));
        Assert.assertTrue(message.getText().contains(msg));
        Assert.assertTrue(mainContainer.findElement(By.linkText("Home")).isDisplayed());
    }

    @Test(testName = "108", enabled = true, priority = 3)
    public void ValidateNotFoundPageAppearsWithMessageAfterSearchingForANonExistentPost() {
        String msg = "Could not find that tweet";
        driver.get("https://nb-twitter.vercel.app/tweet/whatwhatwhat");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement message = driver.findElement(By.xpath("//h4[contains(text(), 'Could not find')]"));
        Assert.assertTrue(message.getText().contains(msg));
        Assert.assertTrue(mainContainer.findElement(By.linkText("Home")).isDisplayed());
    }
}