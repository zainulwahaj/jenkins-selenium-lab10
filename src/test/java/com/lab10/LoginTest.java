package com.lab10;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class LoginTest {
    private WebDriver driver;

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginWithIncorrectCredentials() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-debugging-port=0");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data");

        driver = new ChromeDriver(options);
        driver.navigate().to("http://103.139.122.250:4000/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        email.clear();
        email.sendKeys("qasim@malik.com");
        password.clear();
        password.sendKeys("abcdefg");

        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(., 'Sign In')]")));
        signIn.click();

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(normalize-space(.),'Incorrect email or password')]")));

        String errorText = errorElement.getText();
        Assertions.assertTrue(errorText.contains("Incorrect email or password"));
    }
}
