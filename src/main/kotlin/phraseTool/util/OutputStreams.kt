package phraseTool.util

import java.io.InputStream
import java.io.OutputStream

fun OutputStream.writeUint16(int: Int )
{
    val uint16 = ByteArray(2)
    uint16[0] = (int       and 0xff).toByte()
    uint16[1] = (int shr 8 and 0xff).toByte()
    write(uint16)
}

fun InputStream.readUint16() : Int
{
    return (read() shl 8 and 0xff) + (read() and 0xff)
}
