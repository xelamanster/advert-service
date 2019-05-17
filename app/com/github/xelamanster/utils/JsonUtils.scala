package com.github.xelamanster.utils

import io.circe.Printer

object JsonUtils {
  val dropNullValues: Printer = Printer.noSpaces.copy(dropNullValues = true)
}
