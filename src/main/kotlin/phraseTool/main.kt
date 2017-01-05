
package phraseTool

import phraseTool.model.PhraseBank
import phraseTool.process.io.read.JSONPhraseBankReader
import phraseTool.process.io.write.CDataWriter
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser
import kotlin.system.exitProcess

val initialKey     = "phrase"   // Must be present as a fragment in JSON input file
val refByteLength  = 3          // Byte length of a reference ( 1 control char + uint16 offset )
val refByte        = 0x1B       // Control char to use for reference

fun main(args: Array<String>)
{
    val file : File = args.firstOrNull()?.run( ::File ) ?: getFileFromGUI() ?: exitProcess(0)
    if( !file.exists() ) exitProcess(1)

    val inputPath = Paths.get( file.toURI() )
    val phraseBank : PhraseBank = JSONPhraseBankReader().read( inputPath )

    val outputPath = File("phraseBank.h").toPath()
    CDataWriter( variableName = "phraseBank" ).write( phraseBank, outputPath )
}

fun getFileFromGUI() : File?
{
    val chooser = JFileChooser().apply()
    {
        dialogTitle       = "Choose JSON file"
        approveButtonText = "Load"
    }

    return if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) chooser.selectedFile else null
}
