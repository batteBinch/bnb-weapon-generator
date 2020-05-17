package application

import groovy.yaml.YamlSlurper
import org.codehaus.groovy.runtime.StringGroovyMethods
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import weapon.*

/**
 * Class for creating random Weapons from the predefined Weapons.
 *
 * @author Sasha Price
 */
class Generator {
    /**
     * Parses markdown text for conversion into HTML.
     */
    private static final Parser PARSER = Parser.builder().build()

    /**
     * Converts parsed markdown text into HTML format.
     */
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build()

    /**
     * Parses text from YAML files into a Closure for dynamic method overriding in Prefix.
     */
    private static final GroovyShell closureConverter = new GroovyShell()

    /**
     * A Map containing all the parsed YAML Weapons grouped by Manufacturer
     */
    private static final Map<Manufacturer, List<Map<String, ?>>> YAML_WEAPONS = [:]

    /**
     * A Map containing all the parsed YAML Prefixes grouped by Manufacturer
     */
    private static final Map<Manufacturer, List<Map<String, ?>>> YAML_PREFIXES = [:]

    /**
     * A List of the fields in a Weapon
     */
    private static final Set<String> WEAPON_FIELDS = ['name', 'type', 'manufacturer', 'reloadSpeed',
                                                      'range', 'damage', 'clipSize', 'description']

    /**
     * A List of the fields in a Prefix
     */
    private static final Set<String> PREFIX_FIELDS = ['name', 'manufacturer']

    /**
     * The random number generator for Weapon generation
     */
    private static final Random RANDOM = new Random()

    /**
     * Determines whether the fields of this class have been initialized
     */
    private static boolean initialized

    /**
     * A class to mix into String which allows for conversion of the names
     * of Weapon enums into their respective enum values.
     */
    private static class EnumConverter {
        private static final Closure DEFAULT_CONVERTER = StringGroovyMethods.&asType
        private static final Set<Class> WEAPON_ENUMS = [Element, Manufacturer, Rarity, Type]

        static Object asType(String self, Class c) {
            c in WEAPON_ENUMS
                    ? c.get(self)
                    : DEFAULT_CONVERTER.call(self, c)
        }
    }

    /**
     * Intentionally left blank to hide the default constructor
     */
    private Generator() {
    }

    /**
     * Generates a random Weapon from the default weapons.
     * @param level the level of the Weapon
     * @param rarity the Rarity of the Weapon
     * @return a random Weapon
     */
    static Weapon generateRandomWeapon(int level, Rarity rarity) {
        if (!initialized) {
            initialize()
        }

        Manufacturer manufacturer = nextItem(Manufacturer.values() as List<Manufacturer>)

        Weapon weapon = extractWeapon(nextItem(YAML_WEAPONS[manufacturer]))
        weapon.level = level
        weapon.rarity = rarity

        Prefix prefix = extractPrefix(nextItem(YAML_PREFIXES[manufacturer]))

        weapon.prefix = prefix

        return weapon
    }

    /**
     * Creates a Weapon from the properties of a YAML Weapon.
     * @param yamlWeapon the parsed properties of the Weapon
     * @return the extracted Weapon
     */
    private static Weapon extractWeapon(Map<String, ?> yamlWeapon) {
        Weapon weapon = new Weapon()
        weapon.name = yamlWeapon.name
        weapon.type = yamlWeapon.type as Type
        weapon.manufacturer = yamlWeapon.manufacturer as Manufacturer
        weapon.reloadSpeed = yamlWeapon.reloadSpeed
        weapon.range = yamlWeapon.range
        weapon.damage = yamlWeapon.damage
        weapon.clipSize = yamlWeapon.clipSize as int[]
        weapon.description = yamlWeapon.description as List<String>

        if (yamlWeapon.containsKey('element')) {
            weapon.element = yamlWeapon.element as Element
        }

        // Gets additional properties from yamlWeapon
        for (String key : yamlWeapon.keySet().removeAll(WEAPON_FIELDS)) {
            weapon.(key) = yamlWeapon[key]
        }
        return weapon
    }

    /**
     * Creates a Prefix from the properties of a YAML Prefix.
     * @param yamlPrefix the parsed properties of the Prefix
     * @return the extracted Weapon
     */
    private static Prefix extractPrefix(Map<String, ?> yamlPrefix) {
        Prefix prefix = new Prefix()
        prefix.name = yamlPrefix.name
        prefix.manufacturer = yamlPrefix.manufacturer as Manufacturer

        // Gets additional properties from yamlPrefix
        for (String key : yamlPrefix.keySet().removeAll(PREFIX_FIELDS)) {
            prefix.methods[key] = closureConverter.evaluate(yamlPrefix[key] as String) as Closure
        }
        return prefix
    }

    /**
     * Picks a random element from a given List.
     * @param items the List to pick from
     * @return a random item from the List
     */
    private static <T> T nextItem(List<T> items) {
        items[RANDOM.nextInt(items.size())]
    }

    /**
     * Uses lazy initialization for faster app startup.
     */
    private static void initialize() {
        String.mixin(EnumConverter)
        for (Manufacturer manufacturer : Manufacturer.values()) {
            String fileName = manufacturer.toString().toLowerCase()

            Reader weaponReader = new FileReader("resources\\weapons\\${fileName}Weapons.yaml")
            Reader prefixReader = new FileReader("resources\\prefixes\\${fileName}Prefixes.yaml")

            YAML_WEAPONS[manufacturer] = (List<Map<String, ?>>) new YamlSlurper().parse(weaponReader)
            YAML_PREFIXES[manufacturer] = (List<Map<String, ?>>) new YamlSlurper().parse(prefixReader)

            weaponReader.close()
            prefixReader.close()
        }
        initialized = true
    }
}