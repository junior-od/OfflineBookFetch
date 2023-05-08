package com.blinkslabs.blinkist.android.challenge.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BooksActivityTest {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<BooksActivity>()

    @Test
    fun testThatOrderIconIsDisplayed() {
        composeTestRule.activityRule.scenario.onActivity {
        }

        composeTestRule.onNodeWithContentDescription("order").assertIsDisplayed()
    }

    @Test
    fun testThatFilterIconIsDisplayed() {
        composeTestRule.activityRule.scenario.onActivity {
        }

        composeTestRule.onNodeWithContentDescription("filter").assertIsDisplayed()
    }

    @Test
    fun testThatOnClickFilterIconDisplaysDialog() {
        composeTestRule.activityRule.scenario.onActivity {
        }

        composeTestRule.onNodeWithContentDescription("filter").performClick()

        composeTestRule.onNodeWithContentDescription("filter dialog").assertIsDisplayed()
    }

    @Test
    fun testThatOnClickFilterIconDisplaysDialogAndOnWeekClickCloseDialog() {
        composeTestRule.activityRule.scenario.onActivity {
        }

        composeTestRule.onNodeWithContentDescription("filter").performClick()

        composeTestRule.onNodeWithContentDescription("filter dialog").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Week").performClick()

        composeTestRule.onNodeWithContentDescription("filter dialog").assertDoesNotExist()
    }

    @Test
    fun testThatOnClickFilterIconDisplaysDialogAndOnAlphabetClickCloseDialog() {
        composeTestRule.activityRule.scenario.onActivity {
        }

        composeTestRule.onNodeWithContentDescription("filter").performClick()

        composeTestRule.onNodeWithContentDescription("filter dialog").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Alphabet").performClick()

        composeTestRule.onNodeWithContentDescription("filter dialog").assertDoesNotExist()
    }
}
