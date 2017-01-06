package phraseTool.process.io.read

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import phraseTool.model.PhraseBank
import phraseTool.model.RawPhraseBank
import java.nio.file.Files
import java.nio.file.Path

class JSONPhraseBankReader() : PhraseBankReader
{
    override val fileExtension : String = "json"

    override fun read(path: Path): PhraseBank
    {
        assert( path.toFile().exists() )

        val json   = String( Files.readAllBytes( path ) )
        val mapper = jacksonObjectMapper()

        val rawPhraseBank : RawPhraseBank = mapper.readValue( json )

        return PhraseBank( rawPhraseBank )
    }
}



