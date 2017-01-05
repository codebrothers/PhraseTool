package phraseTool.model

import phraseTool.model.ByteSizeable
import phraseTool.model.Fragment
import phraseTool.refByteLength
import java.util.*

/**
 * Created by Chris on 04/01/2017.
 */
class Replacement( string: String ) : ByteSizeable
{
    val text       : List<String>
    val references : List<Fragment>

    init
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
