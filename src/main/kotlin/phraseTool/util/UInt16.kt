package phraseTool.util

import java.io.InputStream
import java.io.OutputStream
import kotlin.jvm.internal.Ref

data class UInt16Bytes( val lsb: Byte, val msb: Byte )
{
    fun toInt() : Int
    {
        return (lsb.toInt() and 0xff) + ((msb.toInt() and 0xff) shl 8 )
    }
}

fun Int.toUInt16Bytes() : UInt16Bytes
{
    return UInt16Bytes( lsb = (this and 0xff).toByte(), msb = (this shr 8 and 0xff).toByte() )
}

fun OutputStream.writeUInt16(int: Int )
{
    val bytes = int.toUInt16Bytes()

    write( bytes.lsb.toInt() )
    write( bytes.msb.toInt() )
}

fun InputStream.readUInt16() : Int
{
    return UInt16Bytes( lsb = read().toByte(), msb = read().toByte() ).toInt()
}

fun ByteArray.readUInt16( readOffset: Ref.IntRef ) : Int
{
    val lsb = this[ readOffset.element++ ]
    val msb = this[ readOffset.element++ ]
    return UInt16Bytes( lsb, msb ).toInt()
}