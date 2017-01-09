
package phraseTool

import junit.framework.TestCase
import phraseTool.util.readUint16
import phraseTool.util.writeUint16
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class TestOutputStreams : TestCase("Stuff")
{
    fun testWriteReadUint16()
    {
        listOf( 0, 16, 255, 256, 31234, 65535 ).forEach()
        {
            numberIn ->

            val outStream = ByteArrayOutputStream()
            outStream.writeUint16(numberIn)
            val bytes = outStream.toByteArray()

            assertEquals( 2, bytes.size )

            val inStream = ByteArrayInputStream( bytes )
            val numberOut = inStream.readUint16()

            assertEquals( numberIn, numberOut )
        }
    }
}
