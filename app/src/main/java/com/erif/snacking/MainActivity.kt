package com.erif.snacking

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.snacking.helper.AdapterRecyclerView
import com.erif.snacking.helper.MainActivityHelper
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var titles: Array<String>
    private lateinit var helper: MainActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titles = arrayOf(
            "Basic",
            "With Icon",
            "With Action",
            "With Margin",
            "Corner Radius",
            "Corner Radius (Custom)",
            "With Border",
            "Background Color",
            "Background Image",
            "Text Color",
            "Text Style",
            "Custom Font Family",
            "Anchor View",
            "Top Position",
            "With State",
            "Message Max Lines",
            "Landscape Screen"
        )

        val parentView: View = findViewById(R.id.parentView)
        val fab = findViewById<ExtendedFloatingActionButton>(R.id.fab)
        helper = MainActivityHelper(parentView, fab)

        val recyclerView = findViewById<RecyclerView>(R.id.act_main_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterRecyclerView(callback())
        recyclerView.adapter = adapter

        val list: MutableList<String> = ArrayList()
        list.addAll(titles)
        adapter.setList(list)
    }

    private fun callback(): AdapterRecyclerView.Callback {
        return object : AdapterRecyclerView.Callback {
            override fun onItemClick(message: String?) {
                when (message) {
                    titles[0] -> { helper.snackBarBasic() }
                    titles[1] -> { helper.snackBarIcon() }
                    titles[2] -> { helper.snackBarAction() }
                    titles[3] -> { helper.snackBarMargin() }
                    titles[4] -> { helper.snackBarCorner() }
                    titles[5] -> { helper.snackBarCornerCustom() }
                    titles[6] -> { helper.snackBarBorder() }
                    titles[7] -> { helper.snackBarBackground() }
                    titles[8] -> { helper.snackBarBackgroundImage() }
                    titles[9] -> { helper.snackBarTextColor() }
                    titles[10] -> { helper.snackBarBold() }
                    titles[11] -> { helper.snackBarFont() }
                    titles[12] -> { helper.snackBarAnchor() }
                    titles[13] -> { helper.snackBarPosition() }
                    titles[14] -> { helper.snackBarState() }
                    titles[15] -> { helper.snackBarMaxLines() }
                    titles[16] -> { helper.snackBarLandscape() }
                }
            }
        }
    }

}