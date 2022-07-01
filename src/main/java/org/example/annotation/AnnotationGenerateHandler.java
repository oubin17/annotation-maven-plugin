package org.example.annotation;

import javax.validation.constraints.*;
import java.lang.reflect.Field;

/**
 * AnnotationGenerateHandler
 *
 * @author oubin.ob
 * @version : AnnotationGenerateHandler.java v 0.1 2022/7/1 10:23 oubin.ob Exp $$
 */
public class AnnotationGenerateHandler {

    protected static String generateExplain(Field field) {

        StringBuilder stringBuilder = new StringBuilder(256);

        stringBuilder.append("    /**\r\n" +
                "     *\r\n" +
                "     * <div style=\"display:none\">start validator</div>\r\n")
                .append("     * <ul>\r\n");

        NotNull notNull = field.getAnnotation(NotNull.class);
        if (notNull != null) {
            stringBuilder.append("     *     <li>Not Null</li>\r\n");
        }

        Null isNull = field.getAnnotation(Null.class);
        if (isNull != null) {
            stringBuilder.append("     *     <li>Null</li>\r\n");
        }

        NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
        if (notEmpty != null) {
            stringBuilder.append("     *     <li>Not Empty</li>\r\n");
        }

        NotBlank notBlank = field.getAnnotation(NotBlank.class);
        if (notBlank != null) {
            stringBuilder.append("     *     <li>Not Blank</li>\r\n");
        }

        Min min = field.getAnnotation(Min.class);
        if (min != null) {
            stringBuilder.append("     *     <li>value >= ").append(min.value()).append(", message = ").append(min.message()).append("</li>\r\n");
        }

        Max max = field.getAnnotation(Max.class);
        if (max != null) {
            stringBuilder.append("     *     <li>value <= ").append(max.value()).append(", message = ").append(max.message()).append("</li>\r\n");
        }

        stringBuilder.append("     * </ul>\r\n");
        stringBuilder.append("     * <div style=\"display:none\">end validator</div>\r\n");
        stringBuilder.append("     */\r\n");

        return stringBuilder.toString();


    }
}
