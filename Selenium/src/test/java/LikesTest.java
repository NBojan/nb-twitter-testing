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

public class LikesTest {
    WebDriver driver;
    Utils utils = new Utils();
    // except the always liked post, the others should always remove the like after liking, for no testing breaks

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
        driver.get(utils.url);
    }

    @Test(testName = "070", enabled = true, priority = 1)
    public void ValidateLikingAndRemovingLikeOnAPostFromAnotherUser() {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // like
        tweet.findElement(By.cssSelector(".like-btn")).click();
        Assert.assertTrue(tweet.findElement(By.cssSelector(".dislike-btn")).isDisplayed());
        // remove like
        tweet.findElement(By.cssSelector(".dislike-btn")).click();
        Assert.assertTrue(tweet.findElement(By.cssSelector(".like-btn")).isDisplayed());
    }

    @Test(testName = "071", enabled = true, priority = 2)
    public void ValidateLikingAndRemovingLikeOnAPersonalPost() {
        WebElement tweet = driver.findElement(By.xpath(utils.ownPostLocator));
        // like
        tweet.findElement(By.cssSelector(".like-btn")).click();
        Assert.assertTrue(tweet.findElement(By.cssSelector(".dislike-btn")).isDisplayed());
        // remove like
        tweet.findElement(By.cssSelector(".dislike-btn")).click();
        Assert.assertTrue(tweet.findElement(By.cssSelector(".like-btn")).isDisplayed());
    }

    @Test(testName = "072", enabled = true, priority = 3)
    public void ValidateLikingAndRemovingTheLikeAfterGoingToThePostPage() {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // go to post page
        tweet.findElement(By.cssSelector("p.post-text")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        // like
        WebElement tweetInPage = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        tweetInPage.findElement(By.cssSelector(".like-btn")).click();
        Assert.assertTrue(tweetInPage.findElement(By.cssSelector(".dislike-btn")).isDisplayed());
        // remove like
        tweetInPage.findElement(By.cssSelector(".dislike-btn")).click();
        Assert.assertTrue(tweetInPage.findElement(By.cssSelector(".like-btn")).isDisplayed());
    }

    @Test(testName = "073", enabled = true, priority = 4)
    public void ValidateLikingAndRemovingTheLikeAfterGoingToAProfilePage() {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // go to post page
        tweet.findElement(By.cssSelector("p.post-username")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("p.profile-name")).isDisplayed());
        // like
        WebElement tweetInPage = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        tweetInPage.findElement(By.cssSelector(".like-btn")).click();
        Assert.assertTrue(tweetInPage.findElement(By.cssSelector(".dislike-btn")).isDisplayed());
        // remove like
        tweetInPage.findElement(By.cssSelector(".dislike-btn")).click();
        Assert.assertTrue(tweetInPage.findElement(By.cssSelector(".like-btn")).isDisplayed());
    }

    @Test(testName = "074", enabled = true, priority = 5)
    public void VerifyNumberOfLikesIsDisplayedForAPostThatHasAtLeast1Like() {
        WebElement tweet = driver.findElement(By.xpath(utils.alwaysLikedPostLocator));
        Assert.assertTrue(tweet.findElement(By.cssSelector("span.likeNumber")).isDisplayed());
    }

    @Test(testName = "075", enabled = true, priority = 6)
    public void VerifyTheNumberOfLikesIsHiddenIfThereAreNoLikes() {
        WebElement tweet = driver.findElement(By.xpath(utils.ownPostLocator));
        Assert.assertEquals(tweet.findElements(By.cssSelector("span.likeNumber")).size(), 0, "this element exists");
    }
}
