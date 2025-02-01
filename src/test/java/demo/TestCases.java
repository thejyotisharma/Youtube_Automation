package demo;

import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.logging.Level;

public class TestCases extends ExcelDataProvider { // Lets us read the data
    ChromeDriver driver;
    Wrappers wrapper;

    @Test
    public void testCase01() {
        String expectedUrl = "https://www.youtube.com/";
        String actualUrl = driver.getCurrentUrl();
        System.out.println(actualUrl);
        Assert.assertEquals(expectedUrl, actualUrl, "Failed to verify url");
        wrapper.waitandscrollToElement(By.xpath("//a[@href='https://www.youtube.com/about/']"));
        wrapper.clickElement(By.xpath("//a[@href='https://www.youtube.com/about/']"));
        wrapper.waitandscrollToElement(By.xpath("//section[@class='ytabout__content']"));
        String text = wrapper.getText(By.xpath("//section[@class='ytabout__content']"));
        System.out.println(text);
    }

    @Test
    public void testCase02() throws InterruptedException {
        driver.get("https://www.youtube.com/");

        wrapper.clickElement(By.xpath("//a[@id='endpoint' and @title='Movies']"));
        System.out.println("Movies page opened");
        String rightArrowXPath = "//span[text()='Top selling']//ancestor::div[@id='dismissible']/div[2]//div[@id='right-arrow']//button[@aria-label='Next']";
        WebElement element = wrapper.waitForElement(By.xpath(rightArrowXPath));

        while (element != null && element.isDisplayed() && element.isEnabled()) {
            System.out.println("Arrow clicked");
            wrapper.clickElement(By.xpath(rightArrowXPath));
            Thread.sleep(1000);
        }
        String movieRating = wrapper.getText(By.xpath("(//ytd-grid-movie-renderer)[last()]//ytd-badge-supported-renderer[1]//div[last()]"));
        if (movieRating.equals("A")) {
            System.out.println("Pass: Movie is A marked");
        } else {
            System.out.println("Fail: Movie is marked " + movieRating);
        }

        String movieMetadata = wrapper.getText(By.xpath("(//ytd-grid-movie-renderer)[last()]//a[contains(@class,'ytd-grid-movie-renderer')]/span"));
        if (movieMetadata.length() > 0) {
            System.out.println("Pass: Movie category exists: " + movieMetadata);
        } else {
            System.out.println("Fail: Movie category not found");
        }
    }

    @Test
    public void testCases03() {
        wrapper.clickElement(By.xpath("//a[@id='endpoint' and @title='Music']"));
        System.out.println("Music page opened");

        By showMoreButton = By.xpath("(//button[@aria-label='Show more'])[last()]");
        wrapper.waitandscrollToElement(showMoreButton);
        wrapper.clickElement(showMoreButton);

        String playlistName = wrapper.getText(By.xpath("((//div[contains(@class,'yt-lockup-view-model-wiz--compact')][last()])//a/span)[last()]"));
        System.out.println("playlist: "+playlistName);

        String titleCount = wrapper.getText(By.xpath("((//div[contains(@class,'yt-lockup-view-model-wiz--compact')][last()])//div[@class='badge-shape-wiz__text'])[last()]"));
        System.out.println("titleCount: "+titleCount);
        if (Integer.parseInt(titleCount.split(" ")[0]) <= 50){
            System.out.println("pass: Title count is less than 50");
        }else{
            System.out.println("fail: Title count is greater than 50");
        }
    }

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.get("https://www.youtube.com/");
        wrapper = new Wrappers(driver);
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}