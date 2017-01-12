package phraseTool.process.generator

import phraseTool.util.readUInt16
import java.util.*
import kotlin.jvm.internal.Ref

/**
 * Implementation of a Phrase Generator in an embedded style.
 * Works directly with the binary data produced by BinaryPhraseBankWriter
 * It's purpose is to prototype a generation algorithm which can be more easily ported to C for use on a Microcontroller.
 */
class BinaryPhraseGenerator( val phraseBankData: ByteArray ) : PhraseGenerator
{
    private var seekRemain : Int = 0


    private val pointerStack = Stack<Int>() // Remember where to return to after jumping to resolve a replacement

    private var state = State.CHOOSE

    enum class State
    {
        CHOOSE,     // Read a two-byte number, being the number of succeeding Fragments that may be chosen from
        SEEK,       // Seeking a chosen Fragment: Advanced through bytes, decrementing seekRemain when null encountered
        TEXT,       // Append text to the output buffer
        REFERENCE   //
    }

    override fun generate() : String
    {
        val stringBuilder = StringBuilder()

        val offset = Ref.IntRef().apply { element = 0 }

        pointerStack.push(0)
        state = State.CHOOSE

        while( !pointerStack.empty() )
        {
            when(state)
            {
                State.CHOOSE ->
                {
                    offset.element = pointerStack.pop()
                    val fragmentCount = phraseBankData.readUInt16( offset )
                    seekRemain = ( Math.random() * fragmentCount.toDouble() ).toInt()
                    state = State.SEEK
                }

                State.SEEK ->
                {
                    val c : Char = phraseBankData[offset.element++]
                }

                State.TEXT ->
                {

                }

                State.REFERENCE ->
                {

                }
            }
        }

        return
    }
}