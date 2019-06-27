package com.sibedge.yokodzun.android.ui.pager

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.jutils.helpers.VariableConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


@SuppressLint("ViewConstructor")
open class Pager(
    context: Context,
    private val pages: List<PagerPage>,
    private val selectedPage: VariableConnector<Int>
) : LinearLayout(
    context
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

    private val pager = ViewPager(context).apply {
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

    private val tabLayout = TabLayout(context).apply {
        setLinearParams(MATCH_PARENT, WRAP_CONTENT)
        setupWithViewPager(pager)
        setBackgroundColor(ColorManager.PRIMARY)
        setTabTextColors(
            ColorManager.BG.mapWithAlpha(0.5f).get(context),
            ColorManager.BG.get(context)
        )
        setSelectedTabIndicatorColor(ColorManager.BG.get(context))
        setSelectedTabIndicatorHeight(dpToPxInt(4))
        setTabRippleColorResource(android.R.color.white)
    }

    init {
        orientation = VERTICAL
        addChild(tabLayout)
        addChild(pager)

        updateFont(tabLayout)
    }

    private fun updateFont(view: ViewGroup) {
        view.forEachChildren { child ->
            when (child) {
                is TextView -> {
                    child.typeface = FontManager.UBUNTU_BOLD.get(context).typeface
                    child.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeManager.TEXT_12.getPx(context))
                }

                is ViewGroup ->
                    updateFont(child)
            }
        }
    }

}