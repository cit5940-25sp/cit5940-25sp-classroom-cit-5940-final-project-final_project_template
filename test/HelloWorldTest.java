import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloWorldTest {

    @Test
    public void testSayHello() {

        assertEquals("Hello, World!", HelloWorld.sayHello());
    }

}
