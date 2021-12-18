package com.example.seleniumtodomvctest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tests {
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait waitVar;

    @BeforeAll
    public static void initialize() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void prepareDriver(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));
        waitVar = new WebDriverWait(driver, 40);
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void todoTestCase() throws InterruptedException {
        driver.get("https://todomvc.com");
        Thread.sleep(1500);
        choosePlatform("Backbone.js");
        Thread.sleep(1500);
        addTodo("Walk the dog");
        addTodo("Do homework");
        addTodo("Go to party");
        Thread.sleep(1500);
        removeTodo();
        Thread.sleep(1500);
        assertLeft(2);

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "AngularJS",
            "React",
            "Dart"})
    public void todoParameterizedTestCase(String platform) throws InterruptedException, IOException {
        driver.get("https://todomvc.com");
        Thread.sleep(1500);
        choosePlatform(platform);
        System.out.println("choose platform: success");
        Thread.sleep(1500);
        addTodo("Walk the dog");
        addTodo("Do homework");
        addTodo("Go to party");
        System.out.println("add todos: success");
        Thread.sleep(1500);
        removeTodo();
        System.out.println("remove todo: success");
        Thread.sleep(1500);
        assertLeft(2);
        System.out.println("Result verification: success");
        screenshot(platform);

    }

    private void choosePlatform(String platform) {
        waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(platform)));
        WebElement chosenPlatform = driver.findElement(By.linkText(platform));
        chosenPlatform.click();
    }
    private void addTodo(String todo) {
        waitVar.until(ExpectedConditions.visibilityOfElementLocated(By.className("new-todo")));
        WebElement todoEl = driver.findElement(By.className("new-todo"));
        todoEl.sendKeys(todo);
        todoEl.sendKeys(Keys.RETURN);
    }
    private void removeTodo() {
        WebElement todoEl = driver.findElement(By.cssSelector("li:nth-child(2) .toggle"));
        todoEl.click();
    }
    private void assertLeft(int exp) {
        WebElement todosCount = driver.findElement(By.xpath("//footer/*/span/strong | //footer/span/strong "));
        System.out.println(todosCount);
        ExpectedConditions.textToBePresentInElement(todosCount, Integer.toString(exp));
    }
    public void screenshot(String platform) throws IOException {
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("./src/main/resources/Screenshots/"+platform+".png"));
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
