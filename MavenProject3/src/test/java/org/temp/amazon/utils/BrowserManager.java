package org.temp.amazon.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.exec.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.TestException;

public class BrowserManager {
	public static WebDriver driver;
	public WebDriverWait wait;
    public Actions actions;
    public Select select;
    public static int timeout = 10;
    public static String driverpath = "C:\\Users\\Bala\\eclipse-workspace\\Augustfour\\driver\\chromedriver.exe";
    public String filepath = "C:/Users/Bala/eclipse-workspace/Screen/";
    
	public static WebDriver getDriver(String type,String url) {
	
		if(type.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", driverpath);
		    driver = new ChromeDriver();
		}
		else if(type.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "C:\\Users\\Bala\\eclipse-workspace\\Augustfour\\driver\\msedgedriver.exe");
			driver = new EdgeDriver();
		}
		else {
			Assert.assertTrue(false, "No browser type sent");
		}
	
		driver.manage().window().maximize();
		driver.get(url);
		Reporter.log("Navigated to Browser:" + type + "URL:" + url,true);
		return driver;
	}
	public void pageRefresh() {
		driver.navigate().refresh();
	}
	
	public String getPageTitle() throws Exception {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            throw new TestException(String.format("Current page title is: %s", driver.getTitle()));
        }
    }
	
	public String getCurrentURL() {
        try {
            return driver.getCurrentUrl();
        } catch (Exception e) {
            throw new TestException(String.format("Current URL is: %s", driver.getCurrentUrl()));
        }
    }
	public WebElement getElement(By selector) {
        try {
            return driver.findElement(selector);
        } catch (Exception e) {
            System.out.println(String.format("Element %s does not exist - proceeding", selector));
        }
       return null;
    }

    public String getElementText(By selector) {
        waitUntilElementIsDisplayedOnScreen(selector);
       
        try {
        	
            return driver.findElement(selector).getText();
        } catch (Exception e) {
            System.out.println(String.format("Element %s does not exist - proceeding", selector));
        }
        return null;
    }

    public List<WebElement> getElements(By Selector) {
        waitForElementToBeVisible(Selector);
        try {
            return driver.findElements(Selector);
        } catch (Exception e) {
            throw new NoSuchElementException(String.format("The following element did not display: [%s] ", Selector.toString()));
        }
    }
    

    public List<String> getListOfElementTexts(By selector) {
        List<String> elementList = new ArrayList<String>();
        List<WebElement> elements = getElements(selector);

        for (WebElement element : elements) {
            if (element == null) {
                throw new TestException("Some elements in the list do not exist");
            }
            if (element.isDisplayed()) {
                elementList.add(element.getText().trim());
            }
        }
        return elementList;
    }

    public void click(By selector) {
        WebElement element = getElement(selector);
        waitForElementToBeClickable(selector);
        try {
            element.click();
        } catch (Exception e) {
            throw new TestException(String.format("The following element is not clickable: [%s]", selector));
        }
    }

    public void scrollToThenClick(By selector) {
        WebElement element = driver.findElement(selector);
        actions = new Actions(driver);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            actions.moveToElement(element).perform();
            actions.click(element).perform();
        } catch (Exception e) {
            throw new TestException(String.format("The following element is not clickable: [%s]", element.toString()));
        }
    }

    public void sendKeys(By selector, String value) {
        WebElement element = getElement(selector);
        clearField(element);
        try {
            element.sendKeys(value);
        } catch (Exception e) {
            throw new TestException(String.format("Error in sending [%s] to the following element: [%s]", value, selector.toString()));
        }
    }

    public void clearField(WebElement element) {
        try {
            element.clear();
            waitForElementTextToBeEmpty(element);
        } catch (Exception e) {
            System.out.print(String.format("The following element could not be cleared: [%s]", element.getText()));
        }
    }

    public void waitForElementToDisplay(By Selector) {
        WebElement element = getElement(Selector);
        while (!element.isDisplayed()) {
            System.out.println("Waiting for element to display: " + Selector);
            sleep(200);
        }
    }

    public void waitForElementTextToBeEmpty(WebElement element) {
        String text;
        try {
            text = element.getText();
            int maxRetries = 10;
            int retry = 0;
            while ((text.length() >= 1) || (retry < maxRetries)) {
                retry++;
                text = element.getText();
            }
        } catch (Exception e) {
            System.out.print(String.format("The following element could not be cleared: [%s]", element.getText()));
        }

    }

    public void waitForElementToBeVisible(By selector) {
        try {
            wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        } catch (Exception e) {
            throw new NoSuchElementException(String.format("The following element was not visible: %s", selector));
        }
    }

    public void waitUntilElementIsDisplayedOnScreen(By selector) {
        try {
            wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(selector));
        } catch (Exception e) {
            throw new NoSuchElementException(String.format("The following element was not visible: %s ", selector));
        }
    }

    public void waitForElementToBeClickable(By selector) {
        try {
            wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.elementToBeClickable(selector));
        } catch (Exception e) {
            throw new TestException("The following element is not clickable: " + selector);
        }
    }

    public void sleep(final long millis) {
        System.out.println((String.format("sleeping %d ms", millis)));
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void selectIfOptionTextContains(By selector, String searchCriteria) {

        waitForElementToBeClickable(selector);
        Select dropdown = new Select(getElement(selector));

        List<WebElement> options = dropdown.getOptions();

        String optionText = "";

        if (options == null) {
            throw new TestException("Options for the dropdown list cannot be found.");
        }

        for (WebElement option : options) {

            optionText = option.getText().trim();
            boolean isOptionDisplayed = option.isDisplayed();

            if (optionText.contains(searchCriteria) && isOptionDisplayed) {
                try {
                    dropdown.selectByVisibleText(optionText);
                    break;
                } catch (Exception e) {
                    throw new NoSuchElementException(String.format("The following element did not display: [%s] ", selector.toString()));
                }
            }
        }
    }

    public void selectdropdownvalue(By selector, String value) {
    	waitForElementToBeVisible(selector);
    	WebElement element =  driver.findElement(selector);
    	Select dropdown = new Select(element);
    	dropdown.selectByValue(value);
    }
    public void selectIfOptionTextEquals(By selector, String searchCriteria) {

        waitForElementToBeClickable(selector);
        Select dropdown = new Select(getElement(selector));

        List<WebElement> options = dropdown.getOptions();

        String optionText = "";

        if (options == null) {
            throw new TestException("Options for the dropdown list cannot be found.");
        }

        for (WebElement option : options) {

            optionText = option.getText().trim();
            boolean isOptionDisplayed = option.isDisplayed();

            if (optionText.equals(searchCriteria) && isOptionDisplayed) {
                try {
                    dropdown.selectByVisibleText(optionText);
                    break;
                } catch (Exception e) {
                    throw new NoSuchElementException(String.format("The following element did not display: [%s] ", selector.toString()));
                }
            }
        }
    }

    public List<String> getDropdownValues(By selector) {

        waitForElementToDisplay(selector);
        Select dropdown = new Select(getElement(selector));
        List<String> elementList = new ArrayList<String>();

        List<WebElement> allValues = dropdown.getOptions();

        if (allValues == null) {
            throw new TestException("Some elements in the list do not exist");
        }

        for (WebElement value : allValues) {
            if (value.isDisplayed()) {
                elementList.add(value.getText().trim());
            }
        }
        return elementList;
    }
    
    
    public void windowhandle(By selector) {
    	
    	String parentwindowid = driver.getWindowHandle();
    	click(selector);
    	Set<String> allWindowid=driver.getWindowHandles();
    	for(String x:allWindowid) {
    		if(!parentwindowid.equals(x)) {
    			driver.switchTo().window(x);
    			//sleep(timeout);
  
    		}
    	}
    	
    }
    
    public void screenshot(String s) {
    	TakesScreenshot tk = (TakesScreenshot)driver;
    	File f = tk.getScreenshotAs(OutputType.FILE);
    	File f1 = new File(filepath+s+".jpg");
    	try {
			FileUtils.copyFile(f, f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void quit() throws Exception {
    	wait();
    	driver.quit();
    }
}
