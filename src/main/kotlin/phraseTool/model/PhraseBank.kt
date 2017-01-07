package phraseTool.model

import java.util.*

data class RawPhraseBank( val fragments: Set<RawFragment> )

class PhraseBank
{
    val fragments : Set<Fragment>

    constructor( rawPhraseBank: RawPhraseBank ) : this( HashSet(rawPhraseBank.fragments.map { Fragment.forRawFragment(it) } ) )

    constructor( fragments: Set<Fragment> )
    {
        this.fragments = fragments
    }

    companion object
}

