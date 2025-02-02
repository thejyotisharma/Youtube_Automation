package demo;

import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class TestCases extends ExcelDataProvider {
    ChromeDriver driver;
    Wrappers wrapper;

    //To Assert the correct URL, Click on "About" at the bottom of the sidebar, and print the message on the screen.
    @Test
    public void testCase01() {
        String expectedUrl = "https://www.youtube.com/";
        String actualUrl = driver.getCurrentUrl();
        System.out.println(actualUrl);
        // Assert that the current URL matches the expected URL
        Assert.assertEquals(expectedUrl, actualUrl, "Failed to verify url");
        wrapper.waitandscrollToElement(By.xpath("//a[@href='https://www.youtube.com/about/']"));
        wrapper.clickElement(By.xpath("//a[@href='https://www.youtube.com/about/']"));
        wrapper.waitandscrollToElement(By.xpath("//section[@class='ytabout__content']"));
        // Retrieve and print the text content from the About page
        String text = wrapper.getText(By.xpath("//section[@class='ytabout__content']"));
        System.out.println(text);
    }

    //To Assert on whether the movie is marked A for Mature or not. Apply a Soft assert on the movie category to check if it exists.
    @Test
    public void testCase02() throws InterruptedException {
        driver.get("https://www.youtube.com/");
        // Click on the 'Movies' section link on the homepage
        wrapper.clickElement(By.xpath("//a[@id='endpoint' and @title='Movies']"));
        System.out.println("Movies page opened");
        String rightArrowXPath = "//span[text()='Top selling']//ancestor::div[@id='dismissible']/div[2]//div[@id='right-arrow']//button[@aria-label='Next']";
        WebElement element = wrapper.waitForElement(By.xpath(rightArrowXPath));

        // Click the "Next" button while it's visible and enabled to scroll through the movie list
        while (element != null && element.isDisplayed() && element.isEnabled()) {
            System.out.println("Arrow clicked");
            wrapper.clickElement(By.xpath(rightArrowXPath));
            Thread.sleep(1000);
        }
        String movieRating = wrapper.getText(By.xpath("(//ytd-grid-movie-renderer)[last()]//ytd-badge-supported-renderer[1]//div[last()]"));

        // Check if the movie rating is "A" (Mature)
        if (movieRating.equals("A")) {
            System.out.println("Pass: Movie is A marked");
        } else {
            System.out.println("Fail: Movie is marked " + movieRating);
        }

        // Get the metadata (category) of the last movie in the list
        String movieMetadata = wrapper.getText(By.xpath("(//ytd-grid-movie-renderer)[last()]//a[contains(@class,'ytd-grid-movie-renderer')]/span"));

        // Check if the movie category exists and print the result
        if (movieMetadata.length() > 0) {
            System.out.println("Pass: Movie category exists: " + movieMetadata);
        } else {
            System.out.println("Fail: Movie category not found");
        }
    }

    //Verify that the Music page loads correctly, and the title count for the playlist is less than or equal to 50.
    @Test
    public void testCase03() {
        wrapper.clickElement(By.xpath("//a[@id='endpoint' and @title='Music']"));
        System.out.println("Music page opened");
        By showMoreButton = By.xpath("(//button[@aria-label='Show more'])[last()]");

        // Wait for and scroll to the 'Show more' button, then click it
        wrapper.waitandscrollToElement(showMoreButton);
        wrapper.clickElement(showMoreButton);

        // Get the name of the last playlist on the page
        String playlistName = wrapper.getText(By.xpath("((//div[contains(@class,'yt-lockup-view-model-wiz--compact')][last()])//a/span)[last()]"));
        System.out.println("playlist: " + playlistName);

        // Get the title count of the last playlist
        String titleCount = wrapper.getText(By.xpath("((//div[contains(@class,'yt-lockup-view-model-wiz--compact')][last()])//div[@class='badge-shape-wiz__text'])[last()]"));
        System.out.println("titleCount: " + titleCount);

        // Check if the title count is less than or equal to 50
        if (Integer.parseInt(titleCount.split(" ")[0]) <= 50) {
            System.out.println("pass: Title count is less than 50");
        } else {
            System.out.println("fail: Title count is greater than 50");
        }
    }

    //Verify that the total like count for the first 3 news posts is correctly accumulated.
    @Test
    public void testCase04() {
        wrapper.clickElement(By.xpath("//a[@id='endpoint' and @title='News']"));
        System.out.println("News page opened");
        int likesCount = 0;
        for (int i = 1; i <= 3; i++) {
            // Get the title of the i-th news post
            String titleXpath = "(//span[text()='Latest news posts']//ancestor::div[5])//div[@id='contents-container']/div//ytd-rich-item-renderer[" + i + "]//div[@id='body']";
            System.out.println("Title for " + i + " news is:" + wrapper.getText(By.xpath(titleXpath)));

            // Get the like count for the i-th news post
            String likesXpath = "(//span[text()='Latest news posts']//ancestor::div[5])//div[@id='contents-container']/div//ytd-rich-item-renderer[" + i + "]//span[@id='vote-count-middle']";
            String likesString = wrapper.getText(By.xpath(likesXpath));

            // If there is a like count, add it to the total likes count
            if (!likesString.equals("")) {
                likesCount += Integer.parseInt(likesString);
            }
        }
        System.out.println("Total like count: " + likesCount);
    }

    //Search for a key, and accumulate total likes based on the view count of the videos in the search results.
    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void testCases05(String key) throws InterruptedException {
        Thread.sleep(1000);
        String textboxXpath = "(//input[@name='search_query' and @type='text'])[1]";
        wrapper.waitandscrollToElement(By.xpath(textboxXpath));
        Thread.sleep(2000);
        wrapper.sendKeys(By.xpath(textboxXpath), key);
        Thread.sleep(1000);
        wrapper.sendKeys(By.xpath(textboxXpath), Keys.ENTER);
        int lk = 0, i = 1;
        while (lk < 100000000) {
            // XPath to get the view count for the i-th video
            String viewsSpanXpath = "(//span[contains(@class, 'ytd-video-meta-block') and contains(text(), 'views')])[" + i + "]";
            wrapper.waitandscrollToElement(By.xpath(viewsSpanXpath));
            String count = wrapper.getText(By.xpath(viewsSpanXpath)).split(" ")[0];

            // Check if the count is in millions (M), thousands (K), or regular format
            if (count.endsWith("M")) {
                Float num = Float.parseFloat(count.replace("M", ""));
                lk = (int) (lk + (num * 1000000));  // Convert millions to the full number
            } else if (count.endsWith("K")) {
                Float num = Float.parseFloat(count.replace("K", ""));
                lk = (int) (lk + (num * 1000));  // Convert thousands to the full number
            } else if (!count.equals("")) {
                int num = Integer.parseInt(count);
                lk = lk + num;
            }
            System.out.println("Likes are: " + lk);
            i = i + 1;
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