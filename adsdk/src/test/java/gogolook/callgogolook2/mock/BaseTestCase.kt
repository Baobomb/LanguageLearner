package gogolook.callgogolook2.mock

import android.content.Context
import org.junit.After
import org.junit.Before
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest

@PrepareForTest(Context::class)
@PowerMockIgnore("javax.*", "org.bouncycastle.*")
open class BaseTestCase {
    protected lateinit var mMockContext: Context

    @Before
    @Throws(Exception::class)
    open fun setUp() {
        // context
        mockApplicationContext()
    }

    @After
    @Throws(Exception::class)
    open fun tearDown() {

    }

    @Throws(Exception::class)
    private fun mockApplicationContext() {
        mMockContext = mock(Context::class.java).apply {
            `when`(packageName).thenReturn("gogolook.callgogolook2")
            `when`(applicationContext).thenReturn(this)
        }
    }
}