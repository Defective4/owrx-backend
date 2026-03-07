package io.github.defective4.sdr.owrxsrc.template;

import java.io.IOException;
import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.FileRenderer;

public class HTMLTemplateRenderer implements FileRenderer {

    private final HTMLTemplateManager templateManager;

    public HTMLTemplateRenderer(HTMLTemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    @Override
    public String render(String arg0, Map<String, ? extends Object> arg1, Context ctx) {
        try {
            return templateManager.renderTemplate(arg0, arg1);
        } catch (IOException e) {
            ctx.status(HttpStatus.NOT_FOUND);
            return arg0 + " not found";
        }
    }

}
