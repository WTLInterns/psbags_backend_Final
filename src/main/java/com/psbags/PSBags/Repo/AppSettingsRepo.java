package com.psbags.PSBags.Repo;

import com.psbags.PSBags.Model.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepo extends JpaRepository<AppSettings, Integer> {
}
