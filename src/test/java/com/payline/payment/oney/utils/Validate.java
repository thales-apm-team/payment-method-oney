package com.payline.payment.oney.utils;

import com.payline.payment.oney.InvalidRequestException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Validate {


    /**
     * Global validation method.
     * for childObjects, syntax is childObject.fieldName
     * for Map key, syntax is childObject#.keyName or childObject#.fieldName.keyName
     *
     * @param toValidate the object to validate
     * @param ifNull     error message if object is null
     * @param fields     String[] [] field name, error message
     *                   one field per line : [0] = field name and [1] = error message
     * @throws InvalidRequestException if the corresponding field is null
     */
    public static void validate(Object toValidate, String ifNull, String[][] fields) throws InvalidRequestException {

        PluginUtils.requireNonNull(toValidate, ifNull);

        Class clz = toValidate.getClass();
        Object obj;
        Object parent = toValidate;
        Map<String, Object> checkedObject = new HashMap<>();
        String fieldName = "";
        String key;
        try {
            for (String[] fieldNameAndError : fields) {
                if (fieldNameAndError[0].contains(".")) {
                    String[] objNames = fieldNameAndError[0].split("\\.");
                    fieldName = objNames[objNames.length - 1];
                    if (fieldName.contains("#")) {
                        String[] mapKey = objNames[objNames.length - 1].split("#");
                        parent = checkedObject.get(mapKey[0]);
                        key = mapKey[1];
                        if (!(parent instanceof Map<?, ?>)) {
                            throw new InvalidRequestException("Validation failure, \'#\' detected, but "
                                    + parent.getClass().getSimpleName() + " is not a Map<?,?>");
                        }
                        PluginUtils.requireNonNull((Map) parent, key, fieldNameAndError[1]);
                        continue;
                    } else {
                        parent = checkedObject.get(objNames[objNames.length - 2]);
                    }
                } else {
                    fieldName = fieldNameAndError[0];
                    parent = toValidate;
                }
                clz = parent.getClass();
                Field f = clz.getDeclaredField(fieldName);
                if (!Modifier.isPublic(f.getModifiers())) {
                    f.setAccessible(true);
                }
                obj = f.get(parent);
                PluginUtils.requireNonNull(obj, fieldNameAndError[1]);
                checkedObject.put(fieldName, obj);
            }
        } catch (NoSuchFieldException e) {
            throw new InvalidRequestException("Validation failure, field " + fieldName + " not found for class "
                    + clz.getSimpleName());
        } catch (IllegalAccessException e) {
            throw new InvalidRequestException("Validation failure, field " + fieldName + " not readable for class "
                    + clz.getSimpleName());
        }
    }
}
