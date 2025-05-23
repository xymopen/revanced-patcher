package app.revanced.patcher.patch.options

import app.revanced.patcher.patch.Patch
import kotlin.reflect.KProperty

/**
 * A [Patch] option.
 *
 * @param key The identifier.
 * @param default The default value.
 * @param values The set of guaranteed valid values identified by their string representation.
 * @param title The title.
 * @param description A description.
 * @param required Whether the option is required.
 * @param valueType The type of the option value (to handle type erasure).
 * @param validator The function to validate the option value.
 * @param T The value type of the option.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
open class PatchOption<T>(
    val key: String,
    val default: T?,
    val values: Map<String, T?>?,
    val title: String?,
    val description: String?,
    val required: Boolean,
    val valueType: String,
    val validator: PatchOption<T>.(T?) -> Boolean,
) {
    /**
     * The value of the [PatchOption].
     */
    var value: T?
        /**
         * Set the value of the [PatchOption].
         *
         * @param value The value to set.
         *
         * @throws PatchOptionException.ValueRequiredException If the value is required but null.
         * @throws PatchOptionException.ValueValidationException If the value is invalid.
         */
        set(value) {
            assertRequiredButNotNull(value)
            assertValid(value)

            uncheckedValue = value
        }

        /**
         * Get the value of the [PatchOption].
         *
         * @return The value.
         *
         * @throws PatchOptionException.ValueRequiredException If the value is required but null.
         * @throws PatchOptionException.ValueValidationException If the value is invalid.
         */
        get() {
            assertRequiredButNotNull(uncheckedValue)
            assertValid(uncheckedValue)

            return uncheckedValue
        }

    // The unchecked value is used to allow setting the value without validation.
    private var uncheckedValue = default

    /**
     * Reset the [PatchOption] to its default value.
     * Override this method if you need to mutate the value instead of replacing it.
     */
    open fun reset() {
        uncheckedValue = default
    }

    private fun assertRequiredButNotNull(value: T?) {
        if (required && value == null) throw PatchOptionException.ValueRequiredException(this)
    }

    private fun assertValid(value: T?) {
        if (!validator(value)) throw PatchOptionException.ValueValidationException(value, this)
    }


    override fun toString() = value.toString()

    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ) = value

    operator fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T?,
    ) {
        this.value = value
    }

    @Suppress("unused")
    companion object PatchExtensions {
        /**
         * Create a new [PatchOption] with a string value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.stringPatchOption(
            key: String,
            default: String? = null,
            values: Map<String, String?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<String>.(String?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "String",
            validator,
        )

        /**
         * Create a new [PatchOption] with an integer value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.intPatchOption(
            key: String,
            default: Int? = null,
            values: Map<String, Int?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Int?>.(Int?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "Int",
            validator,
        )

        /**
         * Create a new [PatchOption] with a boolean value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.booleanPatchOption(
            key: String,
            default: Boolean? = null,
            values: Map<String, Boolean?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Boolean?>.(Boolean?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "Boolean",
            validator,
        )

        /**
         * Create a new [PatchOption] with a float value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.floatPatchOption(
            key: String,
            default: Float? = null,
            values: Map<String, Float?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Float?>.(Float?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "Float",
            validator,
        )

        /**
         * Create a new [PatchOption] with a long value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.longPatchOption(
            key: String,
            default: Long? = null,
            values: Map<String, Long?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Long?>.(Long?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "Long",
            validator,
        )

        /**
         * Create a new [PatchOption] with a string array value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.stringArrayPatchOption(
            key: String,
            default: Array<String>? = null,
            values: Map<String, Array<String>?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Array<String>?>.(Array<String>?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "StringArray",
            validator,
        )

        /**
         * Create a new [PatchOption] with an integer array value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.intArrayPatchOption(
            key: String,
            default: Array<Int>? = null,
            values: Map<String, Array<Int>?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Array<Int>?>.(Array<Int>?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "IntArray",
            validator,
        )

        /**
         * Create a new [PatchOption] with a boolean array value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.booleanArrayPatchOption(
            key: String,
            default: Array<Boolean>? = null,
            values: Map<String, Array<Boolean>?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Array<Boolean>?>.(Array<Boolean>?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "BooleanArray",
            validator,
        )

        /**
         * Create a new [PatchOption] with a float array value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.floatArrayPatchOption(
            key: String,
            default: Array<Float>? = null,
            values: Map<String, Array<Float>?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Array<Float>?>.(Array<Float>?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "FloatArray",
            validator,
        )

        /**
         * Create a new [PatchOption] with a long array value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>> P.longArrayPatchOption(
            key: String,
            default: Array<Long>? = null,
            values: Map<String, Array<Long>?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            validator: PatchOption<Array<Long>?>.(Array<Long>?) -> Boolean = { true },
        ) = registerNewPatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            "LongArray",
            validator,
        )

        /**
         * Create a new [PatchOption] with a string set value and add it to the current [Patch].
         *
         * @param key The identifier.
         * @param default The default value.
         * @param values The set of guaranteed valid values identified by their string representation.
         * @param title The title.
         * @param description A description.
         * @param required Whether the option is required.
         * @param valueType The type of the option value (to handle type erasure).
         * @param validator The function to validate the option value.
         *
         * @return The created [PatchOption].
         *
         * @see PatchOption
         */
        fun <P : Patch<*>, T> P.registerNewPatchOption(
            key: String,
            default: T? = null,
            values: Map<String, T?>? = null,
            title: String? = null,
            description: String? = null,
            required: Boolean = false,
            valueType: String,
            validator: PatchOption<T>.(T?) -> Boolean = { true },
        ) = PatchOption(
            key,
            default,
            values,
            title,
            description,
            required,
            valueType,
            validator,
        ).also(options::register)
    }
}
