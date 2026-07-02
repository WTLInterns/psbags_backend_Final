package com.psbags.PSBags.Controller;

import com.psbags.PSBags.Model.AppSettings;
import com.psbags.PSBags.Service.AppSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AppSettingsController {

    private final AppSettingsService appSettingsService;

    // ─── Admin: get full settings ─────────────────────────────────────────────

    @GetMapping("/admin/settings")
    public ResponseEntity<AppSettings> getSettings() {
        return ResponseEntity.ok(appSettingsService.getSettings());
    }

    // ─── Admin: update GST ────────────────────────────────────────────────────

    @PutMapping("/admin/settings/gst")
    public ResponseEntity<AppSettings> updateGst(@RequestBody Map<String, Double> body) {
        Double gst = body.get("gstPercentage");
        if (gst == null) {
            throw new RuntimeException("gstPercentage is required");
        }
        return ResponseEntity.ok(appSettingsService.updateGstPercentage(gst));
    }

    // ─── Admin: update business information ──────────────────────────────────

    @PutMapping("/admin/settings/business")
    public ResponseEntity<AppSettings> updateBusinessInfo(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(appSettingsService.updateBusinessInfo(
                body.get("businessName"),
                body.get("businessEmail"),
                body.get("businessMobile"),
                body.get("businessWhatsapp")
        ));
    }

    // ─── Public: get WhatsApp number only (used by enquiry form) ─────────────

    @GetMapping("/public/settings/whatsapp")
    public ResponseEntity<Map<String, String>> getPublicWhatsapp() {
        AppSettings settings = appSettingsService.getSettings();
        String whatsapp = settings.getBusinessWhatsapp();
        return ResponseEntity.ok(Map.of("businessWhatsapp", whatsapp != null ? whatsapp : ""));
    }
}
