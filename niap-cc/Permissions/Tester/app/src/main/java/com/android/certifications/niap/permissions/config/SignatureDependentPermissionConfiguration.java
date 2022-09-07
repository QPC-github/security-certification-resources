/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.certifications.niap.permissions.config;

import static com.android.certifications.niap.permissions.utils.SignaturePermissions.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.WindowManager;

import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.R;
import com.android.certifications.niap.permissions.RuntimePermissionTester;
import com.android.certifications.niap.permissions.SignaturePermissionTester;
import com.android.certifications.niap.permissions.utils.SignaturePermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Configuration designed to test signature permissions that are dependent on other permissions. In
 * this case the signature permission under test is RADIO_SCAN_WITHOUT_LOCATION, intended to allow a
 * radio scan to be performed without a location permission granted.
 */
public class SignatureDependentPermissionConfiguration implements TestConfiguration {
    private final Activity mActivity;

    private static final String[] DEPENDENT_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.UWB_RANGING,
    };

    public SignatureDependentPermissionConfiguration(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void preRunSetup() throws BypassConfigException {
        if (areDependentPermissionsGranted(mActivity)) {
            throw new BypassConfigException(
                    mActivity.getResources().getString(R.string.permissions_must_not_be_granted,
                            String.join(", ", DEPENDENT_PERMISSIONS)));
        }
    }

    /**
     * Returns whether the dependent permissions have been granted; in order to run the test in this
     * configuration these permissions must not be granted.
     */
    public static boolean areDependentPermissionsGranted(Activity activity) {
        for (String permission : DEPENDENT_PERMISSIONS) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BasePermissionTester> getPermissionTesters(Activity activity) {

        List<BasePermissionTester> permissionTesters = new ArrayList<>();
        permissionTesters.add(new RuntimePermissionTester(this, activity));
        permissionTesters.add(new SignaturePermissionTester(this, activity));
        return permissionTesters;
    }

    @Override
    public Optional<List<String>> getRuntimePermissions() {
        // The API guarded by UWB_RANGING is also guarded by UWB_PRIVILEGED which is checked first.
        // In this configuration UWB_PRIVILEGED should be granted while UWB_RANGING is not, so this
        // allows verification that this permission is checked and the API fails as expected when
        // it is not granted.
        return Optional.of(Collections.singletonList(Manifest.permission.UWB_RANGING));
    }

    @Override
    public Optional<List<String>> getSignaturePermissions() {
        List<String> permissions = new ArrayList<>();
       // permissions.add(permission.START_ANY_ACTIVITY);
        // Starting in Android 12 the NETWORK_SCAN permission behaves similar to
        // RADIO_SCAN_WITHOUT_LOCATION in that it allows a network scan without a location
        // permission.
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        //    permissions.add(permission.RADIO_SCAN_WITHOUT_LOCATION);
        //    permissions.add(permission.NETWORK_SCAN);
        //}
        permissions.add(permission.ENTER_CAR_MODE_PRIORITIZED);
        //permissions.add(permission.LOCATION_BYPASS);
        //addPermissionsForSV2(permissions);
        //addPermissionsForT(permissions);

        return Optional.of(permissions);
    }

    private void addPermissionsForSV2(List<String> permissions)
    {
        permissions.add(permission.ALLOW_SLIPPERY_TOUCHES);
        permissions.add(permission.TRIGGER_SHELL_PROFCOLLECT_UPLOAD);
    }

    private void addPermissionsForT(List<String> permissions)
    {
            permissions.add(permission.LOCATION_BYPASS);
            permissions.add(permission.CONTROL_AUTOMOTIVE_GNSS);
            permissions.add(permission.MANAGE_WIFI_NETWORK_SELECTION);
            permissions.add(permission.MANAGE_WIFI_INTERFACES);
            permissions.add(permission.TRIGGER_LOST_MODE);
            //Infeasible#permissions.add(permission.START_CROSS_PROFILE_ACTIVITIES);
            permissions.add(permission.QUERY_USERS);
            permissions.add(permission.QUERY_ADMIN_POLICY);
            permissions.add(permission.PROVISION_DEMO_DEVICE);
            permissions.add(permission.REQUEST_COMPANION_PROFILE_APP_STREAMING);
            permissions.add(permission.REQUEST_COMPANION_PROFILE_COMPUTER);
            permissions.add(permission.REQUEST_COMPANION_SELF_MANAGED);
            permissions.add(permission.READ_APP_SPECIFIC_LOCALES);
            permissions.add(permission.USE_ATTESTATION_VERIFICATION_SERVICE);
            //Infeasible#permissions.add(permission.VERIFY_ATTESTATION);
            permissions.add(permission.REQUEST_UNIQUE_ID_ATTESTATION);
            //Infeasible#permissions.add(permission.GET_HISTORICAL_APP_OPS_STATS);
            permissions.add(permission.SET_SYSTEM_AUDIO_CAPTION);
            //Infeasible#permissions.add(permission.INSTALL_DPC_PACKAGES);
            permissions.add(permission.REVOKE_POST_NOTIFICATIONS_WITHOUT_KILL);
            //Infeasible#permissions.add(permission.DELIVER_COMPANION_MESSAGES);
            //Infeasible#permissions.add(permission.MODIFY_TOUCH_MODE_STATE);
            permissions.add(permission.MODIFY_USER_PREFERRED_DISPLAY_MODE);
            permissions.add(permission.ACCESS_ULTRASOUND);
            permissions.add(permission.CALL_AUDIO_INTERCEPTION);
            permissions.add(permission.MANAGE_LOW_POWER_STANDBY);
            permissions.add(permission.ACCESS_BROADCAST_RESPONSE_STATS);
            permissions.add(permission.CHANGE_APP_LAUNCH_TIME_ESTIMATE);
            permissions.add(permission.SET_WALLPAPER_DIM_AMOUNT);
            permissions.add(permission.MANAGE_WEAK_ESCROW_TOKEN);
            permissions.add(permission.START_REVIEW_PERMISSION_DECISIONS);
            //Infeasible#permissions.add(permission.START_VIEW_APP_FEATURES);
            permissions.add(permission.MANAGE_CLOUDSEARCH);
            permissions.add(permission.MANAGE_WALLPAPER_EFFECTS_GENERATION);
            //Infeasible#permissions.add(permission.SUPPRESS_CLIPBOARD_ACCESS_NOTIFICATION);
            //Infeasible#permissions.add(permission.ACCESS_TV_SHARED_FILTER);
            //Infeasible#permissions.add(permission.ADD_ALWAYS_UNLOCKED_DISPLAY);
            permissions.add(permission.SET_GAME_SERVICE);
            permissions.add(permission.ACCESS_FPS_COUNTER);
            permissions.add(permission.MANAGE_GAME_ACTIVITY);
            //Infeasible#permissions.add(permission.LAUNCH_DEVICE_MANAGER_SETUP);
            permissions.add(permission.UPDATE_DEVICE_MANAGEMENT_RESOURCES);
            permissions.add(permission.READ_SAFETY_CENTER_STATUS);
            //Infeasible#permissions.add(permission.SET_UNRESTRICTED_KEEP_CLEAR_AREAS);
            permissions.add(permission.TIS_EXTENSION_INTERFACE);
            //Infeasible#permissions.add(permission.WRITE_SECURITY_LOG);
            permissions.add(permission.MAKE_UID_VISIBLE);

            permissions.add(permission.BIND_ATTESTATION_VERIFICATION_SERVICE);
            permissions.add(permission.BIND_TRACE_REPORT_SERVICE);
            permissions.add(permission.BIND_GAME_SERVICE);
            permissions.add(permission.BIND_SELECTION_TOOLBAR_RENDER_SERVICE);
            permissions.add(permission.BIND_WALLPAPER_EFFECTS_GENERATION_SERVICE);
            permissions.add(permission.BIND_TV_INTERACTIVE_APP);
            permissions.add(permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE);
    }

    @Override
    public int getButtonTextId() {
        return R.string.run_permission_dependent_tests;
    }
}
