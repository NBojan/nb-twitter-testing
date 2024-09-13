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

import java.io.File;
import java.time.Duration;

public class UploadCommentTest {
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

    @Test(testName = "076", enabled = true, priority = 1)
    public void ValidateTheCommentButtonOpensTheUploadCommentContainer() throws InterruptedException {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
    }

    @Test(testName = "077", enabled = true, priority = 2)
    public void ValidatePostingACommentWithTextAndImageOnAPostFromAnotherUser() throws InterruptedException {
        String text = utils.getSaltString(10);
        String numb = utils.getRandomNumber(5);
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        uploadModal.findElement(By.id("comment-textarea")).sendKeys(text);
        // insert image
        uploadModal.findElement(By.id("forComment")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // upload
        uploadModal.findElement(By.xpath("//button[text()='Reply']")).click();
        // assert redirect and modal closed
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(newComment.findElement(By.xpath("//img[@alt='postImage']"))));
        Assert.assertTrue(newComment.findElement(By.cssSelector("img[alt='postImage']")).isDisplayed());
    }

    @Test(testName = "078", enabled = true, priority = 3)
    public void ValidatePostingACommentWithTextOnAPostFromAnotherUser() throws InterruptedException {
        String text = utils.getSaltString(10);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        uploadModal.findElement(By.id("comment-textarea")).sendKeys(text);
        // upload
        uploadModal.findElement(By.xpath("//button[text()='Reply']")).click();
        // assert redirect and modal closed
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
    }

    @Test(testName = "079", enabled = true, priority = 4)
    public void ValidatePostingACommentOnAPersonalPostWithTextAndImage() throws InterruptedException {
        String text = utils.getSaltString(10);
        String numb = utils.getRandomNumber(5);
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        WebElement tweet = driver.findElement(By.xpath(utils.ownPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        uploadModal.findElement(By.id("comment-textarea")).sendKeys(text);
        // insert image
        uploadModal.findElement(By.id("forComment")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // upload
        uploadModal.findElement(By.xpath("//button[text()='Reply']")).click();
        // assert redirect and modal closed
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(newComment.findElement(By.xpath("//img[@alt='postImage']"))));
        Assert.assertTrue(newComment.findElement(By.cssSelector("img[alt='postImage']")).isDisplayed());
    }

    @Test(testName = "080", enabled = true, priority = 5)
    public void ValidatePostingACommentOnAPersonalPostWithTextOnly() throws InterruptedException {
        String text = utils.getSaltString(10);
        WebElement tweet = driver.findElement(By.xpath(utils.ownPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        uploadModal.findElement(By.id("comment-textarea")).sendKeys(text);
        // upload
        uploadModal.findElement(By.xpath("//button[text()='Reply']")).click();
        // assert redirect and modal closed
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
    }

    @Test(testName = "081", enabled = true, priority = 6)
    public void ValidatePostingACommentFromThePostPage() throws InterruptedException {
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
    }

    @Test(testName = "082", enabled = true, priority = 7)
    public void ValidatePostingACommentFromAProfilePage() throws InterruptedException {
        String text = utils.getSaltString(10);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // go to the profile page
        tweet.findElement(By.cssSelector("p.post-username")).click();
        Assert.assertTrue(driver.findElement(By.cssSelector("p.profile-name")).isDisplayed());
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
        // assert redirect and modal closed
        Assert.assertTrue(driver.findElement(By.xpath("//h5[text()='Replies']")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // assert new comment
        WebElement newComment = driver.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article"));
        Assert.assertTrue(newComment.isDisplayed());
        Assert.assertTrue(newComment.findElement(By.cssSelector("p.post-text")).isDisplayed());
    }
    @Test(testName = "083", enabled = false, priority = 8)
    public void ValidateTextboxDoesNotAcceptMoreThan280Chars() throws InterruptedException {
        String text = utils.getSaltString(281);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert text
        WebElement textarea = uploadModal.findElement(By.id("comment-textarea"));
        textarea.sendKeys(text);
        // assert
        Assert.assertTrue(textarea.getText().length() <= 280, "textarea length is: " + textarea.getText().length());
    }

    @Test(testName = "084", enabled = true, priority = 9)
    public void VerifyThereIsAPreviewOfTheImageAfterSelectingIt() throws InterruptedException {
        String numb = utils.getRandomNumber(5);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert image
        uploadModal.findElement(By.id("forComment")).sendKeys(uploadFile.getAbsolutePath());
        // assert
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
    }

    @Test(testName = "085", enabled = true, priority = 10)
    public void ValidateRemovingASelectedImage() throws InterruptedException {
        String numb = utils.getRandomNumber(5);
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert image and assert
        uploadModal.findElement(By.id("forComment")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // remove image and assert
        uploadModal.findElement(By.xpath("//img[@alt='selectedFile']/parent::div")).click();
        Assert.assertEquals(uploadModal.findElements(By.cssSelector("img[alt='selectedFile']")).size(), 0);
    }

    @Test(testName = "086", enabled = true, priority = 11)
    public void ValidateReplyButtonIsDisabledIfNeitherTextOrImageInputted() throws InterruptedException {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // assert reply button
        Assert.assertFalse(uploadModal.findElement(By.xpath("//button[text()='Reply']")).isEnabled());
    }

    @Test(testName = "087", enabled = true, priority = 12)
    public void ValidateReplyButtonIsDisabledIfOnlyWhiteSpacesAreInputted() throws InterruptedException {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert white spaces
        uploadModal.findElement(By.id("comment-textarea")).sendKeys("   ");
        // assert reply button
        Assert.assertFalse(uploadModal.findElement(By.xpath("//button[text()='Reply']")).isEnabled());
    }

    @Test(testName = "088", enabled = false, priority = 13)
    public void ValidateReplyButtonIsDisabledIfWrongFormatOfFileIsSelected() throws InterruptedException {
        WebElement tweet = driver.findElement(By.xpath(utils.anotherUserPostLocator));
        File uploadFile = new File("src/test/images/wrongFormat/1.pdf");
        // open modal
        tweet.findElement(By.cssSelector(".comment-btn")).click();
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // insert wrong format
        uploadModal.findElement(By.id("forComment")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // assert reply button
        Assert.assertFalse(uploadModal.findElement(By.xpath("//button[text()='Reply']")).isEnabled());
    }
}
