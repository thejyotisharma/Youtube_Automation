package demo.wrappers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Wrappers {
    private WebDriver driver;
    public WebDriverWait wait;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void waitandscrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getText(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return element.getText();
    }

    public WebElement clickElement(By locator) {
        waitForElement(locator);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
       element.click();
        return element;
    }

    public void sendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void sendKeys(By locator, Keys text) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        element.sendKeys(text);
    }
}

