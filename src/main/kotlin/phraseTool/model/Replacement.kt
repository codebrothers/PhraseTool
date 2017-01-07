package phraseTool.model

import phraseTool.process.io.refByteLength
import java.util.*

/**
 * Created by Chris on 04/01/2017.
 */
class Replacement : ByteSizeable
{
    val text       : List<String>
    val references : List<Fragment>

    constructor()
    {
        text       = ArrayList()
        references = ArrayList()
    }

    constructor( string: String )
    {
        val pair = string
                .split( "{", "}" )
                .foldIndexed( Pair( ArrayList<String>(), ArrayList<String>() ) )
                {
                    i, replacement, string ->
                    ( if ( i % 2 == 0 ) replacement.first else replacement.second ).add( string )
                    replacement
                }

        text       = Collections.unmodifiableList( pair.first  )
        references = Collections.unmodifiableList( pair.second.map { Fragment.forKey( it ) } )

        assert( text.size == (references.size + 1) )
    }

    override fun byteSize() : Int
    {
        return (references.size * refByteLength) + text.fold( 0 ) { length, text -> length + text.length }
    }
}
