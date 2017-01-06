package phraseTool.process

import phraseTool.model.Fragment
import phraseTool.model.PhraseBank
import phraseTool.model.Replacement
import phraseTool.refByte
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset

internal fun PhraseBank.toByteArray() : ByteArray
{
    val out = ByteArrayOutputStream()

    val initialFragment : Fragment = fragments.find { fragment -> fragment.key == phraseTool.initialKey } ?: throw Exception("Input error: 'phrase' fragment required but not found.")

    val otherFragments : List<Fragment> = (fragments - initialFragment).toList()

    var offset = 0
    var lastFragment = initialFragment
    val fragmentOffsets : Map<Fragment,Int> = otherFragments.fold( mutableMapOf( lastFragment to offset ) )
    {
        map, fragment ->
        offset += lastFragment.byteSize()
        lastFragment = fragment
        map[fragment] = offset
        map
    }

    fun ByteArrayOutputStream.writeAsUint16(int: Int )
    {
        val uint16 = ByteArray(2)
        uint16[0] = (int       and 0xff).toByte()
        uint16[1] = (int shr 8 and 0xff).toByte()
        write(uint16)
    }

    fun writeReplacement( replacement: Replacement)
    {
        val ascii = Charset.forName("ASCII")

        replacement.text.forEachIndexed()
        {
            index, string ->

            val stringAscii = string.toByteArray( ascii )
            out.write( stringAscii )

            out.write( refByte )

            if( replacement.references.size > index )
            {
                val referencedFragment       = replacement.references[index]
                val referencedFragmentOffset = fragmentOffsets[referencedFragment] ?: throw Exception("Fragment '${referencedFragment.key}' not properly deserialized")

                out.writeAsUint16( referencedFragmentOffset )

                out.write( 0 )
            }
        }
    }

    fun writeFragment( fragment: Fragment)
    {
        val replacements = fragment.replacements ?: throw Exception("Fragment '${fragment.key}' undefined")

        out.writeAsUint16( replacements.size )
        replacements.forEach( ::writeReplacement )
    }

    writeFragment(initialFragment)
    otherFragments.forEach( ::writeFragment )

    return out.toByteArray()
}
