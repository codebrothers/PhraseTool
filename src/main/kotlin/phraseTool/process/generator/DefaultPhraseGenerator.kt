package phraseTool.process.generator

import phraseTool.model.PhraseBank

/**
 * Implementation of a Phrase Generator in a style idiomatic to Kotlin/OO languages
 * Works with a data-model representation of a Phrase Bank, phrase Fragments and their replacements.
 */
class DefaultPhraseGenerator(val phraseBank: PhraseBank) : PhraseGenerator
{
    override fun generate() : String
    {
        return ""
    }
}