import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import java.time.Duration;

public class CompatibilityTest {
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
        driver.manage().window().maximize();
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//        driver.manage().window().maximize();
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(utils.registerUrl);
    }

    @Test(testName = "111", enabled = true, description = "Run all the tests using Chrome")
    public void TestUsingChrome() {
        System.out.println("Run all the tests using Chrome");
    }

    @Test(testName = "112", enabled = true, description = "Run all the tests using Firefox")
    public void TestUsingFirefox() {
        System.out.println("Run all the tests using Firefox");
    }

    @Test(testName = "113", enabled = true, description = "Run all the tests using Safari")
    public void TestUsingSafari() {
        System.out.println("Run all the tests using Safari");
    }
}
