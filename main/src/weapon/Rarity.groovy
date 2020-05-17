package weapon

/**
 * @author Sasha Price
 */
enum Rarity {
    COMMON('Common'),
    UNCOMMON('Uncommon'),
    RARE('Rare'),
    VERY_RARE('Very Rare'),
    SERAPH('Seraph'),
    LEGENDARY('Legendary'),
    PEARLESCENT('Pearlescent'),
    EFFERVESCENT('Effervescent')

    private static final Map<String, Rarity> NAME_MAP = values().collectEntries { [it.name, it] }.asImmutable()
    private final String name

    Rarity(String name) {
        this.name = name
    }

    @Override
    String toString() {
        name
    }

    static Rarity get(String name) {
        NAME_MAP[name]
    }
}