package weapon

enum Type {
    ASSAULT_RIFLE('Assault Rifle'),
    PISTOL('Pistol'),
    ROCKET_LAUNCHER('Rocket Launcher'),
    SHOTGUN('Shotgun'),
    SNIPER_RIFLE('Sniper Rifle'),
    SUBMACHINE_GUN('Submachine Gun')

    static final Map<String, Type> NAME_MAP = values().collectEntries { [it.name, it] }.asImmutable()
    private final String name

    Type(String name) {
        this.name = name
    }

    @Override
    String toString() {
        name
    }

    static Type get(String name) {
        NAME_MAP[name]
    }
}