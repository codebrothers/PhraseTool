package phraseTool.process.generator

/**
 * Implementation of a Phrase Generator in an embedded style.
 * Works directly with the binary data produced by BinaryPhraseBankWriter
 * It's purpose is to prototype a generation algorithm which can be more easily ported to C for use on a Microcontroller.
 */
class BinaryPhraseGenerator( val phraseBankData: ByteArray ) : PhraseGenerator
{
    override fun generate() : String
    {
        return ""
    }
}