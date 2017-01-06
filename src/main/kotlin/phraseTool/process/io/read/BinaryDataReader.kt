package phraseTool.process.io.read

import phraseTool.model.PhraseBank
import java.nio.file.Path

/**
 * Created by Chris on 06/01/2017.
 */
class BinaryDataReader : PhraseBankReader
{
    override val fileExtension : String = "bin"

    override fun read(path: Path): PhraseBank
    {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}