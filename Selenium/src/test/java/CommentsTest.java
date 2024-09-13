import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Random;

public class CommentsTest {
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
        driver.get(utils.forzaMilanPostUrl);
    }

    @Test(testName = "097", enabled = true, priority = 1)
    public void ValidateLikingAndRemovingTheLikeOnAComment() {
        WebElement comment = driver.findElement(By.xpath(utils.ownCommentLocator));
        // like
        comment.findElement(By.cssSelector(".like-btn")).click();
        Assert.assertTrue(comment.findElement(By.cssSelector(".dislike-btn")).isDisplayed());
        // remove like
        comment.findElement(By.cssSelector(".dislike-btn")).click();
        Assert.assertTrue(comment.findElement(By.cssSelector(".like-btn")).isDisplayed());
    }

    @Test(testName = "098", enabled = true, priority = 2)
    public void VerifyThereIsNoCommentButtonOnAComment() {
        WebElement comment = driver.findElement(By.xpath(utils.ownCommentLocator));
        // assert no comment button
        Assert.assertEquals(comment.findElements(By.cssSelector(".comment-btn")).size(), 0, "there is a comment button on the comment");
    }

    @Test(testName = "099", enabled = true, priority = 3)
    public void ValidateOptionToRemoveCommentIsVisibleForAPersonalComment() {
        WebElement comment = driver.findElement(By.xpath(utils.ownCommentLocator));
        // assert remove button displayed
        Assert.assertTrue(comment.findElement(By.cssSelector("button.delete-btn")).isDisplayed());
    }

    @Test(testName = "100", enabled = true, priority = 4)
    public void ValidateOptionToRemoveCommentIsNotVisibleForAnotherUsersComment() {
        WebElement comment = driver.findElement(By.xpath(utils.anotherUserCommentLocator));
        // assert remove button not present
        Assert.assertEquals(comment.findElements(By.cssSelector(".delete-btn")).size(), 0, "there is a delete button on the non-personal-comment");
    }

    @Test(testName = "101", enabled = true, priority = 5, description = "TC-81 before executing")
    public void ValidatePostingAndRemovingAComment() throws InterruptedException {
        // TC-81
        String text = utils.getSaltString(10);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // go to the post page
        tweet.findElement(By.cssSelector("p.post-text")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        WebElement tweet1 = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet1.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        uploadModal.findElement(By.id("comment-textarea")).sendKeys(text);
        // upload
        uploadModal.findElement(By.xpath("//button[text()='Reply']")).click();
        // assert modal closed
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.invisibilityOf(uploadModal));
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
        // end of TC-81
        // remove comment
        newComment.findElement(By.cssSelector("button.delete-btn")).click();
        driver.switchTo().alert().accept();
        Assert.assertEquals(driver.findElements(By.xpath("//p[text()='" + text + "']/ancestor::article")).size(), 0, "comment still exists");
    }
}
