import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

public class FirstTest extends CoreTestCase {

    private MainPageObject MainPageObject;

    protected void setUp() throws Exception {

        super.setUp();

        MainPageObject = new MainPageObject(driver);
    }

    @Test
    public void testSearch() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.waitForSearchResult("Object-oriented programming language");
    }

    @Test
    public void testCancelSearch() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelSearch();
        SearchPageObject.waitForCancelButtonToDisappear();
    }

    @Test
    public void testCompareArticleTitle() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        String articleTitle = ArticlePageObject.getArticleTitle();

        Assert.assertEquals(
                "Unexpected title",
                "Java (programming language)",
                articleTitle
        );
    }

    @Test
    public void testSwipeArticle() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Appium");
        SearchPageObject.clickByArticleWithSubstring("Automation for Apps");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        ArticlePageObject.swipeToFooter();
    }

    @Test
    public void testCompareElementText() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.assertElementHasText(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Search…",
                "There's no such text in the element"
        );
    }

    @Test
    public void testCancelSearchResults() {
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Lana",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "No search results",
                10
        );

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        MainPageObject.waitForElementNotPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "Search results are still present on the page",
                5
        );
    }

    @Test
    public void testResultsContainWord() {
        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/search_results_list"),
                "No search results",
                10
        );

        MainPageObject.assertElementHasText(
                By.id("org.wikipedia:id/search_results_list"),
                "Java",
                "At least one of the results has no the word you are looking for"
        );
    }

    @Test
    public void testSaveFirstArticleToMyList() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Java");
        SearchPageObject.clickByArticleWithSubstring("Object-oriented programming language");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        String articleTitle = ArticlePageObject.getArticleTitle();
        String nameOfFolder = "Learning programming";

        ArticlePageObject.addArticleToMyList(nameOfFolder);
        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = new NavigationUI(driver);
        NavigationUI.clickMyLists();

        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);
        MyListsPageObject.openFolderByName(nameOfFolder);
        MyListsPageObject.swipeArticleToDelete(articleTitle);
    }

    @Test
    public void testAmountOfNotEmptySearch() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        String searchLine = "Linkin Park discography";
        SearchPageObject.typeSearchLine(searchLine);
        int amountOfSearchResults = SearchPageObject.getAmountOfFoundArticles();

        Assert.assertTrue(
                "We found too few results",
                amountOfSearchResults > 0
        );
    }

    @Test
    public void testAmountOfEmptySearch() {
        SearchPageObject SearchPageObject = new SearchPageObject(driver);
        SearchPageObject.initSearchInput();
        String searchLine = "shjdrkjtfdfhy";
        SearchPageObject.typeSearchLine(searchLine);
        SearchPageObject.waitForEmptyResultsLabel();
        SearchPageObject.assertThereIsNoResultOfSearch();
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Search Wikipedia']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        String searchLine = "Java";
        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@text='Search…']"),
                searchLine,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by " + "'" + searchLine + "'",
                15
        );

        String titleBeforeRotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find a title of the article",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String titleAfterRotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find a title of the article",
                15
        );

        Assert.assertEquals(
                "The article title has been changed after screen rotation",
                titleBeforeRotation,
                titleAfterRotation
        );

        driver.rotate(ScreenOrientation.PORTRAIT);

        String titleAfterSecondRotation = MainPageObject.waitForElementAndGetAttribute(
                By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot find a title of the article",
                15
        );

        Assert.assertEquals(
                "The article title has been changed after screen rotation",
                titleBeforeRotation,
                titleAfterSecondRotation
        );

    }

    @Test
    public void testCheckSearchArticleInBackground() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Search Wikipedia']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@text='Search…']"),
                "Java",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                5
        );

        driver.runAppInBackground(2);

        MainPageObject.waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Object-oriented programming language']"),
                "Cannot find the article after returning from background",
                5
        );
    }

    @Test
    public void testSaveTwoArticlesTiListAnDeleteOne() {
        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Search Wikipedia']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@text='Search…']"),
                "Java",
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find the button to open article options",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ListView//*[@text='Add to reading list']"),
                "Cannot find an option to add the article to reading list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find a GOT IT tip overlay",
                5
        );

        MainPageObject.waitForElementAndClear(
                By.xpath("//*[@text='My reading list']"),
                "Cannot find an input to set name of an article folder",
                1
        );

        String nameOfFolder = "Learning programming";

        MainPageObject.waitForElementAndSetValue(
                By.id("org.wikipedia:id/text_input"),
                nameOfFolder,
                "Cannot put text into articles folder input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='OK']"),
                "Cannot press OK button to save a reading list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot press X to close the article",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Search Wikipedia']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        String searchRequest = "Appium";

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@text='Search…']"),
                searchRequest,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Automation for Apps']"),
                "Cannot find 'Automation for Apps' topic searching by " + "'" + searchRequest + "'",
                15
        );

        MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );

        String articleTitleBeforeAdding = MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        ).getText();

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find the button to open article options",
                10
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ListView//*[@text='Add to reading list']"),
                "Cannot find an option to add the article to reading list",
                10
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + nameOfFolder + "']"),
                "Cannot find created folder",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),
                "Cannot press X to close the article",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//android.widget.FrameLayout[@content-desc='My lists']"),
                "Cannot press My List to open reading list",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + nameOfFolder + "']"),
                "Cannot find created folder",
                5
        );

        MainPageObject.swipeElementToLeft(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot swipe saved article"
        );

        MainPageObject.waitForElementNotPresent(
                By.xpath("//*[@text='Java (programming language)']"),
                "Cannot delete saved article",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='" + searchRequest + "']"),
                "The article '" + searchRequest + "' is not in the list",
                1
        );

        String articleTitleAfterAdding = MainPageObject.waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        ).getText();

        Assert.assertEquals(
                "The article title has been changed after adding to the reading list",
                articleTitleBeforeAdding,
                articleTitleAfterAdding

        );
    }

    @Test
    public void testTitleElement() {

        String searchRequest = "Appium";

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@text='Search Wikipedia']"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        MainPageObject.waitForElementAndSendKeys(
                By.xpath("//*[@text='Search…']"),
                searchRequest,
                "Cannot find search input",
                5
        );

        MainPageObject.waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@text='Automation for Apps']"),
                "Cannot find 'Automation for Apps' topic searching by " + "'" + searchRequest + "'",
                15
        );

        MainPageObject.assertElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title"
        );
    }
}
