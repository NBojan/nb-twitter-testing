import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;

public class UploadProfilePictureTest {
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

//    @BeforeTest
//    @Parameters("browser")
//    public void setup(String browser) throws Exception {
//        if (browser.equalsIgnoreCase("firefox")) {
//            driver = new FirefoxDriver();
//        } else if (browser.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
//        } else if (browser.equalsIgnoreCase("Edge")) {
//            driver = new EdgeDriver();
//        } else {
//            throw new Exception("Incorrect Browser");
//        }
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
//        login();
//    }
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
        driver.get(utils.url);
    }

    @Test(testName = "034", enabled = true, priority = 1)
    public void ValidateProfilePictureChangeByUsingAValidImage() throws InterruptedException {
        // new file and oldImageSrc
        String number = utils.getRandomNumber(2);
        File uploadFile = new File("src/test/images/profilePics/" + number + ".jpg");
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        String oldImage = leftContainer.findElement(By.xpath("//img[@alt='UserImg']")).getAttribute("src");
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // attach picture and upload
        uploadModal.findElement(By.id("profilePic")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        uploadModal.findElement(By.xpath("//button[text()='Upload']")).click();
        // assert modal close
        Assert.assertFalse(driver.findElement(By.cssSelector("aside#closeModal textarea")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
        // refresh and compare images
        driver.navigate().refresh();
        WebElement leftContainer1 = driver.findElement(By.id("leftSidebar"));
        String newImage = leftContainer1.findElement(By.xpath("//img[@alt='UserImg']")).getAttribute("src");
        Assert.assertNotSame(oldImage, newImage, "images are the same");
    }
    @Test(testName = "035", enabled = true, priority = 2)
    public void ValidateProfilePicturePreviewAfterSelectingAnImage() throws InterruptedException {
        // new file and oldImageSrc
        String number = utils.getRandomNumber(2);
        File uploadFile = new File("src/test/images/profilePics/" + number + ".jpg");
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        String oldImage = leftContainer.findElement(By.xpath("//img[@alt='UserImg']")).getAttribute("src");
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // attach picture and assert
        uploadModal.findElement(By.id("profilePic")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
    }
    @Test(testName = "036", enabled = true, priority = 3)
    public void ValidateRemovingASelectedImageAndSelectAPictureAgain() throws InterruptedException {
        // new file and oldImageSrc
        String number = utils.getRandomNumber(2);
        File uploadFile = new File("src/test/images/profilePics/" + number + ".jpg");
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        String oldImage = leftContainer.findElement(By.xpath("//img[@alt='UserImg']")).getAttribute("src");
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // attach picture and assert
        uploadModal.findElement(By.id("profilePic")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // remove picture
        uploadModal.findElement(By.xpath("//img[@alt='selectedFile']/parent::div")).click();
        Assert.assertEquals(uploadModal.findElements(By.cssSelector("img[alt='selectedFile']")).size(), 0, "element still exists");
        //attach picture and assert
        uploadModal.findElement(By.id("profilePic")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
    }
    @Test(testName = "037", enabled = true, priority = 4)
    public void ValidateUploadButtonDisabledIfNoImageSelected() throws InterruptedException {
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // assert button
        Assert.assertFalse(uploadModal.findElement(By.xpath("//button[text()='Upload']")).isEnabled());
    }
    @Test(testName = "038", enabled = false, priority = 5)
    public void ValidateUploadButtonDisabledIfWrongFormatSelected() throws InterruptedException {
        // new file and oldImageSrc
        File uploadFile = new File("src/test/images/wrongFormat/1.pdf");
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // attach picture and assert
        uploadModal.findElement(By.id("profilePic")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadModal.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // assert button
        Assert.assertFalse(uploadModal.findElement(By.xpath("//button[text()='Upload']")).isEnabled());
    }
    @Test(testName = "039", enabled = true, priority = 6)
    public void ValidateCloseButtonClosesTheUploadContainer() throws InterruptedException {
        // new file and oldImageSrc
        WebElement leftContainer = driver.findElement(By.id("leftSidebar"));
        // open upload modal
        leftContainer.findElement(By.cssSelector(".userProfile")).click();
        leftContainer.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // uploadModal assert visible
        Thread.sleep(100);
        WebElement uploadModal = driver.findElement(By.xpath("//aside[@id='closeModal' and contains(@class, 'z-50')]"));
        Assert.assertTrue(uploadModal.isDisplayed());
        // closeButton
        uploadModal.findElements(By.cssSelector("button")).getFirst().click();
        // assert modal closes
        Assert.assertFalse(driver.findElement(By.cssSelector("aside#closeModal textarea")).isDisplayed());
        Assert.assertFalse(uploadModal.isDisplayed());
    }
}
