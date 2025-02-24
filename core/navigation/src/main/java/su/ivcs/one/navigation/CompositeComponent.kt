package su.ivcs.one.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface CompositeComponent : BaseComponent {

    fun getChildStack(): Value<ChildStack<*, BaseComponent>>

    @Composable
    override fun Render() {
        val childStack by getChildStack().subscribeAsState()

        Children(
            stack = childStack,
        ) {
            it.instance.Render()
        }
    }
}
