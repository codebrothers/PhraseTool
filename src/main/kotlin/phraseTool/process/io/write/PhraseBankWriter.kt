package phraseTool.process.io.write

import phraseTool.model.PhraseBank
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Path

interface PhraseBankWriter
{
    fun write(phraseBank: PhraseBank, stream: OutputStream)
}

/*
Wrap the basic OutputStream writing ability of all PhraseBankWriters with file-writing capability.
 */
fun PhraseBankWriter.write( phraseBank: PhraseBank, path: Path )
{
    val file = File( path.toUri() )
    val stream = FileOutputStream(file)
    write( phraseBank, stream )
    stream.close()
}
