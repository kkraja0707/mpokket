package org.temp.amazon.tc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.temp.amazon.po.Po_Search;
import org.temp.amazon.utils.BrowserManager;
import org.testng.annotations.Test;

public class TCs {

	String url = "https://www.amazon.in/";
    static WebDriver driver;
   
	Po_Search obj = PageFactory.initElements(driver,Po_Search.class);
	@Test
	public void t_01_product_search() {
		driver = BrowserManager.getDriver("chrome",url);
		obj.SearchProduct();	
		
	}
	@Test
	public void t_02_product_search() {
	
		obj.Addproduct();
		
	}
	@Test
	public void t_03_product_search() {
		obj.Add10product();
		
	}
	@Test
	public void t_04_product_search() throws Exception {
		obj.RemoveProduct();
	}
	}

