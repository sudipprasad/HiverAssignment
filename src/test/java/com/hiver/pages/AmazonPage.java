package com.hiver.pages;

import com.hiver.generics.GenericLibs;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class AmazonPage {
    private WebDriver driver;
    public static final String AMAZON_URL = "https://www.amazon.in/";

    //Locators

    @FindBy(id="twotabsearchtextbox")
    private WebElement searchInput;

    @FindBy(css="span.a-color-state.a-text-bold")
    private WebElement searchListingPage;

    @FindBy(xpath="")
    private WebElement xxxxxx;

    public AmazonPage(String browserType) {
        int browserCode = GenericLibs.getBrowserCode(browserType.toUpperCase());
        driver = GenericLibs.getWebDriverInstance(browserCode);
        driver.get(AMAZON_URL);
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);
        GenericLibs.setWaitTimeFromProps();
    }

    public void searchProduct(String searchTerm){
        GenericLibs.waitForElementToAppear(driver, searchInput);
        searchInput.sendKeys(searchTerm);
        searchInput.sendKeys(Keys.ENTER);

        Assert.assertTrue(searchListingPage.isDisplayed(), "Landing on Search result page");
        String xpath="//a/span[contains(text(), '"+ searchTerm +"')]";
        List<WebElement> searchItemPanel = driver.findElements(By.xpath(xpath));
        if(searchItemPanel.size()>0) {
            System.out.println("PASSED: Searched Item Found!!");
            Assert.assertTrue(true);
        }else{
            System.out.println("FAILED: Searched Item not Found!!");
            Assert.assertTrue(false);
        }

    }

    public int getPriceOfItemFromSearchResult(String itemName){
        String xpath="(//a/span[contains(text(), '"+ itemName +"')])[last()]/../../../../../../..//span[@class='a-price-whole']";
        GenericLibs.waitForElementToAppear(driver, driver.findElement(By.xpath(xpath)));
        return Integer.parseInt(driver.findElement(By.xpath(xpath)).getText().replace(",","").replace("₹",""));
    }

    public void closeBrowser() {
        driver.quit();
    }
}
