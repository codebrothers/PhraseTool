package phraseTool.process.io.write

import phraseTool.model.Fragment
import phraseTool.model.PhraseBank
import phraseTool.model.Replacement
import phraseTool.process.io.FileTypeProvider
import phraseTool.process.io.refByte
import phraseTool.util.writeUint16
import java.io.OutputStream
import java.nio.charset.Charset

class BinaryWriter : PhraseBankWriter, FileTypeProvider
{
    override val fileExtension       : String = "bin"
    override val fileTypeDescription : String = "PhraseBank binary format"

    override fun write( phraseBank: PhraseBank, stream: OutputStream )
    {
        val fragments = phraseBank.fragments
        val initialFragment : Fragment = fragments.find { fragment -> fragment.key == phraseTool.initialKey } ?: throw Exception("Input error: 'phrase' fragment required but not found.")
        val otherFragments : List<Fragment> = (fragments - initialFragment).toList()
        var offset = 0

        fun writeUint16( int: Int )
        {
            stream.writeUint16( int )
            offset += 2
        }

        offset = 0

        writeUint16(fragments.size)

        var predictedOffset = offset
        var lastFragment = initialFragment
        val predictedFragmentOffsets = otherFragments.fold( mutableMapOf( lastFragment to offset ) )
        {
            map, fragment ->
            predictedOffset += lastFragment.byteSize()
            lastFragment = fragment
            map[fragment] = predictedOffset
            map
        }

        fun writeByte( int: Int )
        {
            stream.write(int)
            ++offset
        }

        fun writeRefTo( offset: Int )
        {
            writeByte( refByte )
            writeUint16( offset )
        }

        fun writeBytes( bytes: ByteArray )
        {
            stream.write(bytes)
            offset += bytes.size
        }

        fun writeReplacement( replacement: Replacement)
        {
            val ascii = Charset.forName("ASCII")

            replacement.text.forEachIndexed()
            {
                index, string ->

                val stringAscii = string.toByteArray( ascii )
                writeBytes( stringAscii )

                if( index < replacement.references.size )
                {
                    val referencedFragment       = replacement.references[index]
                    val referencedFragmentOffset = predictedFragmentOffsets[referencedFragment] ?: throw Exception("Fragment '${referencedFragment.key}' not properly deserialized")

                    writeRefTo( referencedFragmentOffset )
                }
            }

            writeByte(0)
        }

        fun writeFragment( fragment: Fragment)
        {
            assert( predictedFragmentOffsets.containsKey( fragment ) )
            assert( offset == predictedFragmentOffsets[fragment] )

            val replacements = fragment.replacements ?: throw Exception("Fragment '${fragment.key}' undefined")

            writeUint16( replacements.size )
            replacements.forEach( ::writeReplacement )
        }


        writeFragment(initialFragment)
        otherFragments.forEach( ::writeFragment )

        stream.flush()
    }
}
