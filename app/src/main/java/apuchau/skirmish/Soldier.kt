package apuchau.skirmish

class Soldier {

    override fun hashCode(): Int {
        return 1;
    }

    override fun equals(other: Any?): Boolean {
        return (other != null) && (other is Soldier);
    }
}