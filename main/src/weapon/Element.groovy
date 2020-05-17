package weapon

/**
 * An enum of the 5 elemental types in Borderlands.
 * @author Sasha Price
 */
enum Element {
    CORROSIVE('Corrosive'),
    CRYOGENIC('Cryogenic'),
    INCENDIARY('Incendiary'),
    RADIATION('Radiation'),
    SHOCK('Shock')

    private static final Map<String, Element> NAME_MAP = values().collectEntries { [it.name, it] }.asImmutable()
    private final String name

    Element(String name) {
        this.name = name
    }

    @Override
    String toString() {
        name
    }

    static Element get(String name) {
        NAME_MAP[name]
    }
}