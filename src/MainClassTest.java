import org.junit.Assert;
import org.junit.Test;

public class MainClassTest extends MainClass {

    @Test
    public void testGetClassNumber(){
        Assert.assertTrue("Значение меньше 45", getClassNumber() > 45);
    }
}