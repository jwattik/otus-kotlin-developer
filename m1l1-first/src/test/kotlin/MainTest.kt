import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun testMainOutput() {
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out

        System.setOut(printStream)

        main()

        System.setOut(originalOut)

        assertEquals("Hello World!\n", outputStream.toString())
    }
}