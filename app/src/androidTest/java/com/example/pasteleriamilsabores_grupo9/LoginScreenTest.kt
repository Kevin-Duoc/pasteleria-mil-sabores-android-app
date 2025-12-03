
package com.example.pasteleriamilsabores_grupo9

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilsabores_grupo9.ui.login.LoginContent
import com.example.pasteleriamilsabores_grupo9.ui.theme.PasteleriaMilSabores_Grupo9Theme
import com.example.pasteleriamilsabores_grupo9.viewmodel.LoginUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllComponents_usingTestTags() {
        // Arrange
        composeTestRule.setContent {
            PasteleriaMilSabores_Grupo9Theme {
                LoginContent(
                    uiState = LoginUiState(),
                    navController = rememberNavController(),
                    onLoginClick = { _, _ -> }
                )
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("emailField").assertExists()
        composeTestRule.onNodeWithTag("passwordField").assertExists()
        composeTestRule.onNodeWithTag("loginButton").assertExists()
    }

    @Test
    fun loginForm_whenUserInput_callsOnLoginClickWithCorrectData() {
        var clickedEmail = ""
        var clickedPassword = ""

        // 1. Arrange
        composeTestRule.setContent {
            PasteleriaMilSabores_Grupo9Theme {
                LoginContent(
                    uiState = LoginUiState(),
                    navController = rememberNavController(),
                    onLoginClick = { email, password ->
                        clickedEmail = email
                        clickedPassword = password
                    }
                )
            }
        }

        val expectedEmail = "test@example.com"
        val expectedPassword = "password123"

        // 2. Act: Simula la entrada de texto y el clic
        composeTestRule.onNodeWithTag("emailField").performTextInput(expectedEmail)
        composeTestRule.onNodeWithTag("passwordField").performTextInput(expectedPassword)
        composeTestRule.onNodeWithTag("loginButton").performClick()

        // 3. Assert: Verifica que la lambda onLoginClick fue llamada con los datos correctos
        assertEquals("El email enviado no es el correcto", expectedEmail, clickedEmail)
        assertEquals("La contraseña enviada no es la correcta", expectedPassword, clickedPassword)
    }
}
