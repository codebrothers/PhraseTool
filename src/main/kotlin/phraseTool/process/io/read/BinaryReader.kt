package phraseTool.process.io.read

import phraseTool.model.Fragment
import phraseTool.model.PhraseBank
import phraseTool.model.Replacement
import phraseTool.process.io.FileTypeProvider
import phraseTool.process.io.refByte
import phraseTool.process.io.refByteLength
import phraseTool.util.readUint16
import java.io.InputStream
import java.util.*

class BinaryReader : PhraseBankReader, FileTypeProvider
{
    override val fileExtension       : String = "bin"
    override val fileTypeDescription : String = "PhraseBank binary format"

    override fun read( stream: InputStream ): PhraseBank
    {
        var offset = 0
        val offsetFragments : Map<Int, Fragment> = HashMap()

        fun readUint16() : Int
        {
            val uint16 = stream.readUint16()
            offset += refByteLength
            return uint16
        }

        fun readByte() : Int
        {
            val byte = stream.read()
            ++offset
            return byte
        }

        fun readReplacement() : Replacement
        {
            val replacement = Replacement()

            val textBuilder = StringBuilder()

            when( readByte() )
            {
                refByte ->
                {
                    val text = textBuilder.toString()
                    textBuilder.setLength(0)
                    val reference : Int = readUint16()
                }

                else ->
                {
                    textBuilder.append( refByte )
                }
            }

            ++offset

            return replacement
        }

        fun readFragment() : Fragment
        {
            val fragment = Fragment.forKey( offset.toString() )
            val replacementCount = readUint16()
            fragment.replacements = (0..replacementCount).map{ readReplacement() }.toSet()
            return fragment
        }

        val fragmentCount = readUint16()
        val fragments = (0..fragmentCount).map{ readFragment() }.toSet()

        return PhraseBank( fragments )
    }
}
