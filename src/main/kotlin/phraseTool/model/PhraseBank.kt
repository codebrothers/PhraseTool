package phraseTool.model

import java.util.*

data class RawPhraseBank( val fragments: Set<RawFragment> )

class PhraseBank( rawPhraseBank: RawPhraseBank )
{
    val fragments : Set<Fragment> = HashSet(rawPhraseBank.fragments.map { Fragment.forRawFragment(it) } )
}

