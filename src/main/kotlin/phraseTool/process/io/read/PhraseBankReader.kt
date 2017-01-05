package phraseTool.process.io.read

import phraseTool.model.PhraseBank
import java.nio.file.Path

interface PhraseBankReader
{
    fun read( path: Path) : PhraseBank
}