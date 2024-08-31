import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class CommentsAppTest {

    public WebDriver driver;
    public int commentCount;
    public Object[][] dataSet = {
            { "Sai", "Iam software developer" },
            { "Nath", "Iam Automation tester" },
            { "Cherry", "AI-powered automation, limitless potential." },
            { "Durgam", "Data-driven decision-making, game-changer." },
            { "Laddu", "Qa" }
    };

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\HP\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://qacommentsapp.ccbp.tech/");
        commentCount = 0;
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @DataProvider
    public Object[][] commentsData() {
        return dataSet;
    }

    public void addComment(String name, String comment) {
        WebElement nameInputField = driver.findElement(By.className("name-input"));
        nameInputField.sendKeys(name);

        WebElement commentInputField = driver.findElement(By.className("comment-input"));
        commentInputField.sendKeys(comment);

        WebElement addCommentButton = driver.findElement(By.className("add-button"));
        addCommentButton.click();

        commentCount++;
    }

    public void addCommentsData() {
        for (Object[] data : dataSet) {
            String name = String.valueOf(data[0]);
            String comment = String.valueOf(data[1]);
            addComment(name, comment);
        }
    }

    @Test(priority = 1)
    public void testCommentsCount() {
        for (Object[] data : dataSet) {
            String name = String.valueOf(data[0]);
            String comment = String.valueOf(data[1]);
            addComment(name, comment);

            WebElement commentCounterEle = driver.findElement(By.className("comments-count"));
            String countStr = commentCounterEle.getText();
            int countInt = Integer.parseInt(countStr);
            Assert.assertEquals(countInt, commentCount, "Comments count do not match");
        }
    }

    @Test(priority = 2, dataProvider = "commentsData")
    public void testCommentsInitial(String name, String comment) {
        addComment(name, comment);

        WebElement initialLetter = driver.findElement(By.className("initial"));
        String initialLetterStr = initialLetter.getText();
        char initial = initialLetterStr.charAt(0);
        Assert.assertEquals(initial, name.charAt(0), "Initial do not match");
    }

    @Test(priority = 3)
    public void testCommentsAndOrder() {
        addCommentsData();

        List<WebElement> usernameElements = driver.findElements(By.className("username"));
        List<WebElement> commentElements = driver.findElements(By.className("comment"));

        for (int i = 0; i < dataSet.length; i++) {
            Object data[] = dataSet[i];
            String name = String.valueOf(data[0]);
            String comment = String.valueOf(data[1]);
            WebElement usernameEle = usernameElements.get(i);
            WebElement commentEle = commentElements.get(i);
            Assert.assertEquals(usernameEle.getText(), name, "Order does not match");
            Assert.assertEquals(commentEle.getText(), comment, "Comment does not correspond to the username");
        }
    }

    @Test(priority = 4)
    public void testCommentLikes() {
        addCommentsData();

        int likesCount = 0;

        List<WebElement> likeButtonElements = driver.findElements(By.xpath("//button[text() = 'Like']"));

        for (WebElement likeButton : likeButtonElements) {
            likeButton.click();

            likesCount++;

            List<WebElement> likedElements = driver.findElements(By.className("active"));

            Assert.assertEquals(likedElements.size(), likesCount, "Likes count do not match");
        }
    }

    @Test(priority = 5)
    public void testCommentDeletes() {
        addCommentsData();

        List<WebElement> deleteButtonElements = driver
                .findElements(By.xpath("//img[@class = 'delete']/parent::button"));

        for (WebElement deleteButton : deleteButtonElements) {
            deleteButton.click();

            commentCount--;

            List<WebElement> commentElements = driver.findElements(By.className("comment-item"));

            Assert.assertEquals(commentElements.size(), commentCount, "Comments count do not match");
        }
    }


}
