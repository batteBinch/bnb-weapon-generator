package weapon

enum Manufacturer {
    ATLAS('Atlas'),
    DAHL('Dahl'),
    HYPERION('Hyperion'),
    JAKOBS('Jakobs'),
    MALIWAN('Maliwan'),
    SCAV('Scav'),
    TEDIORE('Tediore'),
    TORGUE('Torgue'),
    VLADOF('Vladof')

    private static final Map<String, Manufacturer> NAME_MAP = values().collectEntries { [it.name, it] }.asImmutable()
    private final String name

    Manufacturer (String name) {
        this.name = name
    }

    @Override
    String toString() {
        name
    }

    static Manufacturer get(String name) {
        NAME_MAP[name]
    }
}