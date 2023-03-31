import {useEffect} from "react";
import {roles, ProfileDTO} from "@/utils/profiles";

export default function Profile(props) {

    let [isEdited, setIsEdited] = useState(false);

    let [profile, setProfile] = useState(props.profile);

    useEffect(() => {
        setIsEdited(true);
    }, [profile]);

    let setProfileField = (e) => {
        setProfile({
            ...profile,
            [e.target.name]: e.target.value
        })
    }

    return <>
        <h1> Profilo </h1>
        <div>
            <label htmlFor={"name"}> Nome </label>
            <input type={"text"} name={"name"} value={profile.name} onChange={setProfileField}/>
            <label htmlFor={"surname"}> Cognome </label>
            <input type={"text"} name={"surname"} value={profile.surname} onChange={setProfileField}/>
            <label htmlFor={"email"}> Email </label>
            <input type={"text"} name={"email"} value={profile.email} onChange={setProfileField}/>
            <label htmlFor={"role"}> Ruolo </label>
            <select name={"role"} value={profile.role} onChange={setProfileField}>
                {Object.keys(roles).map((role) => {
                        return <option key={role} value={role}> {roles[role]} </option>
                    }
                )}
            </select>
            {isEdited && <button
                onClick={async () => {
                    await saveProfile(profile);
                    setIsEdited(false);
                }
                }> Salva </button>}

        </div>
    </>
}




export async function getServerSideProps(context) {
    let {email} = context.params;
    let profile = await ProfileDTO.fetchProfile(email);
    return {
        props: {
            profile
        }
    }
}