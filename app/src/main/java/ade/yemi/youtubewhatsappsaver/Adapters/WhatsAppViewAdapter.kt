package ade.yemi.youtubewhatsappsaver.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class WhatsAppViewAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    var fragmentlist : ArrayList<Fragment> = ArrayList()
    var fragmentTitle : ArrayList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentlist.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentlist[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }
    fun addfragment(fragment: Fragment, title : String){
        fragmentlist.add(fragment)
        fragmentTitle.add(title)
    }
}