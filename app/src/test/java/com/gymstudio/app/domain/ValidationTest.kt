package com.gymstudio.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidationTest {

    @Test
    fun `blank exercise name returns error`() {
        assertEquals(Validation.ERROR_NAME_REQUIRED, Validation.exerciseNameError("   "))
    }

    @Test
    fun `empty exercise name returns error`() {
        assertEquals(Validation.ERROR_NAME_REQUIRED, Validation.exerciseNameError(""))
    }

    @Test
    fun `valid exercise name returns null`() {
        assertNull(Validation.exerciseNameError("Kniebeugen"))
    }

    @Test
    fun `blank plan name returns error`() {
        assertEquals(Validation.ERROR_NAME_REQUIRED, Validation.planNameError(" \t "))
    }

    @Test
    fun `valid plan name returns null`() {
        assertNull(Validation.planNameError("Mein Plan"))
    }

    @Test
    fun `plan without exercises returns error`() {
        assertEquals(Validation.ERROR_PLAN_NEEDS_EXERCISE, Validation.planExerciseError(0))
    }

    @Test
    fun `plan with exercises returns null`() {
        assertNull(Validation.planExerciseError(3))
    }
}
