package nl.tiebe.otarium.logic.home.children.settings.children.ui

import nl.tiebe.otarium.logic.home.children.settings.SettingsComponent

/**
 * Interface for the UI menu of the settings screen.
 */
interface UIChildComponent {
    /**
     * Navigate to the given child.
     */
    fun navigate(child: SettingsComponent.Config)

    /**
     * Get the setting for showing cancelled lessons.
     *
     * @return True if cancelled lessons should be shown, false otherwise.
     */
    fun getShowCancelledLessons(): Boolean

    /**
     * Set the setting for showing cancelled lessons.
     *
     * @param value True if cancelled lessons should be shown, false otherwise.
     */
    fun setShowCancelledLessons(value: Boolean)

    /**
     * Get the setting for marking insufficient grades red. If a grade is lower than `passingGrade`, it will be marked red.
     *
     * @return True if insufficient grades should be marked red, false otherwise.
     */
    fun getMarkInsufficientGradesRed(): Boolean

    /**
     * Set the setting for marking insufficient grades red. If a grade is lower than `passingGrade`, it will be marked red.
     *
     * @param value True if insufficient grades should be marked red, false otherwise.
     */
    fun setMarkInsufficientGradesRed(value: Boolean)

    /**
     * Get the passing grade.
     *
     * @return The passing grade.
     */
    fun getPassingGrade(): String

    /**
     * Set the passing grade.
     *
     * @param value The passing grade. This value should be converted to an integer before being used.
     */
    fun setPassingGrade(value: String)

}