package com.hiver.generics;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GenericLibs {
    public static final int TEST_CONFIG_PROPS = 100;
    public static final int TEST_DATA_PROPS = 101;
    public static final int TEST_OUTPUT_PROPS = 102;

    public static long SHORT_WAIT = 10;
    public static long LONG_WAIT = 20;
    public static long VERY_LONG_WAIT = 30;

    public static int getBrowserCode(String btype){
        return Integer.parseInt(getValueFromPropertyFile(TEST_CONFIG_PROPS, btype));
    }

    public static String getTestData(String key){
        return getValueFromPropertyFile(TEST_DATA_PROPS, key);
    }


    public static String getValueFromPropertyFile(int props_code, String key){

        try (InputStream input = new FileInputStream(getPropertyFilePath(props_code))) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            return prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String getPropertyFilePath(int code) {
        String prop_path = GenericLibs.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "propertiesFiles/";
        switch (code){
            case 101:
                prop_path = prop_path + "testData.properties";
                break;
            case 102:
                prop_path = prop_path + "testOutput.properties";
                break;
            case 100:
            default:
                prop_path = prop_path + "testConfig.properties";
        }
        return prop_path;
    }

    public static WebDriver getWebDriverInstance(int browserCode) {
        WebDriver driver;

        switch (browserCode){
            case 0:
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case 2:
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
                break;
            case 1:
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        return driver;
    }

    public static void waitForElementToAppear(WebDriver driver, WebElement webElement) {
        WebDriverWait wait = new WebDriverWait(driver, VERY_LONG_WAIT, 500);
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }


    public static void setWaitTimeFromProps() {
        SHORT_WAIT = Integer.parseInt(GenericLibs.getValueFromPropertyFile(GenericLibs.TEST_CONFIG_PROPS, "short_wait"));
        LONG_WAIT = Integer.parseInt(GenericLibs.getValueFromPropertyFile(GenericLibs.TEST_CONFIG_PROPS, "long_wait"));
        VERY_LONG_WAIT = Integer.parseInt(GenericLibs.getValueFromPropertyFile(GenericLibs.TEST_CONFIG_PROPS, "very_long_wait"));
    }

}
