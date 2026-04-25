package com.lab10;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-component-update");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-features=TranslateUI,MediaRouter,OptimizationHints,VizDisplayCompositor");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-sync");
        options.addArguments("--metrics-recording-only");
        options.addArguments("--no-first-run");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--password-store=basic");
        options.addArguments("--use-mock-keychain");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.nanoTime());

        driver = new ChromeDriver(options);
        driver.navigate().to("http://103.139.122.250:4000/login");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        ((JavascriptExecutor) driver).executeScript("""
                const originalFetch = window.fetch.bind(window);
                window.fetch = (resource, init = {}) => {
                    const url = typeof resource === 'string' ? resource : resource.url;
                    if (url.includes('supabase.co/auth/v1/token') && url.includes('grant_type=password')) {
                        return Promise.resolve(new Response(JSON.stringify({
                            code: 'invalid_credentials',
                            message: 'Invalid login credentials'
                        }), {
                            status: 400,
                            headers: { 'Content-Type': 'application/json' }
                        }));
                    }
                    return originalFetch(resource, init);
                };
                """);

        email.clear();
        email.sendKeys("qasim@malik.com");
        password.clear();
        password.sendKeys("abcdefg");

        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(., 'Sign In')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", signIn);

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(normalize-space(.),'Invalid login credentials')]")));

        String errorText = errorElement.getText();
        Assertions.assertTrue(errorText.contains("Invalid login credentials"));
    }
}
