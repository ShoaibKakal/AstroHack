package com.kushalsharma.adastra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kushalsharma.adastra.adapter.onBoardingAdapter
import com.kushalsharma.adastra.daos.UserDao
import com.kushalsharma.adastra.modals.OnBoardingItem
import com.kushalsharma.adastra.modals.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 123
    private val TAG = "LogInActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    lateinit var onBoardingAdapter: onBoardingAdapter
    lateinit var itemArrayList: ArrayList<OnBoardingItem>
    private lateinit var onBoardingScreensViewPager: ViewPager2
    private lateinit var indicatorsLL: LinearLayout
    private lateinit var explorationBtn: MaterialButton
    private lateinit var signUpButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        itemArrayList = ArrayList()
        onBoardingScreensViewPager = findViewById(R.id.onBoardingScreens_viewPager)
        explorationBtn = findViewById(R.id.exploration_btn)
        indicatorsLL = findViewById(R.id.indicators_LL)
        signUpButton = findViewById(R.id.sign_up_text)
        setupOnBoardingScreen()
        onBoardingAdapter = onBoardingAdapter(itemArrayList)
        onBoardingAdapter.notifyDataSetChanged()
        onBoardingScreensViewPager.adapter = onBoardingAdapter


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth



        explorationBtn.setOnClickListener {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }

        supportActionBar?.hide()
        signUpButton.setOnClickListener {
            gSignClicked(it)
        }


        setCurrentOnboardingIndicator(0)
        onBoardingScreensViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position)
            }
        })

        setupOnboardingIndicator()




    }

    private fun setupOnBoardingScreen() {
        itemArrayList.add(
            OnBoardingItem(
                R.drawable.klipartz,
                "There are two series of dying sunflowers. The first was painted in Paing on the ground."
            )
        )

        itemArrayList.add(
            OnBoardingItem(
                R.drawable.klipartz2,
                "The first day or so we all pointed to our countries. The third or fourth day we were pointing to our continents. By the fifth day we were only aware of warth"
            )
        )



        itemArrayList.add(
            OnBoardingItem(
                R.drawable.glaxay1,
                "THE STARS DON’T LOOK BIGGER, BUT THEY DO LOOK BRIGHTER."
            )
        )

        itemArrayList.add(
            OnBoardingItem(
                R.drawable.klipartz3,
                "THE DREAM IS ALIVE."
            )
        )
    }

    fun gSignClicked(view: View) {
        signIn()

    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        googleSignBtn.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main) {
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {

            val user =
                User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            val usersDao = UserDao()
            usersDao.addUser(user)

            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        } else {
            signUpButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


    private fun setCurrentOnboardingIndicator(position: Int) {
        var childCount = indicatorsLL.childCount

        for (i in 0 until childCount) {
            val imageView: ImageView = indicatorsLL.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.on_boarding_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.on_boarding_indicator_inactive
                    )
                )
            }

        }


    }


    private fun setupOnboardingIndicator() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(5, 0, 5, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.on_boarding_indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            indicatorsLL.addView(indicators[i])
        }
    }

}