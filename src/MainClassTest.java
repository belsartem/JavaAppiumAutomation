import org.junit.Assert;
import org.junit.Test;

public class MainClassTest extends MainClass {

    @Test
    public void testGetClassString(){
        Assert.assertTrue("В возвращенной строке нет значений 'hello' или 'Hello'", getClassString().contains("Hello") || getClassString().contains("hello"));
    }
}