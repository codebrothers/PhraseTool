package phraseTool.process.io.write

import phraseTool.model.PhraseBank
import phraseTool.process.toByteArray
import java.io.File
import java.io.FileWriter
import java.nio.file.Path

class CDataWriter( val variableName: String ) : PhraseBankWriter
{
    override fun write(phraseBank: PhraseBank, path: Path)
    {
        val fileWriter = FileWriter( File( path.toUri() ) )

        val bytes = phraseBank.toByteArray()

        fileWriter.write("\n// Phrasebank data\n\n")
        fileWriter.write("char *phraseBank =\n{\n\t")

        val columnCount = 16
        var columnIndex = 0

        var first : Boolean = true

        bytes.forEach()
        {
            byte ->

            if( first ) { first = false } else { fileWriter.write(", ") }

            if( columnIndex > columnCount )
            {
                fileWriter.write( "\n\t" )
                columnIndex = 0
            }

            val byteString = String.format("%02X", byte )
            fileWriter.write( "0x$byteString" )

            ++columnIndex
        }

        fileWriter.write("\n};\n")
        fileWriter.close()
    }
}
