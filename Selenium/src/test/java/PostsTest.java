import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class PostsTest {
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

    @Test(testName = "064", enabled = true, priority = 1)
    public void VerifyAPostIncludesAProfilePictureUsernameNameAndTime() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        Assert.assertTrue(tweet.findElement(By.cssSelector("img[alt='UserImg']")).isDisplayed());
        Assert.assertTrue(tweet.findElement(By.cssSelector("p.post-name")).isDisplayed());
        Assert.assertTrue(tweet.findElement(By.cssSelector("p.post-username")).isDisplayed());
        Assert.assertTrue(tweet.findElement(By.cssSelector("p.post-timestamp")).isDisplayed());
    }

    @Test(testName = "065", enabled = true, priority = 2)
    public void VerifyAPostIncludesTextAndAnImage() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath("//p[text()='My buddy is chill, lol']/ancestor::article"));
        Assert.assertTrue(tweet.findElement(By.cssSelector("img[alt='postImage']")).isDisplayed());
        Assert.assertTrue(tweet.findElement(By.cssSelector("p.post-text")).isDisplayed());
    }

    @Test(testName = "066", enabled = true, priority = 3)
    public void ValidateTheUsernameIsALinkToTheUserProfile() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath(utils.alwaysLikedPostLocator)); //mytestingaccount
        tweet.findElement(By.cssSelector("p.post-username")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("p.profile-name")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.myTestingAccountUrl, "Url is: " + driver.getCurrentUrl());
    }

    @Test(testName = "067", enabled = true, priority = 4)
    public void ValidateTheNameIsALinkToTheUserProfile() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath(utils.alwaysLikedPostLocator)); //mytestingaccount
        tweet.findElement(By.cssSelector("p.post-name")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("p.profile-name")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.myTestingAccountUrl, "Url is: " + driver.getCurrentUrl());
    }

    @Test(testName = "068", enabled = true, priority = 5)
    public void ValidateTheTextIsALinkToThePostPage() {
        String postUrl = "https://nb-twitter.vercel.app/tweet/E0bBXaNW1zYhgvY3ZMAF";
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath("//p[text()='My buddy is chill, lol']/ancestor::article"));
        tweet.findElement(By.cssSelector("p.post-text")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), postUrl, "Url is: " + driver.getCurrentUrl());
    }

    @Test(testName = "069", enabled = true, priority = 6)
    public void ValidateTheImageIsALinkToThePostPage() {
        String postUrl = "https://nb-twitter.vercel.app/tweet/E0bBXaNW1zYhgvY3ZMAF";
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        WebElement tweet = driver.findElement(By.xpath("//p[text()='My buddy is chill, lol']/ancestor::article"));
        tweet.findElement(By.cssSelector("img[alt='postImage']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), postUrl, "Url is: " + driver.getCurrentUrl());
    }
}
