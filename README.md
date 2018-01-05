# ViewPagerNPE

This repo demonstrates a bug that's been [asked](https://stackoverflow.com/questions/18642890/fragmentstatepageradapter-with-childfragmentmanager-fragmentmanagerimpl-getfra) [about](https://stackoverflow.com/questions/11097091/android-app-crashing-after-a-while-using-fragments-and-viewpager) on SO a bit.

The issue is an NPE when trying to use a `FragmentStatePagerAdapter` with a `childFragmentManager`, and using `FragmentTransaction.replace` operations.
 
## To Reproduce

1. Launch the app, you'll see a bottom nav bar and ugly fragment.
2. Select the middle nav item ("Dashboard")
3. This is a ViewPager. Swipe across some views, then tap "Go".
4. The app navigated you to the third tab, using `FragmentTransaction.replace()`
5. Tap "Dashboard" to return to the view pager. 
6. 💥

## The Crash

Here's an example of the crash:

```
java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object android.util.SparseArray.get(int)' on a null object reference
  at android.support.v4.app.FragmentManagerImpl.getFragment(FragmentManager.java:902)
  at android.support.v4.app.FragmentStatePagerAdapter.restoreState(FragmentStatePagerAdapter.java:216)
  at android.support.v4.view.ViewPager.onRestoreInstanceState(ViewPager.java:1453)
  at android.view.View.dispatchRestoreInstanceState(View.java:17642)
  at android.view.ViewGroup.dispatchRestoreInstanceState(ViewGroup.java:3723)
  at android.view.ViewGroup.dispatchRestoreInstanceState(ViewGroup.java:3729)
  at android.view.View.restoreHierarchyState(View.java:17620)
  at android.support.v4.app.Fragment.restoreViewState(Fragment.java:510)
  at android.support.v4.app.FragmentManagerImpl.moveToState(FragmentManager.java:1445)
  at android.support.v4.app.FragmentManagerImpl.moveFragmentToExpectedState(FragmentManager.java:1740)
  at android.support.v4.app.FragmentManagerImpl.moveToState(FragmentManager.java:1809)
  at android.support.v4.app.BackStackRecord.executeOps(BackStackRecord.java:799)
  at android.support.v4.app.FragmentManagerImpl.executeOps(FragmentManager.java:2580)
  at android.support.v4.app.FragmentManagerImpl.executeOpsTogether(FragmentManager.java:2367)
  at android.support.v4.app.FragmentManagerImpl.removeRedundantOperationsAndExecute(FragmentManager.java:2322)
  at android.support.v4.app.FragmentManagerImpl.execPendingActions(FragmentManager.java:2229)
  at android.support.v4.app.FragmentManagerImpl$1.run(FragmentManager.java:700)
  at android.os.Handler.handleCallback(Handler.java:789)
  at android.os.Handler.dispatchMessage(Handler.java:98)
  at android.os.Looper.loop(Looper.java:164)
  at android.app.ActivityThread.main(ActivityThread.java:6541)
  at java.lang.reflect.Method.invoke(Native Method)
  at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:240)
  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:767)

```
