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

        bytes.forEach { fileWriter.write( Integer.toHexString( it.toInt() ) ) }

        fileWriter.close()
    }
}
