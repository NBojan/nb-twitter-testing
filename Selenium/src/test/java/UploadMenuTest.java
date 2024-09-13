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

public class UploadMenuTest {
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

    @Test(testName = "055", enabled = true, priority = 1)
    public void ValidateUploadingAPostByUsingOnlyText() {
        String text = utils.getSaltString(14);
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and submit
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.cssSelector("button")).click();
        // assert new tweet
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
    }

    @Test(testName = "056", enabled = true, priority = 2)
    public void ValidateUploadingAPostByUsingTextAndAnImage() {
        String text = utils.getSaltString(14);
        String numb = utils.getRandomNumber(5);
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadMenu.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // submit
        uploadMenu.findElement(By.cssSelector("button")).click();
        // wait and assert new tweet
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article//img[@alt='postImage']"))));

        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article//img[@alt='postImage']")).isDisplayed());
    }

    @Test(testName = "057", enabled = true, priority = 3)
    public void VerifyThereIsAPreviewOfTheImageAfterSelectingIt() {
        String numb = utils.getRandomNumber(5);
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert and assert image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadMenu.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
    }

    @Test(testName = "058", enabled = true, priority = 4)
    public void ValidateRemovingASelectedImage() {
        String numb = utils.getRandomNumber(5);
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert and assert image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadMenu.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // remove and assert
        uploadMenu.findElement(By.xpath("//img[@alt='selectedFile']/parent::div")).click();
        Assert.assertEquals(uploadMenu.findElements(By.cssSelector("img[alt='selectedFile']")).size(), 0, "element still exists");
    }

    @Test(testName = "059", enabled = true, priority = 5)
    public void ValidateUploadingAPostWithTextAfterSelectingRemovingAndSelectingAnImage() {
        String text = utils.getSaltString(14);
        String numb = utils.getRandomNumber(5);
        String numbTwo = numb == "1" ? "2" : "1";
        File uploadFile = new File("src/test/images/postPics/" + numb + ".jpg");
        File uploadFileTwo = new File("src/test/images/postPics/" + numbTwo + ".jpg");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(text);
        uploadMenu.findElement(By.id("forFile")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertTrue(uploadMenu.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // remove image
        uploadMenu.findElement(By.xpath("//img[@alt='selectedFile']/parent::div")).click();
        Assert.assertEquals(uploadMenu.findElements(By.cssSelector("img[alt='selectedFile']")).size(), 0, "element still exists");
        // insert image again
        uploadMenu.findElement(By.id("forFile")).sendKeys(uploadFileTwo.getAbsolutePath());
        Assert.assertTrue(uploadMenu.findElement(By.cssSelector("img[alt='selectedFile']")).isDisplayed());
        // submit
        uploadMenu.findElement(By.cssSelector("button")).click();
        // wait and assert new tweet
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOf(mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article//img[@alt='postImage']"))));

        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']")).isDisplayed());
        Assert.assertTrue(mainContainer.findElement(By.xpath("//p[text()='" + text + "']/ancestor::article//img[@alt='postImage']")).isDisplayed());
    }

    @Test(testName = "060", enabled = true, priority = 6)
    public void ValidateTweetButtonIsDisabledIfNeitherTextOrImageInputted() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // assert button
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        Assert.assertFalse(uploadMenu.findElement(By.cssSelector("button")).isEnabled());
    }

    @Test(testName = "061", enabled = true, priority = 7)
    public void ValidateTweetButtonIsDisabledIfOnlyWhiteSpacesAreInputted() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert white space and assert button
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("main-textarea")).sendKeys(" ");
        Assert.assertFalse(uploadMenu.findElement(By.cssSelector("button")).isEnabled());
    }

    @Test(testName = "062", enabled = false, priority = 8)
    public void ValidateTextboxDoesNotAcceptMoreThan280Chars() {
        String text = utils.getSaltString(281);
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        WebElement textarea = uploadMenu.findElement(By.id("main-textarea"));
        textarea.sendKeys(text);
        Assert.assertTrue(textarea.getText().length() <= 280, "textarea length is: " + textarea.getText().length());
    }

    @Test(testName = "063", enabled = false, priority = 9)
    public void ValidateTweetButtonIsDisabledIfWrongFormatOfFileIsSelected() {
        File uploadFile = new File("src/test/images/wrongFormat/1.pdf");
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // insert text and image
        WebElement uploadMenu = mainContainer.findElement(By.xpath("//textarea/ancestor::div"));
        uploadMenu.findElement(By.id("forFile")).sendKeys(uploadFile.getAbsolutePath());
        // assert button
        Assert.assertFalse(uploadMenu.findElement(By.cssSelector("button")).isEnabled());
    }
}
