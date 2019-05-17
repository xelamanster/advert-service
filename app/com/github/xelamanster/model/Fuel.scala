package com.github.xelamanster.model

import enumeratum._
import enumeratum.EnumEntry._

sealed trait Fuel extends EnumEntry with Lowercase

object Fuel extends Enum[Fuel] with CirceEnum[Fuel]{
  case object Gasoline extends Fuel
  case object Diesel extends Fuel

  val values = findValues

}
