package phraseTool.process.io.read

import phraseTool.model.PhraseBank
import java.nio.file.Path

interface PhraseBankReader
{
    val fileExtension : String

    fun read( path: Path) : PhraseBank
}
