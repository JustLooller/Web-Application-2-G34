import React from 'react';
import {useForm} from 'react-hook-form';

const fields = [
    {
        label: "Name",
        name: "name",
        type: "text",
        inputMode: "text",
        options: {
            required: "Required",
        }
    },
    {
        label: "Email",
        name: "email",
        type: "email",
        inputMode: "email",
        options: {
            required: "Required",
            pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: "Invalid email address"
            }
        }

    },
    {
        label: "Age",
        name: "age",
        type: "text",
        inputMode: "numeric",
        options: {
            pattern: {
                value: /^[0-9]+$/i,
                message: "Invalid input"
            },
            required: "Required",
        }
    }
]

export default function ProfileForm({profile, onFormSubmit, submitText, isMailReadOnly}) {
    const {register, handleSubmit, formState: {errors}} = useForm({
        values: profile,
        delayError: 1000,
        reValidateMode: "onChange",
        mode: "onTouched"
    });


    return (
        <form onSubmit={handleSubmit(onFormSubmit)} className={"flex flex-wrap -mx-3 mb-6 w-full justify-center"}>
            {fields.map((field) => (
                <div key={field.name} className={"w-full min-w-fit max-w-xs px-3 mb-6 md:mb-4"}>
                    <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                           htmlFor={field.name}>
                        {field.label}
                    </label>
                    <input id={field.name} inputMode={field.inputMode} type={field.type} readOnly={isMailReadOnly && field.name === "email"}
                           className="appearance-none block w-full text-gray-700 border rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white"
                           {...register(field.name, field.options)}/>
                    {errors[field.name] !== undefined &&
                        <p className="text-red-500 text-xs italic">{errors[field.name].message}</p>}
                </div>
            ))
            }
            <div className={"w-full m-auto justify-center flex"}>
                <input
                    className={"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"}
                    type="submit" value={submitText}/>
            </div>
        </form>
    );
}