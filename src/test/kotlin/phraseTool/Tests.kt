
package phraseTool

import junit.framework.TestCase
import phraseTool.util.readUInt16
import phraseTool.util.writeUInt16
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
            outStream.writeUInt16(numberIn)
            val bytes = outStream.toByteArray()

            assertEquals( 2, bytes.size )

            val inStream = ByteArrayInputStream( bytes )
            val numberOut = inStream.readUInt16()

            assertEquals( numberIn, numberOut )
        }
    }
}
