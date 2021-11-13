package org.temp.amazon.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.temp.amazon.utils.BrowserManager;
import org.testng.Assert;
import org.testng.Reporter;

public class Po_Search extends BrowserManager {

	WebDriver driver;

	public Po_Search(WebDriver driver) {
		this.driver = driver;
	}

	private final By Searchbox = By.id("twotabsearchtextbox");
	private final By Go_button = By.id("nav-search-submit-button");
	private final By Clkproduct = By.xpath("(//span[@class='a-size-medium a-color-base a-text-normal'])[1]");
	private final By clkAddcart = By.id("add-to-cart-button");
	private final By verifyproduct = By.xpath("//h1[@class='a-size-medium a-text-bold']/parent::div/child::h1");
	private final By productAmount = By.xpath("(//span[@class='a-color-price hlb-price a-inline-block a-text-bold']/parent::span/child::span[@class])[1]");
	private final By select10product = By.id("quantity");
	private final By clkcart = By.id("nav-cart-count");
	private final By clkDelete = By.xpath("(//input[@value='Delete']/parent::span/child::input)[1]");
	private final By verifyCartIsEmpty = By.xpath("//span[@id='sc-subtotal-label-activecart']/parent::div/child::span[@class='a-size-medium sc-number-of-items']");

	// Business) method
	public void SearchProduct() {
		sendKeys(Searchbox, "Ponniyin Selvan All 5 Parts");
		Reporter.log("Product entered in search Box:", true);
		click(Go_button);
		if (getCurrentURL().contains("https://www.amazon.in/s?k=Ponniyin+Selvan+All+5+Parts")) {
			Reporter.log("Able to search for the product successfully", true);
			screenshot("ScreenshotforSearchProduct");
		} else {
			Reporter.log("Able to search for the product failed", false);
		}
	}

	public void Addproduct() {
		windowhandle(Clkproduct);
		click(clkAddcart);
		if (getElementText(verifyproduct).contains("Added to Cart")) {
			Reporter.log("Product Added to cart successfully", true);
			screenshot("ScreenshotforAddproduct");
		} else {
			Reporter.log("Product is not Added to cart", true);
		}
	}

	public void Add10product() {

		sendKeys(Searchbox, "Ponniyin Selvan All 5 Parts");
		click(Go_button);
		windowhandle(Clkproduct);
		selectdropdownvalue(select10product, "10");
		click(clkAddcart);

		Reporter.log("Increased product to cart", true);
		if (getElement(productAmount).isDisplayed()) {
			Reporter.log("Verify the Whole product Amount in Cart" + getElementText(productAmount), true);
		}
		screenshot("ScreenshotforAdd10product");

	}

	public void RemoveProduct() throws Exception {
		click(clkcart);
		click(clkDelete);
		Reporter.log("Verify the Cart is Empty and Value of Cart" + getElementText(verifyCartIsEmpty), true);
		Reporter.log("Product is removed", true);
		screenshot("ScreenshotforRemoveProduct");
		quit();
	}
}
