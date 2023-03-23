import org.junit.Assert;
import org.junit.Test;

public class MainClassTest extends MainClass {

    @Test
    public void testGetLocalNumber(){
        Assert.assertEquals("Значение не равно 14", 14, getLocalNumber());
    }
}