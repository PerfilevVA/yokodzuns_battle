package com.sibedge.yokodzun.android.ui.pager

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.jutils.helpers.VariableConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.utils.ContextConnector.context


@SuppressLint("ViewConstructor")
open class Pager(
    private val context: Context,
    private val pages: List<PagerPage>,
    private val selectedPage: VariableConnector<Int>
) {

    private val adapter = object : PagerAdapter() {

        override fun isViewFromObject(view: View, any: Any) =
            view == any

        override fun getCount() =
            pages.size

        override fun instantiateItem(container: ViewGroup, position: Int) =
            pages[position].viewCreator.invoke().apply(container::addView)

        override fun destroyItem(container: ViewGroup, position: Int, any: Any) =
            container.removeView(any as View)

        override fun getPageTitle(position: Int) =
            pages[position].title.get(context)

    }

    val pager = ViewPager(context).apply {
        setLinearParams(MATCH_PARENT, 0, 1f)
        adapter = this@Pager.adapter
        setCurrentItem(selectedPage.value)

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(currentPage: Int) {
                selectedPage.value = currentPage
            }
        })
    }

    val tabLayout = TabLayout(context).apply {
        setLinearParams(MATCH_PARENT, WRAP_CONTENT)
        setupWithViewPager(pager)
        setTabTextColors(
            ColorManager.FG.mapWithAlpha(0.75f).get(context),
            ColorManager.FG.get(context)
        )
        setSelectedTabIndicatorColor(ColorManager.FG.get(context))
        setSelectedTabIndicatorHeight(dpToPxInt(3))
        setTabRippleColorResource(android.R.color.white)
        updateFont(this)
    }

    private fun updateFont(view: ViewGroup) {
        view.forEachChildren { child ->
            when (child) {
                is TextView -> {
                    child.typeface = FontManager.UBUNTU.get(context).typeface
                    child.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        SizeManager.TEXT_12.getPx(context)
                    )
                }

                is ViewGroup ->
                    updateFont(child)
            }
        }
    }

}