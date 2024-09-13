import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class ProfilePageTest {
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
        driver.get(utils.myTestingAccountUrl);
    }

    @Test(testName = "102", enabled = true, priority = 1)
    public void ValidateLinkInTheHeaderRedirectsToTheHomepage() {
        driver.findElement(By.cssSelector("header a")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("section.flex-1 textarea")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url, "url is: " + driver.getCurrentUrl());
    }

    @Test(testName = "103", enabled = true, priority = 2)
    public void VerifyTheUsersPostsAreVisible() {
        WebElement postsContainer = driver.findElement(By.xpath("//h5[text()='Tweets']/parent::div"));
        int postsLength = postsContainer.findElements(By.cssSelector("article")).size();
        Assert.assertTrue(postsLength > 0, "there are no posts");
    }

    @Test(testName = "104", enabled = true, priority = 3)
    public void VerifyTheProvidedMessageAppearsForPostsWithNoTweets() {
        driver.get("https://nb-twitter.vercel.app/profile/alexo");
        String msg = "No recent tweet activity.";
        WebElement postsContainer = driver.findElement(By.xpath("//h5[text()='Tweets']/parent::div"));
        WebElement message = postsContainer.findElement(By.cssSelector("h4"));
        Assert.assertEquals(message.getText(), msg, "Text is: " + message.getText());
    }

    @Test(testName = "105", enabled = true, priority = 4)
    public void VerifyTheProfilePictureUsernameNameAndNumberOfPostsAreDisplayed() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        Assert.assertTrue(mainContainer.findElement(By.cssSelector("img.border-2")).isDisplayed());
        Assert.assertTrue(mainContainer.findElement(By.cssSelector("p.profile-name")).isDisplayed());
        Assert.assertTrue(mainContainer.findElement(By.cssSelector("p.profile-username")).isDisplayed());
        Assert.assertTrue(mainContainer.findElement(By.cssSelector("p.profile-postLength")).isDisplayed());
    }
}
