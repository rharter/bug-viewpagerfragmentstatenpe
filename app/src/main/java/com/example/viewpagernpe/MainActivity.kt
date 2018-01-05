package com.example.viewpagernpe

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class MainActivity : AppCompatActivity() {

  private val fragments = SparseArray<Fragment>(3)

  private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
    // TODO The activity caches the fragments for the bottom nav view
    var fragment = fragments[item.itemId]
    if (fragment == null) {
      fragment = when (item.itemId) {
        R.id.navigation_home -> ChildFragment.create(Color.DKGRAY, false)
        R.id.navigation_dashboard -> ParentFragment()
        R.id.navigation_notifications -> ChildFragment.create(Color.WHITE, false)
        else -> return@OnNavigationItemSelectedListener false
      }
      fragments.put(item.itemId, fragment)
    }
    supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    true
  }

  private lateinit var navigation: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    navigation = findViewById(R.id.navigation)
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    toTab(0)
  }

  fun toTab(tab: Int) {
    navigation.selectedItemId = navigation.menu.getItem(tab).itemId
  }
}

class ParentFragment : Fragment() {

  private lateinit var pager: ViewPager
  private lateinit var adapter: Adapter

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater!!.inflate(R.layout.fragment_parent, container, false)
    pager = v.findViewById(R.id.pager)
    return v
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    // TODO Adapter uses the __childFragmentManager__
    adapter = Adapter(childFragmentManager)
    pager.adapter = adapter
  }

  // TODO Issue only appears with FragmentStatePagerAdapter, as it's a state restoration issue
  internal class Adapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val colors = listOf(Color.RED, Color.BLUE, Color.MAGENTA, Color.GREEN)

    override fun getItem(position: Int): Fragment = ChildFragment.create(colors[position])
    override fun getCount(): Int = colors.size
  }
}

class ChildFragment : Fragment() {

  companion object {
    fun create(color: Int, button: Boolean = true) = ChildFragment().apply {
      arguments = Bundle().apply {
        putInt("color", color)
        putBoolean("button", button)
      }
    }
  }

  private lateinit var background: View
  private lateinit var button: Button

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val v = inflater!!.inflate(R.layout.fragment_child, container, false)
    background = v.findViewById(R.id.background)
    button = v.findViewById(R.id.button)
    return v
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val showButton = arguments?.getBoolean("button") ?: true
    if (showButton) {
      button.visibility = View.VISIBLE
      button.setOnClickListener {
        // TODO This is to trigger a **replace** transaction.
        // Possible this is only caused for fragments that are removed by **replace**???
        (activity as MainActivity).toTab(2)
      }
    } else {
      button.visibility = View.GONE
    }

    val color = arguments?.getInt("color") ?: Color.GRAY
    background.setBackgroundColor(color)
  }

}