package phraseTool.process.io.read

import phraseTool.model.PhraseBank
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path

interface PhraseBankReader
{
    fun read( stream: InputStream ) : PhraseBank
}

/*
Wrap the basic InputStream writing ability of all PhraseBankWriters with file-reading capability.
 */
fun PhraseBankReader.read( path: Path ) : PhraseBank
{
    val file = path.toFile()
    val stream = FileInputStream(file)
    val phraseBank = read( stream )
    stream.close()
    return phraseBank
}