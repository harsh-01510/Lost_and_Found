package com.example.lostfound

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }


        // SEARCH ICON ANIMATION

        val imgSearch =
            findViewById<ImageView>(R.id.imgSearch)

        val moveX =
            android.animation.ObjectAnimator.ofFloat(
                imgSearch,
                "translationX",
                0f,
                20f,
                0f,
                -20f,
                0f
            )

        val moveY =
            android.animation.ObjectAnimator.ofFloat(
                imgSearch,
                "translationY",
                -15f,
                0f,
                15f,
                0f,
                -15f
            )

        moveX.duration = 2000
        moveY.duration = 2000

        moveX.repeatCount =
            android.animation.ValueAnimator.INFINITE

        moveY.repeatCount =
            android.animation.ValueAnimator.INFINITE

        moveX.interpolator =
            android.view.animation.AccelerateDecelerateInterpolator()

        moveY.interpolator =
            android.view.animation.AccelerateDecelerateInterpolator()

        val animatorSet =
            android.animation.AnimatorSet()

        animatorSet.playTogether(
            moveX,
            moveY
        )

        animatorSet.start()

        imgSearch.elevation = 12f


        // LOST BUTTON

        val btnLost =
            findViewById<Button>(R.id.btnLost)

        btnLost.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(view: View?) {

                    val intent =
                        Intent(
                            this@MainActivity,
                            LostItemActivity::class.java
                        )

                    startActivity(intent)
                }
            }
        )


        // FOUND BUTTON

        val btnFound =
            findViewById<Button>(R.id.btnFound)

        btnFound.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(view: View?) {

                    val intent =
                        Intent(
                            this@MainActivity,
                            FoundItemActivity::class.java
                        )

                    startActivity(intent)
                }
            }
        )


        // VIEW BUTTON

        val btnView =
            findViewById<Button>(R.id.btnView)

        btnView.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(view: View?) {

                    val intent =
                        Intent(
                            this@MainActivity,
                            ViewItemsActivity::class.java
                        )

                    startActivity(intent)
                }
            }
        )
    }


    // UPDATE DASHBOARD

    override fun onResume() {

        super.onResume()

        val databaseHelper =
            DatabaseHelper(this)

        val itemList =
            databaseHelper.getAllItems()

        var lostCount = 0
        var foundCount = 0

        for (item in itemList) {

            if (item.type == "Lost") {

                lostCount++

            } else if (item.type == "Found") {

                foundCount++
            }
        }

        val totalCount =
            itemList.size


        val txtTotal =
            findViewById<TextView>(
                R.id.txtTotal
            )

        val txtLost =
            findViewById<TextView>(
                R.id.txtLost
            )

        val txtFound =
            findViewById<TextView>(
                R.id.txtFound
            )


        txtTotal.text =
            totalCount.toString()

        txtLost.text =
            lostCount.toString()

        txtFound.text =
            foundCount.toString()
    }
}