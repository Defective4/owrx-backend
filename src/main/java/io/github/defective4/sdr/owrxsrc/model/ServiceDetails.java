package io.github.defective4.sdr.owrxsrc.model;

import io.github.defective4.sdr.owrxsrc.template.TemplateField;

public record ServiceDetails(@TemplateField("document_root") String documentRoot,
        @TemplateField("receiver_name") String receiverName,
        @TemplateField("receiver_location") String receiverLocation, String locator,
        @TemplateField("receiver_asl") int asl, @TemplateField("map_type") String mapType,
        @TemplateField("session_timeout") int sessionTimeout, @TemplateField("usage_policy_url") String usagePolicyURL,
        @TemplateField("photo_title") String photoTitle, @TemplateField("photo_desc") String photoDescription) {
    public ServiceDetails(ReceiverDetails details, String documentRoot) {
        this(documentRoot, details.receiverName(), details.receiverLocation(), details.locator(), details.asl(), "osm",
                0, details.usagePolicyURL(), details.photoTitle(), details.photoDescription());
    }
}
