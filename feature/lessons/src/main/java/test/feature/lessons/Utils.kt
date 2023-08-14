package test.feature.lessons

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import test.core.navigation_api.NavigationApi
import test.feature.lessons.navigation.LessonsDirections


fun Fragment.findDependencies(): NavigationApi<LessonsDirections> {
    return parents.firstOrNull()
        ?: throw IllegalStateException("No Dependencies in ${allParents.joinToString()}")
}

private val Fragment.parents: Iterable<NavigationApi<LessonsDirections>>
    get() = allParents.mapNotNull { it as? NavigationApi<LessonsDirections> }

private val Fragment.allParents: Iterable<Any>
    get() = object : Iterable<Any> {
        override fun iterator() = object : Iterator<Any> {
            private var currentParentFragment: Fragment? = parentFragment
            private var parentActivity: Activity? = activity
            private var parentApplication: Application? = parentActivity?.application

            override fun hasNext() =
                currentParentFragment != null || parentActivity != null || parentApplication != null

            override fun next(): Any {
                currentParentFragment?.let { parent ->
                    currentParentFragment = parent.parentFragment
                    return parent
                }

                parentActivity?.let { parent ->
                    parentActivity = null
                    return parent
                }

                parentApplication?.let { parent ->
                    parentApplication = null
                    return parent
                }

                throw NoSuchElementException()
            }
        }
    }