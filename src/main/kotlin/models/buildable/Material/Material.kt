package models.buildable.material

interface Material {
    override fun toString(): String
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}