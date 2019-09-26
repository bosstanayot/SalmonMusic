package tanayot.barjord.musicplayerapplication

import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset

class TestHelper {
        @Throws(Exception::class)
        private fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
            return this.bufferedReader(charset).use { it.readText() }
        }

        @Throws(Exception::class)
        fun getStringFromFile(fileName: String): String {
            val filePath: String = System.getProperty("user.dir") + "/src/test/java/tanayot/barjord/musicplayerapplication/"
            val stream: InputStream = FileInputStream(filePath + fileName)
            val ret: String = stream.readTextAndClose()
            stream.close()
            return ret
        }
}