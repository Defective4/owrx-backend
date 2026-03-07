package io.github.defective4.sdr.owrxsrc;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.http.HttpStatus.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import io.github.defective4.sdr.owrxsrc.model.ServiceDetails;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateManager;
import io.github.defective4.sdr.owrxsrc.template.HTMLTemplateRenderer;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class OpenWebRXService {

    private static final String OWRX_RES = "/owrx/htdocs";

    private final AssetCompiler compiler;
    private final Javalin javalin;
    private final ServiceDetails serviceDetails;
    private final HTMLTemplateManager templateManager = new HTMLTemplateManager();

    public OpenWebRXService(ServiceDetails serviceDetails) throws IOException {
        compiler = new AssetCompiler(OWRX_RES);
        javalin = Javalin.create(cfg -> {
            cfg.fileRenderer(new HTMLTemplateRenderer(templateManager));
            cfg.staticFiles.add(scfg -> {
                scfg.location = Location.CLASSPATH;
                scfg.directory = OWRX_RES;
                scfg.hostedPath = "/static";
            });
            cfg.router.apiBuilder(() -> {
                get("/", ctx -> ctx.render(OWRX_RES + "/index.html", Map.of("header",
                        templateManager.renderTemplate(OWRX_RES + "/include/header.include.html", serviceDetails))));
                get("/compiled/{asset}", ctx -> {
                    Optional<String> asset = compiler.getCompiledAsset(ctx.pathParam("asset"));
                    if (asset.isPresent()) {
                        ctx.result(asset.get());
                    } else {
                        ctx.status(NOT_FOUND);
                    }
                });
            });
        });
        this.serviceDetails = serviceDetails;
    }

    public Javalin start(int port) {
        return javalin.start(port);
    }

}
