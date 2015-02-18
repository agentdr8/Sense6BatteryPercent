package com.dr8.sense6batterypercent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xposed implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	private static String targetpkg = "com.android.systemui";
    private static XSharedPreferences pref;


    @Override
	public void initZygote(StartupParam startupParam) throws Throwable {
        pref = new XSharedPreferences("com.dr8.sense6batterypercent", "com.dr8.sense6batterypercent_preferences");
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(targetpkg)) {
//            Log.d("S6BAT:", "matched com.android.systemui");
            pref.reload();
            BatteryIcons.initHandleLoadPackage(lpparam);
        }
    }

}
