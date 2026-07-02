package com.psbags.PSBags.Service;

import com.psbags.PSBags.Model.AppSettings;
import com.psbags.PSBags.Repo.AppSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final AppSettingsRepo appSettingsRepo;

    public AppSettings getSettings() {
        return appSettingsRepo.findById(1)
                .orElseGet(() -> {
                    AppSettings defaults = new AppSettings();
                    defaults.setId(1);
                    defaults.setGstPercentage(18.0);
                    return appSettingsRepo.save(defaults);
                });
    }

    public double getGstPercentage() {
        return getSettings().getGstPercentage();
    }

    public AppSettings updateGstPercentage(double gstPercentage) {
        if (gstPercentage < 0 || gstPercentage > 100) {
            throw new RuntimeException("GST percentage must be between 0 and 100");
        }
        AppSettings settings = getSettings();
        settings.setGstPercentage(gstPercentage);
        return appSettingsRepo.save(settings);
    }

    public AppSettings updateBusinessInfo(String businessName, String businessEmail,
                                          String businessMobile, String businessWhatsapp) {
        AppSettings settings = getSettings();
        if (businessName != null)     settings.setBusinessName(businessName.trim());
        if (businessEmail != null)    settings.setBusinessEmail(businessEmail.trim());
        if (businessMobile != null)   settings.setBusinessMobile(businessMobile.trim());
        if (businessWhatsapp != null) settings.setBusinessWhatsapp(businessWhatsapp.trim());
        return appSettingsRepo.save(settings);
    }
}
