package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumTest {
    public static void main(String[] args) {
        // Automatically download and set up geckodriver
        WebDriverManager.firefoxdriver().setup();

        // Create a new instance of the Firefox driver
        WebDriver driver = new FirefoxDriver();

        // Open the login page
        driver.get("http://student-frontend.s3-website.ap-south-1.amazonaws.com");

        // Wait for the page to load and elements to be ready
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1. Submit with empty fields
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Login']"))); // Button to click
            loginButton.click();

            // Check for empty field error
            String errorMessage = getErrorMessage(driver);
            if (errorMessage != null) {
                System.out.println("Error on empty fields: " + errorMessage);
            } else {
                System.out.println("No error on empty fields.");
            }

            // 2. Enter email only (no password)
            WebElement emailField = driver.findElement(By.id("mui-1")); // Email input field
            WebElement passwordField = driver.findElement(By.id("mui-2")); // Password input field

            emailField.clear();
            passwordField.clear();
            emailField.sendKeys("admin@edtech.com");
            passwordField.sendKeys(""); // No password provided
            loginButton.click();

            // Check for missing password error
            errorMessage = getErrorMessage(driver);
            if (errorMessage != null) {
                System.out.println("Error with missing password: " + errorMessage);
            } else {
                System.out.println("No error with missing password.");
            }

            // 3. Enter incorrect password
            emailField.clear();
            passwordField.clear();
            emailField.sendKeys("admin@edtech.com");
            passwordField.sendKeys("incorrectpassword");
            loginButton.click();

            // Check for incorrect password error
            errorMessage = getErrorMessage(driver);
            if (errorMessage != null) {
                System.out.println("Error with incorrect password: " + errorMessage);
            } else {
                System.out.println("No error with incorrect password.");
            }

            // 4. Enter correct credentials
            emailField.clear();
            passwordField.clear();
            emailField.sendKeys("admin@edtech.com");
            passwordField.sendKeys("edtech"); // Correct password
            loginButton.click();

            // Wait for successful login and assert
            wait.until(ExpectedConditions.urlContains("dashboard"));
            System.out.println("Login successful with correct credentials.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser after completing all the steps
            driver.quit();
        }
    }

    // Helper method to get error messages from the page (you can adjust the XPath based on your page structure)
    private static String getErrorMessage(WebDriver driver) {
        try {
            // Example of getting error message if present
            WebElement errorElement = driver.findElement(By.xpath("//div[@role='alert']")); // Assuming error messages are in alert divs
            if (errorElement.isDisplayed()) {
                return errorElement.getText(); // Return the text of the error message
            }
        } catch (Exception e) {
            // No error message found
        }
        return null; // No error message
    }
}
