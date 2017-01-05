package phraseTool.process.io.write

import phraseTool.model.PhraseBank
import phraseTool.process.toByteArray
import java.nio.file.Files
import java.nio.file.Path

class BinaryDataWriter() : PhraseBankWriter
{
    override fun write( phraseBank: PhraseBank, path: Path)
    {
        val bytes = phraseBank.toByteArray()

        Files.write( path, bytes )
    }
}
