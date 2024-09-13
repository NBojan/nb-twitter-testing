import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class PostPageTest {
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
        driver.get(utils.forzaMilanPostUrl);
    }

    @Test(testName = "094", enabled = true, priority = 1)
    public void ValidateLinkInTheHeaderRedirectsToTheHomepage() {
        driver.findElement(By.cssSelector("header a")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Home']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url, "url is: " + driver.getCurrentUrl());
    }

    @Test(testName = "095", enabled = true, priority = 2)
    public void VerifyTheRepliesArePostedForAPostThatHasReplies() {
        WebElement postContainer = driver.findElement(By.xpath("//h5[text()='Replies']/parent::div"));
        Assert.assertFalse(postContainer.findElements(By.cssSelector("article")).isEmpty());
    }

    @Test(testName = "096", enabled = true, priority = 3)
    public void VerifyTheProvidedMessageAppearsForPostsWithNoComments() {
        driver.get("https://nb-twitter.vercel.app/tweet/0eEQirDmCBRRg0M6t7lX");
        String msg = "Be the first to leave a reply.";
        WebElement postContainer = driver.findElement(By.xpath("//h5[text()='Replies']/parent::div"));
        Assert.assertTrue(postContainer.findElements(By.cssSelector("article")).isEmpty());
        WebElement postedMessage = postContainer.findElement(By.cssSelector("p"));
        Assert.assertEquals(postedMessage.getText(), msg, "message displayed is different");
    }

}
