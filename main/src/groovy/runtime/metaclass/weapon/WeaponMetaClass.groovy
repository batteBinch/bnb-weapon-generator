package groovy.runtime.metaclass.weapon

import weapon.Prefix
import weapon.Weapon

/**
 * This MetaClass delegates method calls of a Weapon such that
 * the method is looked for in the Prefix of the Weapon before
 * its own methods. This allows for dynamic implementing and
 * overriding of methods in a Weapon by placing them in the Prefix.
 *
 * @author Sasha Price
 */
class WeaponMetaClass extends DelegatingMetaClass {

    WeaponMetaClass(MetaClass metaClass) {
        super(metaClass)
    }

    Object invokeMethod(Object object, String methodName, Object[] arguments) {
        if (object instanceof Weapon && methodName != 'getPrefix') {
            Prefix prefix = object.prefix
            if (prefix?.methods?.containsKey(methodName)) {
                return prefix.methods[methodName].rehydrate(object, object, object)(arguments)
            }
        }
        return super.invokeMethod(object, methodName, arguments)
    }

    Object getProperty(Object object, String property) {
        if (object instanceof Weapon && property != 'prefix') {
            String getter = "get${property.charAt(0).toUpperCase()}${property.substring(1)}"
            return object.(getter)()
        }
        return super.getProperty(object, property)
    }
}