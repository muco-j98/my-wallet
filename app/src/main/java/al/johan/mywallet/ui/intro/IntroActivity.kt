package al.johan.mywallet.ui.intro

import al.johan.mywallet.R
import al.johan.mywallet.ui.home.MainActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isSystemBackButtonLocked = true
        isWizardMode = true

        addSlide(AppIntroFragment.newInstance(
                title = "Welcome!",
                description = "Add Positive Transactions By Pressing The + Button",
                imageDrawable = R.drawable.wally_positive_screenshot,
                backgroundColor = resources.getColor(R.color.colorPrimary)
        ))
        addSlide(AppIntroFragment.newInstance(
                description = "Add Negative Transactions By Adding A \"-\" Sign",
                imageDrawable = R.drawable.wally_negative_screenshot,
                backgroundColor = resources.getColor(R.color.colorPrimary)
        ))
        addSlide(AppIntroFragment.newInstance(
                description = "View In-depth Chart For Your Expenses",
                imageDrawable = R.drawable.wally_chart_screen,
                backgroundColor = resources.getColor(R.color.colorPrimary)
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}