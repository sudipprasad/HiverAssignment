package com.hiver.tests;

import com.hiver.pages.FlipkartPage;
import org.testng.Assert;
import org.testng.annotations.*;

public class Assignment1_Flipkart {
    FlipkartPage flipkartPage;
    @BeforeClass
    @Parameters({"browser.type"})
    public void TestSetup(String browser){
        flipkartPage = new FlipkartPage(browser);
    }

    @AfterClass
    public void cleanUp(){
        flipkartPage.closeBrowser();
    }

    @Test
    public void runAssignment1(){
        flipkartPage.navigateToHomePage();

        flipkartPage.navigateToPixelSubMenu();

        String productName = flipkartPage.getFirstProductNameFromListing();

        int productPrice = flipkartPage.getFirstProductPriceFromListing();

        flipkartPage.clickOnFirstProductInListingPage();

        flipkartPage.switchReferenceToNewTab();

        flipkartPage.addProductToCart();

        flipkartPage.goProductToCart();

        String productNameInCart = flipkartPage.getFirstProductNameFromCart();

        int productPriceInCart = flipkartPage.getFirstProductPriceFromCart();

        Assert.assertTrue(productName.equals(productNameInCart), "Same Item Added");
        Assert.assertTrue(productPrice==productPriceInCart, "Same Item Price in Cart");

        flipkartPage.clickAndValidatePlusBtnInCart();

        System.out.println("Total Payable for "+ productName +" "+ flipkartPage.validateProductDetailsSection());
    }
}