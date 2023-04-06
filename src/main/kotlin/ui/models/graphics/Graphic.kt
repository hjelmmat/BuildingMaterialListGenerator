package ui.models.graphics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

class Graphic(val offset: Offset, val size: Size) {
    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is Graphic && offset == other.offset && size == other.size)
    }

    override fun hashCode(): Int {
        return "$offset$size".hashCode()
    }
}