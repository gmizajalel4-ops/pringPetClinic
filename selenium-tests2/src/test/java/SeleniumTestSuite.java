import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebElement;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {

    public static void main(String[] args) {
        // Set ChromeDriver path (Optional if not using remote Selenium Grid)
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // Update with your path

        // Set up Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--verbose");

        // Set up RemoteWebDriver (using Selenium Grid)
        WebDriver driver = null;
        try {
            // Connect to the Selenium Grid node
            URL seleniumGridURL = new URL("http://172.17.0.2:4444/wd/hub"); // Change to your grid URL
            driver = new RemoteWebDriver(seleniumGridURL, options);

            // Increase timeouts
            driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            // Open the target URL
            driver.get("http://192.168.49.2:30080"); // Change to your URL

            // Check title for confirmation
            String pageTitle = driver.getTitle();
            System.out.println("Page title is: " + pageTitle);

            // Example: Interact with elements on the page
            WebElement findButton = driver.findElement(By.xpath("//a[@href='/owners/find']"));
            findButton.click();
            System.out.println("Clicked on Find Owners");

            // Wait for the page load (or any other actions as needed)
            Thread.sleep(3000);  // Just an example of waiting, use more robust waits in real tests

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                // Clean up and close the browser
                driver.quit();
            }
        }
    }
}
