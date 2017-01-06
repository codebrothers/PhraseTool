package phraseTool.model

import java.util.*

data class RawFragment( val key: String, val replacements: Set<String> )

interface ByteSizeable { fun byteSize() : Int }

class Fragment private constructor ( val key : String, var replacements : Set<Replacement>? = null ) : ByteSizeable
{
    companion object
    {
        var fragmentCache = HashMap<String,Fragment>()

        fun getCached( key: String, createNew: (key:String)-> Fragment) : Fragment
        {
            var fragment = fragmentCache[key]
            if( fragment == null )
            {
                fragment = createNew( key )
                fragmentCache[key] = fragment
            }
            return fragment
        }

        fun forRawFragment( rawFragment: RawFragment) : Fragment
        {
            val fragment = getCached( rawFragment.key )
            {
                key -> Fragment( key )
            }

            val replacements = rawFragment.replacements.map( ::Replacement )
            fragment.replacements = HashSet( replacements )

            return fragment
        }

        fun forKey( key: String ) : Fragment
        {
            return getCached( key )
            {
                key -> Fragment( key )
            }
        }
    }

    override fun byteSize() : Int
    {
        return 2 + ( this.replacements?.fold( 0, { length, replacement -> length + replacement.byteSize() } ) ?: 0 )
    }
}

