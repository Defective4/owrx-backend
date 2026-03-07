package io.github.defective4.sdr.owrxsrc.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HTMLTemplateManager {
    private final Map<String, String> loadedTemplates = new HashMap<>();

    public String renderTemplate(String template, Map<String, ? extends Object> args) throws IOException {
        String templateContent;
        if (loadedTemplates.containsKey(template)) {
            templateContent = loadedTemplates.get(template);
        } else {
            StringBuilder builder = new StringBuilder();
            try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(template),
                    StandardCharsets.UTF_8)) {
                while (true) {
                    int c = reader.read();
                    if (c < 0) break;
                    builder.append((char) c);
                }
            }
            templateContent = builder.toString();
            loadedTemplates.put(template, templateContent);
        }

        for (Entry<String, ? extends Object> entry : args.entrySet()) templateContent = templateContent
                .replace(String.format("${%s}", entry.getKey()), String.valueOf(entry.getValue()));

        return templateContent;
    }

    public String renderTemplate(String template, Record argObj) throws IOException {
        Map<String, Object> args = new HashMap<>();
        for (Method field : argObj.getClass().getDeclaredMethods()) {
            if (field.getParameterCount() != 0) continue;
            String name = field.isAnnotationPresent(TemplateField.class)
                    ? field.getAnnotation(TemplateField.class).value()
                    : field.getName();
            try {
                args.put(name, field.invoke(argObj));
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
        return renderTemplate(template, args);
    }
}
