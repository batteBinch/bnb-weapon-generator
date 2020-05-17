package weapon

/**
 * @author Sasha Price
 */
class Prefix {
    String name
    Manufacturer manufacturer
    Map<String, Closure> methods = [:]
}