package io.github.defective4.sdr.owrxsrc.model;

import com.google.gson.annotations.SerializedName;

public record ReceiverDetails(@SerializedName("receiver_name") String receiverName,
        @SerializedName("photo_title") String photoTitle, @SerializedName("session_timeout") int sessionTimeout,
        @SerializedName("receiver_asl") int asl, @SerializedName("keep_files") int keepFiles,
        @SerializedName("photo_desc") String photoDescription,
        @SerializedName("receiver_location") String receiverLocation, @SerializedName("receiver_gps") GPS gps,
        @SerializedName("usage_policy_url") String usagePolicyURL, String locator) {
}
