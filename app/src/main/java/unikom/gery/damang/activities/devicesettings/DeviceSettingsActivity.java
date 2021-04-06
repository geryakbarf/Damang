/*  Copyright (C) 2019-2020 Andreas Shimokawa

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package unikom.gery.damang.activities.devicesettings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unikom.gery.damang.R;
import unikom.gery.damang.activities.AbstractGBActivity;
import unikom.gery.damang.devices.DeviceCoordinator;
import unikom.gery.damang.impl.GBDevice;
import unikom.gery.damang.util.DeviceHelper;


public class DeviceSettingsActivity extends AbstractGBActivity implements
        PreferenceFragmentCompat.OnPreferenceStartScreenCallback {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSettingsActivity.class);

    GBDevice device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        device = getIntent().getParcelableExtra(GBDevice.EXTRA_DEVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        if (savedInstanceState == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(DeviceSpecificSettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                DeviceCoordinator coordinator = DeviceHelper.getInstance().getCoordinator(device);
                int[] supportedSettings = coordinator.getSupportedDeviceSpecificSettings(device);
                if (coordinator.supportsActivityTracking()) {
                    supportedSettings = ArrayUtils.addAll(supportedSettings, R.xml.devicesettings_chartstabs);
                }
                supportedSettings = ArrayUtils.addAll(supportedSettings, R.xml.devicesettings_transliteration);
                fragment = DeviceSpecificSettingsFragment.newInstance(device.getAddress(), supportedSettings);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, fragment, DeviceSpecificSettingsFragment.FRAGMENT_TAG)
                    .commit();

        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen preferenceScreen) {
        DeviceCoordinator coordinator = DeviceHelper.getInstance().getCoordinator(device);
        int[] supportedSettings = coordinator.getSupportedDeviceSpecificSettings(device);
        if (coordinator.supportsActivityTracking()) {
            supportedSettings = ArrayUtils.addAll(supportedSettings, R.xml.devicesettings_chartstabs);
        }

        PreferenceFragmentCompat fragment = DeviceSpecificSettingsFragment.newInstance(device.getAddress(), supportedSettings);
        Bundle args = fragment.getArguments();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, fragment, preferenceScreen.getKey())
                .addToBackStack(preferenceScreen.getKey())
                .commit();
        return true;
    }
}