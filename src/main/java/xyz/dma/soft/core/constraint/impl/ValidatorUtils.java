package xyz.dma.soft.core.constraint.impl;

class ValidatorUtils {
    private ValidatorUtils() {

    }

    public static String getPath(String path, String field) {
        if(path == null && field == null) {
            return "";
        } else if(path == null) {
            return field;
        } else if(field == null) {
            return path;
        } else {
            return path + "." + field;
        }
    }
}
