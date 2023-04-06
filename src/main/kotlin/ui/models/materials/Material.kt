package ui.models.materials

class Material(val material: String, val quantity: String) {
    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is Material && material == other.material && quantity == other.quantity)
    }

    override fun hashCode(): Int {
        return "$material$quantity".hashCode()
    }
}