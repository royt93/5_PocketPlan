package com.fptflash.noteios.data.settings.subCategories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fptflash.noteios.App
import com.fptflash.noteios.R
import com.fptflash.noteios.databinding.FSettingsAboutBinding

class SettingsAboutFr : Fragment() {

    private var _fragmentSettingsAboutBinding: FSettingsAboutBinding? = null
    private val fragmentSettingsAboutBinding get() = _fragmentSettingsAboutBinding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragmentSettingsAboutBinding = FSettingsAboutBinding.inflate(inflater, container, false)

        fragmentSettingsAboutBinding.clGithubLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aboutSrcLink)))
            startActivity(intent)
        }

        fragmentSettingsAboutBinding.clSupportThisProject.setOnClickListener {
        }

        fragmentSettingsAboutBinding.tvStudioMail.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/mail"
            //shareIntent.flags = Intent.CATEGORY_APP_EMAIL
            activity?.startActivity(Intent.createChooser(shareIntent, "Send Mail"))
        }

        //display current version name
        val versionString = "v " + App.instance.packageManager.getPackageInfo(App.instance.packageName, 0).versionName
        fragmentSettingsAboutBinding.tvVersionAbout.text = versionString
        return fragmentSettingsAboutBinding.root
    }

}
