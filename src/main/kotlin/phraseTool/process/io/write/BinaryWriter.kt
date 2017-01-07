package phraseTool.process.io.write

import phraseTool.model.Fragment
import phraseTool.model.PhraseBank
import phraseTool.model.Replacement
import phraseTool.process.io.FileTypeProvider
import phraseTool.process.io.refByte
import phraseTool.process.io.refByteLength
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

        var offset = refByteLength
        var lastFragment = initialFragment
        val fragmentOffsets : Map<Fragment,Int> = otherFragments.fold( mutableMapOf( lastFragment to offset ) )
        {
            map, fragment ->
            offset += lastFragment.byteSize()
            lastFragment = fragment
            map[fragment] = offset
            map
        }

        fun writeReplacement( replacement: Replacement)
        {
            val ascii = Charset.forName("ASCII")

            replacement.text.forEachIndexed()
            {
                index, string ->

                val stringAscii = string.toByteArray( ascii )
                stream.write( stringAscii )

                stream.write( refByte )

                if( replacement.references.size > index )
                {
                    val referencedFragment       = replacement.references[index]
                    val referencedFragmentOffset = fragmentOffsets[referencedFragment] ?: throw Exception("Fragment '${referencedFragment.key}' not properly deserialized")

                    stream.writeUint16( referencedFragmentOffset )

                    stream.write( 0 )
                }
            }
        }

        fun writeFragment( fragment: Fragment)
        {
            val replacements = fragment.replacements ?: throw Exception("Fragment '${fragment.key}' undefined")

            stream.writeUint16( replacements.size )
            replacements.forEach( ::writeReplacement )
        }

        stream.writeUint16(fragments.size)
        writeFragment(initialFragment)
        otherFragments.forEach( ::writeFragment )

        stream.flush()
    }
}
