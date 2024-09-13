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

public class SubmenuTest {
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
//        login();
//        driver.manage().window().setSize(new Dimension(500, 800));
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){ driver.get(utils.url); }

    @Test(testName = "051", enabled = true, priority = 1)
    public void ValidateChoosingTheUploadProfilePictureClosesTheSubmenu() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // click the sidebar btn
        mainContainer.findElement(By.id("sidebarButton")).click();
        // wait for sidebar to open and assert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        WebElement sidebar = driver.findElement(By.cssSelector("aside.translate-x-0"));
        Assert.assertTrue(sidebar.isDisplayed());
        // click the upload profile pic button
        sidebar.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // assert if sidebar closed
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        Assert.assertFalse(driver.findElement(By.cssSelector("aside h3")).isDisplayed());
    }

    @Test(testName = "052", enabled = true, priority = 2)
    public void ValidateChoosingTheUploadProfilePictureOpensTheUploadProfilePictureContainer() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // click the sidebar btn
        mainContainer.findElement(By.id("sidebarButton")).click();
        // wait for sidebar to open and assert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        WebElement sidebar = driver.findElement(By.cssSelector("aside.translate-x-0"));
        Assert.assertTrue(sidebar.isDisplayed());
        // click the upload profile pic button
        sidebar.findElement(By.xpath("//button[text()='Upload profile picture']")).click();
        // wait for sidebar to close and assert
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        Assert.assertFalse(driver.findElement(By.cssSelector("aside h3")).isDisplayed());
        // assert if upload pic container is open
        Assert.assertTrue(driver.findElement(By.xpath("//button[text()='Upload']/ancestor::aside")).isDisplayed());
    }

    @Test(testName = "053", enabled = true, priority = 3)
    public void ValidateClosingTheSubmenuWithTheCloseButton() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // click the sidebar btn
        mainContainer.findElement(By.id("sidebarButton")).click();
        // wait for sidebar to open and assert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        WebElement sidebar = driver.findElement(By.cssSelector("aside.translate-x-0"));
        Assert.assertTrue(sidebar.isDisplayed());
        // click the upload profile pic button
        sidebar.findElements(By.cssSelector("button")).getFirst().click();
        // wait for sidebar to close and assert
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        Assert.assertFalse(driver.findElement(By.cssSelector("aside h3")).isDisplayed());
    }

    @Test(testName = "054", enabled = true, priority = 4)
    public void ValidateTheSignOut() {
        WebElement mainContainer = driver.findElement(By.cssSelector("section.flex-1"));
        // click the sidebar btn
        mainContainer.findElement(By.id("sidebarButton")).click();
        // wait for sidebar to open and assert
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("aside h3"))));
        WebElement sidebar = driver.findElement(By.cssSelector("aside.translate-x-0"));
        Assert.assertTrue(sidebar.isDisplayed());
        // click the upload profile pic button
        sidebar.findElement(By.xpath("//button[text()='Sign Out']")).click();
        // wait for sidebar to close and assert
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Log In']")).isDisplayed());
    }
}
