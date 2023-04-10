import React from 'react';
import {useForm} from 'react-hook-form';
import {roles} from "@/utils/profiles";

const fields = [
    {
        label: "Nome",
        name: "name",
        type: "text",
        options: {
            required: "Campo obbligatorio",
        }
    },
    {
        label: "Cognome",
        name: "surname",
        type: "text",
        options: {
            required: "Campo obbligatorio",
        }
    },
    {
        label: "Email",
        name: "email",
        type: "email",
        options: {
            required: "Campo obbligatorio",
            pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: "Email non valida"
            }
        }

    },
    {
        label: "Password",
        name: "password",
        type: "password",
        options: {
            required: "Campo obbligatorio",
        }
    },
    {
        label: "Conferma Password",
        name: "passwordConfirm",
        type: "password",
        options: {
            required: "Campo obbligatorio",
            validate: (value, formValue) => value === formValue.password || "Le password non coincidono"
        }
    }
]

export default function ProfileForm({profile, onFormSubmit, submitText, isMailReadOnly}) {
    const {register, handleSubmit, formState: {errors}} = useForm({values: profile, delayError: 1000, reValidateMode: "onChange", mode: "onTouched"});


    return (
        <form onSubmit={handleSubmit(onFormSubmit)} className={"flex flex-wrap -mx-3 mb-6 w-full justify-center"}>
            {fields.map((field) => (
                <div key={field.name} className={"w-full min-w-fit max-w-xs px-3 mb-6 md:mb-4"}>
                    <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                           htmlFor={field.name}>
                        {field.label}
                    </label>
                    <input id={field.name} type={field.type} readOnly={isMailReadOnly && field.name === "email"}
                           className="appearance-none block w-full text-gray-700 border rounded py-3 px-4 leading-tight focus:outline-none focus:bg-white"
                           {...register(field.name, field.options)}/>
                    {errors[field.name] !== undefined &&
                        <p className="text-red-500 text-xs italic">{errors[field.name].message}</p>}
                </div>
            ))
            }
            <div className={"w-full min-w-fit max-w-xs px-3 mb-6 md:mb-4"}>
                <label className="block uppercase tracking-wide text-gray-700 text-xs font-bold mb-2"
                       htmlFor={"role"}>
                    Ruolo
                </label>
                <select
                    className="bg-gray-10 border text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-500" {...register("role", {required: "Campo obbligatorio"})}>
                    {Object.keys(roles).map((role) => {
                        return <option key={role} value={role}>{roles[role]}</option>
                    })}
                </select>
            </div>
            <div className={"w-full m-auto justify-center flex"}>
            <input
                className={"bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"}
                type="submit" value={submitText}/>
            </div>
        </form>
    );
}