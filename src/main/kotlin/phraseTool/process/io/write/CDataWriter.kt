package phraseTool.process.io.write

import phraseTool.model.PhraseBank
import phraseTool.process.io.FileTypeProvider
import phraseTool.util.writeCDataHeader
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class CDataWriter( val variableName: String ) : PhraseBankWriter, FileTypeProvider
{
    override val fileTypeDescription : String = "C header source file"
    override val fileExtension       : String = "h"

    override fun write( phraseBank: PhraseBank, stream: OutputStream )
    {
        val byteStream = ByteArrayOutputStream()
        BinaryWriter().write( phraseBank, byteStream )
        val bytes = byteStream.toByteArray()
        writeCDataHeader( bytes, stream, variableName )
    }
}
