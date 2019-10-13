package com.hiver.pages;

import com.hiver.generics.GenericLibs;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FlipkartPage {
    private WebDriver driver;
    public static final String FLIPKART_URL = "https://www.flipkart.com/";

    //Locators

    @FindBy(xpath="//img[@alt='Flipkart']")
    private WebElement logo;

    @FindBy(css="._2AkmmA._29YdH8")
    private WebElement loginPopupCloseBtn;

    @FindBy(xpath = "//span[text()= 'Electronics']")
    private WebElement electronicMenu;

    @FindBy(css ="a[title='Pixel 3a | 3a XL']")
    private WebElement pixelSubMenu;

    @FindBy(xpath = "(//a[@rel='noopener noreferrer'])[1]")
    private WebElement firstPixelPhone;

    @FindBy(xpath = "//button[text()='GO TO CART']")
    private WebElement goToCartBtn;

    @FindBy(xpath = "//button[text()='ADD TO CART']")
    private WebElement addToCartBtn;

    @FindBy(xpath = "(//button[text()='+'])[1]")
    private WebElement plusBtnInCart;

    @FindBy(xpath = "(//div[@class='PaJLWc']/div/div)[1]")
    private WebElement itemOnCartDisplayNameLbl;

    @FindBy(xpath = "//div[@class='PaJLWc']")
    private List<WebElement> noOfItemsInCartLyt;

    @FindBy(xpath = "(//span[@class='pMSy0p XU9vZa'])[1]")
    private WebElement firstItemPriceInCartLbl;

    @FindBy(xpath = "(//div[@class='_3wU53n'])[1]")
    private WebElement firstItemNameInProdListPage;

    @FindBy(xpath = "((//div[@class='_1uv9Cb'])[1]/div)[1]")
    private WebElement firstItemPriceInProdListPage;

    @FindBy(xpath = "//div[starts-with(text(),'Price')]/../span")
    private WebElement productPriceInProductDetailsSection;

    @FindBy(xpath = "//div[text()='Delivery']/../span/span")
    private WebElement deliveryChargeLbl;

    @FindBy(xpath = "//div[text()='Total Payable']/../span")
    private WebElement totalPayablePriceLbl;

    @FindBy(xpath = "//h1[text()='Pixel 3A Series']")
    private WebElement pixelListingPageHeading;

    @FindBy(xpath = "//div[starts-with(text(),'My Cart')]")
    private WebElement cartHeading;

    @FindBy(xpath = "(//div[@class='_2zH4zg'])[1]/input")
    private WebElement noOfItemFirstInput;

    @FindBy(xpath = "//input[@title = 'Search for products, brands and more']")
    private WebElement mainSearchInput;

    @FindBy(xpath = "//*[contains(text(),'Showing 1 –')]")
    private WebElement searchListingPage;
    // Constructor
    public FlipkartPage(String browserType){
        int browserCode = GenericLibs.getBrowserCode(browserType.toUpperCase());
        driver = GenericLibs.getWebDriverInstance(browserCode);
        driver.get(FLIPKART_URL);
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);
        GenericLibs.setWaitTimeFromProps();
        if(loginPopupCloseBtn.isDisplayed()) loginPopupCloseBtn.click();
    }

    public void navigateToHomePage() {
        logo.click();
        driver.manage().timeouts().pageLoadTimeout(GenericLibs.LONG_WAIT, TimeUnit.SECONDS);
    }

    public void searchProduct(String searchTerm){
        GenericLibs.waitForElementToAppear(driver, mainSearchInput);
        mainSearchInput.sendKeys(searchTerm);
        mainSearchInput.sendKeys(Keys.ENTER);

        GenericLibs.waitForElementToAppear(driver, searchListingPage);
        Assert.assertTrue(searchListingPage.isDisplayed(), "Landing on Search result page");
        String xpath="//div[contains(text(), '"+ searchTerm +"')]";
        List<WebElement> searchItemPanel = driver.findElements(By.xpath(xpath));
        if(searchItemPanel.size()>0) {
            System.out.println("PASSED: Searched Item Found!!");
            Assert.assertTrue(true);
        }else{
            System.out.println("FAILED: Searched Item not Found!!");
            Assert.assertTrue(false);
        }
    }

    public void navigateToPixelSubMenu() {
        Actions action = new Actions(driver);
        action.moveToElement(electronicMenu);
        action.build().perform();
        GenericLibs.waitForElementToAppear(driver, pixelSubMenu);
        pixelSubMenu.click();
        GenericLibs.waitForElementToAppear(driver, pixelListingPageHeading);
        Assert.assertTrue(pixelListingPageHeading.isDisplayed(), "Landed on Pixel Listing Page");
    }

    public String getFirstProductNameFromListing() {
        return firstItemNameInProdListPage.getText().trim();
    }

    public int getFirstProductPriceFromListing() {
        return Integer.parseInt(firstItemPriceInProdListPage
                .getText().trim().replace(",","").replace("₹",""));
    }

    public String getFirstProductNameFromCart() {
        return itemOnCartDisplayNameLbl.getText().trim();
    }

    public int getFirstProductPriceFromCart() {
        return Integer.parseInt(firstItemPriceInCartLbl.getText().trim().replace(",","").replace("₹",""));
    }

    public void clickOnFirstProductInListingPage() {
        firstPixelPhone.click();
    }

    public void addProductToCart() {
        GenericLibs.waitForElementToAppear(driver, addToCartBtn);
        addToCartBtn.click();
    }

    public void goProductToCart() {
        try{
            GenericLibs.waitForElementToAppear(driver, cartHeading);
            cartHeading.isDisplayed();
        }catch (NoSuchElementException e){
            GenericLibs.waitForElementToAppear(driver, cartHeading);
            Assert.assertTrue(noOfItemsInCartLyt.size()==1, "There should be only 1 item in the cart");
            GenericLibs.waitForElementToAppear(driver, goToCartBtn);
            goToCartBtn.click();
            GenericLibs.waitForElementToAppear(driver, cartHeading);
        }
    }

    public void clickAndValidatePlusBtnInCart() {
        int productPrice = getFirstProductPriceFromCart();
        int noOfItem = Integer.parseInt(noOfItemFirstInput.getAttribute("value").trim());
        plusBtnInCart.click();
        try{Thread.sleep(2000);}catch (InterruptedException e){}
        int updatedNoOfItem = Integer.parseInt(noOfItemFirstInput.getAttribute("value").trim());
        Assert.assertTrue((noOfItem+1)==updatedNoOfItem, "Cart Add Button Functionality");

        int updatedProductPrice = getFirstProductPriceFromCart();
        Assert.assertTrue((productPrice*updatedNoOfItem)==updatedProductPrice, "Price after add button");
    }

    public int validateProductDetailsSection() {
        int productPricePDSection = Integer.parseInt(productPriceInProductDetailsSection.getText().trim().replace(",","").replace("₹",""));
        int deliveryCharge=0;
        try{
            deliveryCharge = Integer.parseInt(deliveryChargeLbl.getText().trim().replace(",","").replace("₹",""));
        }catch (Exception e){}
        int totalPayable = Integer.parseInt(totalPayablePriceLbl.getText().trim().replace(",","").replace("₹",""));

        Assert.assertTrue((productPricePDSection+deliveryCharge)==totalPayable, "Total Price Calculation");

        return totalPayable;
    }

    public void closeBrowser() {
        driver.quit();
    }

    public void switchReferenceToNewTab() {
        Set<String> windowHandle = driver.getWindowHandles();
        Iterator<String> it = windowHandle.iterator();
        it.next();
        String nextTab = it.next();
        driver.switchTo().window(nextTab);
    }

    public int getProductPriceFromSearchListingPage(String itemName) {
        String xpath="((//div[contains(text(), '"+ itemName +"')]/../../div)[last()]/div/div/div)[1]";
        return Integer.parseInt(driver.findElement(By.xpath(xpath)).getText().replace(",","").replace("₹",""));
    }
}
