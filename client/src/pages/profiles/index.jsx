import {useState} from "react";
import Link from "next/link";
import {ProfileDTO} from "@/utils/profiles";

export default function Profiles() {

    let [searchEmail, setSearchEmail] = useState("");

    let [newProfileForm, setNewProfileForm] = useState(ProfileDTO.empty());

    let setProfileField = (e) => {
        setNewProfileForm({
            ...newProfileForm,
            [e.target.name]: e.target.value
        })
    }

    return <>

        <div>
            <input type="email" name="searchEmail" value={searchEmail}
                   onChange={(e) => setSearchEmail(e.target.value)}/>
            <Link href={"/profiles/" + searchEmail}> Cerca </Link>
        </div>

        <div>
            <h1> Crea nuovo profilo </h1>
            <form>
                <label htmlFor={"name"}> Nome </label>
                <input type="text" name="name" value={newProfileForm.name} onChange={setProfileField}/>
                <label htmlFor={"surname"}> Cognome </label>
                <input type="text" name="surname" value={newProfileForm.surname} onChange={setProfileField}/>
                <label htmlFor={"email"}> Email </label>
                <input type="text" name="email" value={newProfileForm.email} onChange={setProfileField}/>
                <label htmlFor={"password"}> Password </label>
                <input type="password" name="password" value={newProfileForm.password} onChange={setProfileField}/>
                <label htmlFor={"passwordConfirm"}> Conferma Password </label>
                <input type="password" name="passwordConfirm" value={newProfileForm.passwordConfirm}
                       onChange={setProfileField}/>
                <label htmlFor={"role"}> Ruolo </label>
                <select name="role" value={newProfileForm.role} onChange={setProfileField}>
                    {Object.keys(roles).map((role) => {
                            return <option key={role} value={role}> {roles[role]} </option>
                        }
                    )}
                </select>
                <button> Crea</button>
            </form>
        </div>

    </>
}