package monitoring

import java.io.File

object LogProcessExceptions {
  class DiskError(msg: String) extends Error(msg: String) with Serializable
  class CorruptedFileException(msg: String, file: File) extends Exception(msg) with Serializable
  class DbNodeDownException(msg: String) extends Exception(msg) with Serializable
}
