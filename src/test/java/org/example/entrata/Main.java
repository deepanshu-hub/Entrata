package org.example.entrata;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class Main {

    private WebDriver driver;

    // Contains data of different screen sizes.
    @DataProvider
    public Object[][] mobileEmulations() {
        return new Object[][]{
                {"iPad Pro", 420, 600},
                {"Nexus 5", 360, 640},
                {"iPhone X", 375, 812},
                {"Pixel 2", 411, 731}
        };
    }

    // Test for responsive design
    @Test(dataProvider = "mobileEmulations")
    void testResponsiveDesign(String emulation, int h, int w) {
         Map<String, String> deviceMobEmu = new HashMap<>();
         deviceMobEmu.put("deviceName", emulation);

         // Setting up chrome browser
        ChromeOptions chromeOpt = new ChromeOptions();
        chromeOpt.setExperimentalOption("mobileEmulation", deviceMobEmu);

        // Creating driver object
        driver = new ChromeDriver(chromeOpt);
         Dimension d = new Dimension(w, h);
        driver.manage().window().setSize(d);
        driver.get("https://www.entrata.com/");
    }

    // Test for error message in signup
    @Test
    public void testSignUpError() {
    ChromeOptions chromeOpt = new ChromeOptions();
    driver = new ChromeDriver(chromeOpt);

    driver.get("https://sso.entrata.com/entrata/login");
    driver.findElement(By.xpath("//*[@id=\"fsm_request_demo\"]/ul/li[3]/button")).click();

    String error = driver.findElement(By.xpath("//*[@id=\"entrata-login-error\"]")).getText();
    Assert.assertEquals(error,"Please enter username and password");
    }

    // Test for Video playback
    @Test
    public void testVideoPlayback() {
        ChromeOptions chromeOpt = new ChromeOptions();
        driver = new ChromeDriver(chromeOpt);
        driver.get("https://www.entrata.com/");

      WebElement videoElement =  driver.findElement(By.linkText("What is Entrata?"));

      // Using Action class to click on video.
        Actions actions = new Actions(driver);
        actions.moveToElement(videoElement).click().perform();

        Assert.assertTrue(videoElement.isDisplayed(), "Video is not displayed.");

        // Play the video
        playVideo(videoElement);

       //  Wait for some time
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    //     Pause the video
        pauseVideo(videoElement);

        // Verify that the video has been paused
        Assert.assertFalse(!isVideoPlaying(videoElement), "Video is not paused.");

    }

    // Play the video
    private void playVideo(WebElement videoElement) {
        if (!isVideoPlaying(videoElement)) {
            Actions actions = new Actions(driver);
            actions.moveToElement(videoElement).click().perform();
        }
    }

    // Pause the video
    private void pauseVideo(WebElement videoElement) {
        if (isVideoPlaying(videoElement)) {
            Actions actions = new Actions(driver);
            actions.moveToElement(videoElement).click().perform();
        }
    }

    // Checking Video is playing or paused
    private boolean isVideoPlaying(WebElement videoElement) {
        // Check the "paused" attribute of the video element
        return !Boolean.parseBoolean(videoElement.getAttribute("paused"));
    }

}