package phraseTool.util

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

fun writeCDataHeader( bytes: ByteArray, stream: OutputStream, variableName: String )
{
    val ascii = Charset.forName("ASCII")

    val writer = OutputStreamWriter( stream, ascii )

    writer.write("\n// Phrasebank data\n\n")
    writer.write("char *$variableName =\n{\n\t")

    val columnCount = 16
    var columnIndex = 0

    var first : Boolean = true

    bytes.forEach()
    {
        byte ->

        if( first ) { first = false } else { writer.write(", ") }

        if( columnIndex > columnCount )
        {
            writer.write( "\n\t" )
            columnIndex = 0
        }

        val byteString = String.format("%02X", byte )
        writer.write( "0x$byteString" )

        ++columnIndex
    }

    writer.write("\n};\n")

    writer.flush()
}

