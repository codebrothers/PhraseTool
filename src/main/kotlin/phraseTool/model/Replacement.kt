package phraseTool.model

import phraseTool.process.io.refByteLength
import java.util.*

/**
 * Created by Chris on 04/01/2017.
 */
class Replacement( val text: List<String>, val references: List<Fragment> ) : ByteSizeable
{
    constructor( string: String ) : this( Companion.parseRaw( string ) )

    private constructor( pair: Pair<List<String>,List<Fragment>> ) : this( text = pair.first, references = pair.second )

    companion object
    {
        fun parseRaw( string: String ) : Pair<List<String>,List<Fragment>>
        {
            val pair = string
                    .split( "{", "}" )
                    .foldIndexed( Pair( ArrayList<String>(), ArrayList<String>() ) )
                    {
                        i, replacement, string ->
                        ( if ( i % 2 == 0 ) replacement.first else replacement.second ).add( string )
                        replacement
                    }

            val text       = Collections.unmodifiableList( pair.first  )
            val references = Collections.unmodifiableList( pair.second.map { Fragment.forKey( it ) } )

            assert( text.size == (references.size + 1) )

            return Pair(text,references)
        }
    }

    override fun byteSize() : Int
    {
        assert( references.size == (text.size - 1)  )

        return (references.size * refByteLength) + text.fold( 0 ) { length, text -> length + text.length } + 1
    }
}
