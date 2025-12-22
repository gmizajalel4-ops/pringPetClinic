import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SeleniumTestSuite {

    WebDriver driver;

    @BeforeClass
    public void setup() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--headless",
            "--no-sandbox",
            "--disable-dev-shm-usage"
        );

        driver = new RemoteWebDriver(
            new URL("http://localhost:4444/wd/hub"),
            options
        );

        // Set the page load timeout to 3 minutes
        driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);

        // Set the implicit wait timeout for elements to 30 seconds
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void openHomePage() {
        driver.get("http://192.168.49.2:30080");
        
        // Adding a condition to wait for the page to load and assert the title
        Assert.assertTrue(
            driver.getTitle().contains("PetClinic"),
            "Title does not contain PetClinic"
        );
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
