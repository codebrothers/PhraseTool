package phraseTool.process.io.read

import phraseTool.model.Fragment
import phraseTool.model.PhraseBank
import phraseTool.model.Replacement
import phraseTool.process.io.FileTypeProvider
import phraseTool.process.io.refByte
import phraseTool.process.io.refByteLength
import phraseTool.util.readUInt16
import java.io.InputStream
import java.util.*

class BinaryReader : PhraseBankReader, FileTypeProvider
{
    override val fileExtension       : String = "bin"
    override val fileTypeDescription : String = "PhraseBank binary format"

    override fun read( stream: InputStream ): PhraseBank
    {
        var offset = 0  // Only the following read* functions should modify this offset

        fun readUint16() : Int
        {
            val uint16 = stream.readUInt16()
            offset += 2
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
            val replacementTexts     = ArrayList<String>()
            val replacementFragments = ArrayList<Fragment>()

            val textBuilder = StringBuilder()

            var byte : Int
            readLoop@ do
            {
                fun onTextCompleted()
                {
                    val text = textBuilder.toString()
                    replacementTexts.add(text)
                    textBuilder.setLength(0)
                }

                byte = readByte()
                when (byte)
                {
                    refByte -> // Reference escape code
                    {
                        onTextCompleted()

                        val reference: Int = readUint16()
                        val fragment = Fragment.forKey(reference.toString())
                        replacementFragments.add(fragment)
                    }

                    0 -> // Replacement terminator
                    {
                        onTextCompleted()

                        break@readLoop
                    }

                    else -> // Text character
                    {
                        val char = byte.toChar()
                        textBuilder.append(char)
                    }
                }

                ++offset
            }
            while( true )

            return Replacement( replacementTexts, replacementFragments )
        }

        fun readFragment() : Fragment
        {
            val fragment = Fragment.forKey( offset.toString() )
            val replacementCount = readUint16()
            fragment.replacements = (1..replacementCount).map{ readReplacement() }.toSet()
            return fragment
        }

        val fragmentCount = readUint16()
        val fragments = (1..fragmentCount).map{ readFragment() }.toSet()

        return PhraseBank( fragments )
    }
}
