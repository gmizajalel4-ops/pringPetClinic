import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.net.URL;

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
    }

    @Test
    public void openHomePage() {
        driver.get("http://192.168.49.2:30080");
        assert driver.getTitle().contains("PetClinic");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
