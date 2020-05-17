package weapon

import groovy.text.SimpleTemplateEngine
import groovy.text.TemplateEngine
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

/**
 * @author Sasha Price
 */
class Weapon {
    private static final Parser PARSER = Parser.builder().build()
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build()
    private static final TemplateEngine ENGINE = new SimpleTemplateEngine()
    private final Map<String, ?> properties = [:]

    String name
    Prefix prefix
    int level
    Rarity rarity
    Type type
    Manufacturer manufacturer
    String reloadSpeed
    String range
    String[] damage
    int[] clipSize
    List<String> description

    Weapon() {
        damage = new String[20]
        clipSize = new int[20]
    }

    String getName() {
        prefix != null
                ? "$prefix.name $name"
                : name
    }

    void setLevel(int level) {
        if (level < 1 || level > 20) {
            throw new IllegalArgumentException('level must be between 1 and 20')
        }
        this.level = level
    }

    private static final Set<Rarity> FORBIDDEN_RARITIES = [Rarity.LEGENDARY, Rarity.PEARLESCENT, Rarity.SERAPH, Rarity.EFFERVESCENT]

    void setRarity(Rarity rarity) {
        if (rarity in FORBIDDEN_RARITIES) {
            throw new UnsupportedOperationException("rarity $rarity not yet implemented")
        }
        else if (rarity == null) {
            throw new IllegalArgumentException('rarity must not be null')
        }
        this.rarity = rarity
    }

    void setType(Type type) {
        if (!type) {
            throw new IllegalArgumentException('weaponType must not be null')
        }
        this.type = type
    }

    void setManufacturer(Manufacturer manufacturer) {
        if (!manufacturer) {
            throw new IllegalArgumentException('manufacturer must not be null')
        }
        this.manufacturer = manufacturer
    }

    void setRange(String range) {
        if (range == null) {
            throw new IllegalArgumentException('range must not be null')
        }
        this.range = range
    }

    String getDamage() {
        String result = damage[level - 1]
        if (properties.containsKey('element')) {
            result += ' ' + properties.element
        }
        return result
    }

    void setDamage(String[] damage) {
        if (damage == null) {
            throw new IllegalArgumentException('damage must not be null')
        }
        else if (damage.length != 20) {
            throw new IllegalArgumentException('damage must have 20 elements')
        }
        else if (null in damage) {
            throw new IllegalArgumentException('damage must not contain null')
        }
        this.damage = damage.clone() as String[]
    }

    int getClipSize() {
        manufacturer == Manufacturer.SCAV
                ? 0
                : clipSize[level - 1]
    }

    void setClipSize(int[] clipSize) {
        if (clipSize == null) {
            throw new IllegalArgumentException('clipSize must not be null')
        }
        else if (clipSize.length != 20) {
            throw new IllegalArgumentException('clipSize must have 20 elements')
        }
        System.arraycopy(clipSize, 0, this.clipSize, 0, 20)
    }

    void propertyMissing(String name, Object value) {
        if (value) {
            properties[name] = value
        } else {
            properties.remove(name)
        }
    }

    Object propertyMissing(String name) {
        if (!properties.containsKey(name)) {
            throw new MissingPropertyException(name, Weapon)
        }
        properties[name]
    }
}