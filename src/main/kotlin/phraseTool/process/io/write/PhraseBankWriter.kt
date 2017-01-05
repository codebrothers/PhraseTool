package phraseTool.process.io.write

import phraseTool.model.PhraseBank
import java.nio.file.Path

interface PhraseBankWriter
{
    fun write( phraseBank: PhraseBank, path: Path)
}

