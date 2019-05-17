package com.github.xelamanster.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import io.circe.{Decoder, Encoder, Printer}
import io.circe.java8.time.{decodeLocalDateTimeWithFormatter, encodeLocalDateTimeWithFormatter}

object JsonUtils {
  object implicits {
    implicit val dateDecoder: Decoder[LocalDateTime] =
      decodeLocalDateTimeWithFormatter(defaultDateFormat)

    implicit val dateEncoder: Encoder[LocalDateTime] =
      encodeLocalDateTimeWithFormatter(defaultDateFormat)
  }

  val defaultDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

  val dropNullValues: Printer = Printer.noSpaces.copy(dropNullValues = true)
}
