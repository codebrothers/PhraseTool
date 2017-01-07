
package phraseTool

import phraseTool.model.PhraseBank
import phraseTool.process.io.FileTypeProvider
import phraseTool.process.io.read.BinaryReader
import phraseTool.process.io.read.JSONReader
import phraseTool.process.io.read.PhraseBankReader
import phraseTool.process.io.read.read
import phraseTool.process.io.write.BinaryWriter
import phraseTool.process.io.write.CDataWriter
import phraseTool.process.io.write.PhraseBankWriter
import phraseTool.process.io.write.write
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

val initialKey = "phrase"   // Must be present as a fragment in JSON input file

val readerFactories : Array<()->PhraseBankReader> = arrayOf( ::BinaryReader, ::JSONReader )
val writerFactories : Array<()->PhraseBankWriter> = arrayOf( ::BinaryWriter, ::createCDataWriter )

val readers = readerFactories.map { it() }
val writers = writerFactories.map { it() }

fun readerFor( extension: String ) : PhraseBankReader? { return readers.find { (it as? FileTypeProvider)?.fileExtension.equals( extension, ignoreCase = true ) } }
fun writerFor( extension: String ) : PhraseBankWriter? { return writers.find { (it as? FileTypeProvider)?.fileExtension.equals( extension, ignoreCase = true ) } }

fun main(args: Array<String>)
{
    fun loadPhraseBank() : PhraseBank
    {
        val fileAwareReaders : Collection<FileTypeProvider> = readers.mapNotNull( { reader -> reader as? FileTypeProvider } )
        val file : File = args.getOrNull(0)?.run( ::File ) ?: getUserSelectedFile( title = "Select input PhraseBank", actionName = "Load", fileTypeProviders = fileAwareReaders ) ?: exitProcess(0)
        if( !file.exists() ) exitProcess(1)
        val reader = readerFor( file.extension ) ?: exitProcess(1)
        val inputPath = Paths.get( file.toURI() )
        return reader.read( inputPath )
    }

    fun savePhraseBank( phraseBank: PhraseBank )
    {
        val fileAwareWriters : Collection<FileTypeProvider> = writers.mapNotNull( { reader -> reader as? FileTypeProvider } )
        val file : File = args.getOrNull(1)?.run( ::File ) ?: getUserSelectedFile( title = "Select output file", actionName = "Save", fileTypeProviders = fileAwareWriters ) ?: exitProcess(0)
        val writer = writerFor( file.extension ) ?: exitProcess(1)
        val outputPath = Paths.get( file.toURI() )
        return writer.write( phraseBank, outputPath )
    }

    val phraseBank = loadPhraseBank()
    savePhraseBank( phraseBank )
}

fun getUserSelectedFile( title: String, actionName: String, fileTypeProviders: Collection<FileTypeProvider> ) : File?
{
    val chooser = JFileChooser().apply()
    {
        dialogTitle       = title
        approveButtonText = actionName
    }

    fileTypeProviders.map     { FileNameExtensionFilter( it.fileTypeDescription, it.fileExtension ) }
                     .forEach { chooser.addChoosableFileFilter(it) }

    return if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) chooser.selectedFile else null
}

fun createCDataWriter() : PhraseBankWriter
{
    return CDataWriter( variableName = "phraseBank" )
}