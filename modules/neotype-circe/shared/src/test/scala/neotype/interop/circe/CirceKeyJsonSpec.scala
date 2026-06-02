package neotype.interop.circe

import io.circe.*
import neotype.interop.circe.given
import neotype.test.*
import neotype.test.definitions.*

// Circe doesn't have a unified key Codec type that's commonly used,
// so we create a stub combining Decoder and Encoder
final case class CirceKeyCodec[A](keyDecoder: KeyDecoder[A], keyEncoder: KeyEncoder[A])

object CirceKeyCodec:
  given [A](using d: KeyDecoder[A], e: KeyEncoder[A]): CirceKeyCodec[A] = CirceKeyCodec(d, e)

object CirceKeyLibrary extends JsonLibrary[CirceKeyCodec]:
  def decode[A](json: String)(using codec: CirceKeyCodec[A]): Either[String, A] =
    codec.keyDecoder.apply(json).toRight("Could not decode key")

  def encode[A](value: A)(using codec: CirceKeyCodec[A]): String =
    codec.keyEncoder(value)

object CirceKeyJsonSpec extends JsonLibrarySpec[CirceKeyCodec]("CirceKey", CirceKeyLibrary):
  override protected def optionalHolderCodec: Option[CirceKeyCodec[OptionalHolder]] = None

  override protected def listHolderCodec: Option[CirceKeyCodec[ListHolder]] =
    None

  override protected def compositeCodec: Option[CirceKeyCodec[Composite]] = None
