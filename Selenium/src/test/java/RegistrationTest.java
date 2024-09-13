import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RegistrationTest {
    WebDriver driver;
    Utils utils = new Utils();

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
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(utils.registerUrl);
    }

    @Test(testName = "003", groups = {"smoke"}, enabled = true, priority = 1)
    public void ValidateSignupWithValidDataInAllFields() throws InterruptedException {
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        Thread.sleep(100);
        Assert.assertEquals(driver.getCurrentUrl(), utils.url,"url is different that the homepage url");
    }

    @Test(testName = "004", groups = {"smoke"}, enabled = true, priority = 2)
    public void ValidateSignupWithCyrillicLettersInFirstLastAndUsername() {
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltStringCyrilic(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltStringCyrilic(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltStringCyrilic(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url,"url is different that the homepage url");
    }

    @Test(testName = "005", enabled = true, priority = 3)
    public void ValidateSignupWith1LetterFirstLastAndUsername() {
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltStringCyrilic(1));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltStringCyrilic(1));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltStringCyrilic(1));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url,"url is different that the homepage url");
    }

    @Test(testName = "006", enabled = true, priority = 4)
    public void ValidateSignupWithUsingUsernameWith24Characters() {
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(24));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        Assert.assertTrue(driver.findElement(By.xpath("//img[@alt='Logo']")).isDisplayed());
        Assert.assertEquals(driver.getCurrentUrl(), utils.url,"url is different that the homepage url");
    }

    @Test(testName = "007", enabled = true, priority = 5)
    public void ValidateUsernameWontAcceptMoreThan24Characters() {
        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(utils.getSaltString(30));
        System.out.println(usernameInput.getAttribute("value").length());
        Assert.assertTrue(usernameInput.getAttribute("value").length() <= 24, "username accepts more than 24 chars");
    }

    @Test(testName = "008", enabled = true, priority = 6)
    public void ValidateSubmitButtonIsDisabledIfAtLeast1FieldIsEmpty() {
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(24));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "009", enabled = true, priority = 7)
    public void ValidateSubmitButtonIsDisabledIfWhiteSpacesAreInputtedInAllTheFields() {
        driver.findElement(By.name("firstName")).sendKeys(" ");
        driver.findElement(By.name("lastName")).sendKeys(" ");
        driver.findElement(By.name("username")).sendKeys(" ");
        driver.findElement(By.name("email")).sendKeys(" ");
        driver.findElement(By.name("password")).sendKeys(" ");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "010", enabled = true, priority = 8)
    public void VerifyEmailPlaceholderIsEmailAddressCom() {
        String placeholder = "email@address.com";
        WebElement emailInput = driver.findElement(By.name("email"));
        Assert.assertEquals(emailInput.getAttribute("placeholder"), placeholder, "placeholder is not the required one");
    }

    @Test(testName = "011", groups = {"invalidTc"}, enabled = true, priority = 9)
    public void ValidateErrorMessageIfUsernameIsAlreadyInUse() {
        String errMsg = "Username is already in use";
        String usernameInUse = "dummies";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(usernameInUse);
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "012", groups = {"invalidTc"}, enabled = true, priority = 10)
    public void ValidateErrorMessageIfEmailIsAlreadyInUse() {
        String errMsg = "Email is already in use";
        String emailInUse = "dummy@dummies.com";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(emailInUse);
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "013", groups = {"invalidTc"}, enabled = true, priority = 11)
    public void ValidateErrorMessageIfPasswordIsLessThan8Chars() {
        String errMsg = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        String badPass = "Pass123";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys(badPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "014", groups = {"invalidTc"}, enabled = true, priority = 12)
    public void ValidateErrorMessageIfPasswordDoesNotIncludeLowercaseLetters() {
        String errMsg = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        String badPass = "PASSWORD123";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys(badPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "015", groups = {"invalidTc"}, enabled = true, priority = 13)
    public void ValidateErrorMessageIfPasswordDoesNotIncludeUppercaseLetters() {
        String errMsg = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        String badPass = "password123";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys(badPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "016", groups = {"invalidTc"}, enabled = true, priority = 14)
    public void ValidateErrorMessageIfPasswordDoesNotIncludeNumbers() {
        String errMsg = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        String badPass = "Password";
        driver.findElement(By.name("firstName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("lastName")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("username")).sendKeys(utils.getSaltString(10));
        driver.findElement(By.name("email")).sendKeys(utils.getSaltString(10) + "@gmail.com");
        driver.findElement(By.name("password")).sendKeys(badPass);
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement error = driver.findElement(By.cssSelector("p.text-red-500"));
        Assert.assertTrue(error.isDisplayed());
        Assert.assertEquals(error.getText(), errMsg, "err message is not the same");
    }

    @Test(testName = "017", groups = {"smoke"}, enabled = true, priority = 15)
    public void ValidateLinkToSwitchToLogInIsVisible() {
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Log In']")).isDisplayed());
    }

    @Test(testName = "018", groups = {"smoke"}, enabled = true, priority = 16)
    public void ValidateLinkToSwitchToLogInSwitchesToLoginForm() {
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Sign Up']")).isDisplayed());
    }
}
