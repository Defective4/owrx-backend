package io.github.defective4.sdr.owrxsrc.model;

import java.net.URL;

import io.github.defective4.sdr.owrxsrc.template.TemplateField;

public record ServiceDetails(@TemplateField("document_root") String documentRoot,
        @TemplateField("receiver_name") String receiverName,
        @TemplateField("receiver_location") String receiverLocation, String locator,
        @TemplateField("receiver_asl") int asl, @TemplateField("map_type") String mapType,
        @TemplateField("session_timeout") int sessionTimeout, @TemplateField("usage_policy_url") URL usagePolicyURL,
        @TemplateField("photo_title") String photoTitle, @TemplateField("photo_desc") String photoDescription) {
}
