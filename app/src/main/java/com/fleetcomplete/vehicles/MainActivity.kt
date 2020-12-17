/*
 * Copyright 2019, The Android Open Source Project
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

package com.fleetcomplete.vehicles

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import com.fleetcomplete.vehicles.App.Companion.app
import com.fleetcomplete.vehicles.view.util.KeepStateNavigator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_key_entry.*


class MainActivity : AppCompatActivity() {
    private var navController : NavController? = null
    var menu : Menu? = null

    private val MyPREFERENCES = "MyPrefs"
    private val KEY_FLEET_COMPLETE_API_KEY = "FLEET_COMPLETE_API_KEY"

    private lateinit var binding : View

    @SuppressLint("RestrictedApi", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setBackgroundColor(Color.WHITE)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_refresh_24)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        val controller = navHostFragment.navController


        // setup custom navigator
        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_container)
        controller.navigatorProvider += navigator

        controller.setGraph(R.navigation.home)

        navController = controller

        val prefs: SharedPreferences =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        if(prefs.contains(KEY_FLEET_COMPLETE_API_KEY)){
            app!!.fleetCompleteApiKey = prefs.getString(KEY_FLEET_COMPLETE_API_KEY,null).toString()
        }
    }

    override fun onBackPressed() {
        if(navController?.currentDestination!!.id==R.id.vehiclesListScreen) {
            finish();
        }else{
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_api_key -> {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_key_entry)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            dialog.keyInput.setText(app!!.fleetCompleteApiKey)
            dialog.okBtn.setOnClickListener {
                app!!.fleetCompleteApiKey = dialog.keyInput.text.toString()
                dialog.dismiss()
                val editor: SharedPreferences.Editor =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit()
                editor.putString(KEY_FLEET_COMPLETE_API_KEY, app!!.fleetCompleteApiKey)
                editor.commit()
                onBackPressedDispatcher.onBackPressed()
            }
            dialog.cancelBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
