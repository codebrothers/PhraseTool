package phraseTool.process.io.read

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import phraseTool.model.PhraseBank
import phraseTool.model.RawPhraseBank
import phraseTool.process.io.FileTypeProvider
import sun.misc.IOUtils
import java.io.InputStream

class JSONReader() : PhraseBankReader, FileTypeProvider
{
    override val fileTypeDescription : String = "JSON file"
    override val fileExtension       : String = "json"

    override fun read(stream: InputStream): PhraseBank
    {
        val bytes = IOUtils.readFully(stream, -1, false )

        val json   = String( bytes )
        val mapper = jacksonObjectMapper()

        val rawPhraseBank : RawPhraseBank = mapper.readValue( json )

        return PhraseBank( rawPhraseBank )
    }
}



