package com.hiver.tests;

import com.hiver.generics.GenericLibs;
import com.hiver.pages.AmazonPage;
import com.hiver.pages.FlipkartPage;
import org.testng.annotations.*;

public class Assignment2_Amazon {
    AmazonPage amazonPage;
    String browserType = "CHROME";
    @BeforeClass
    @Parameters({"browser.type"})
    public void TestSetup(String browser){
        browserType = browser;
        amazonPage = new AmazonPage(browser);
        System.out.println("Test Setup done!!");
    }

    @AfterClass
    public void cleanUp(){
        amazonPage.closeBrowser();
    }

    @Test
    public void runAssignment2(){
        amazonPage.searchProduct(GenericLibs.getValueFromPropertyFile(
                GenericLibs.TEST_DATA_PROPS, "AMAZON_SEARCH_KEYWORD_IPHONE6"));

        int amazonPrice = amazonPage.getPriceOfItemFromSearchResult(
                GenericLibs.getValueFromPropertyFile(GenericLibs.TEST_DATA_PROPS, "AMAZON_SEARCH_KEYWORD_IPHONE6"));

        amazonPage.closeBrowser();

        FlipkartPage flipkartPage = new FlipkartPage(browserType);
        String searchKey = GenericLibs.getValueFromPropertyFile(
                GenericLibs.TEST_DATA_PROPS, "AMAZON_SEARCH_KEYWORD_IPHONE6");
        searchKey = searchKey.replace("32GB","Space Grey, 32 GB").replace(" - Space Grey","");

        flipkartPage.searchProduct(searchKey);

        int flipkartPrice = flipkartPage.getProductPriceFromSearchListingPage(searchKey);

        if(amazonPrice > flipkartPrice){
            System.out.println("Flipkart Price for search item is less than Amazon");
        }else if(flipkartPrice > amazonPrice){
            System.out.println("Amazon Price for search item is less than Flipkart");
        }else {
            System.out.println("Item price in both sites are same");
        }

        flipkartPage.closeBrowser();
    }
}
