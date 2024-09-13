import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class RemovePostTest {
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

    @Test(testName = "089", enabled = false, priority = 1)
    public void ValidateOptionToRemoveAPostIsVisibleForPersonalPost() {
        WebElement tweet = driver.findElement(By.xpath(utils.ownPostLocator));
        // assert delete btn exists
        Assert.assertEquals(tweet.findElements(By.cssSelector("button.delete-btn")).size(), 1, "delete btn missing");
    }

    @Test(testName = "090", enabled = false, priority = 2)
    public void ValidateOptionToRemoveAPostIsNotVisibleForAnotherUsersPost() {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // assert delete btn exists
        Assert.assertEquals(tweet.findElements(By.cssSelector("button.delete-btn")).size(), 0, "delete btn exists when it should not");
    }

    @Test(testName = "091", enabled = false, priority = 3, description = "TC 55 before removing")
    public void ValidatePostingAndRemovingAPost() {
        //TC-55
        String text = utils.getSaltString(14);
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and submit
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.cssSelector("button")).click();
        // assert new tweet
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
        // end of TC-55
        // remove and assert
        WebElement newTweet = mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        newTweet.findElement(By.cssSelector("button.delete-btn")).click();
        driver.switchTo().alert().accept();
        Assert.assertEquals(mainContainer.findElements(By.xpath("//p[text()='" + text + "']/ancestor::article")).size(), 0, "post still exists");
    }

    @Test(testName = "092", enabled = false, priority = 4, description = "TC 55 before removing")
    public void ValidateRemovingPostAfterPostingAndGoingToThePostPage() {
        //TC-55
        String text = utils.getSaltString(14);
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and submit
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.cssSelector("button")).click();
        // assert new tweet
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
        // end of TC-55
        // go to the post page
        WebElement newTweet = mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        newTweet.findElement(By.cssSelector("p.post-text")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        // remove
        WebElement newTweet1 = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        newTweet1.findElement(By.cssSelector("button.delete-btn")).click();
        driver.switchTo().alert().accept();
        // assert
        Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Home']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url, "url is: " + driver.getCurrentUrl());
        Assert.assertEquals(driver.findElements(By.xpath("//p[text()='" + text + "']/ancestor::article")).size(), 0, "post still exists");
    }

    @Test(testName = "093", enabled = true, priority = 5, description = "TC 55 before removing")
    public void ValidateRemovingPostAfterPostingAndGoingToTheProfilePage() {
        //TC-55
        String text = utils.getSaltString(14);
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and submit
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.cssSelector("button")).click();
        // assert new tweet
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
        // end of TC-55
        // go to the post page
        WebElement newTweet = mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        newTweet.findElement(By.cssSelector("p.post-username")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("p.profile-username")).isDisplayed());
        // remove
        WebElement newTweet1 = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        newTweet1.findElement(By.cssSelector("button.delete-btn")).click();
        driver.switchTo().alert().accept();
        // assert
        Assert.assertEquals(driver.findElements(By.xpath("//p[text()='" + text + "']/ancestor::article")).size(), 0, "post still exists");
    }
}
