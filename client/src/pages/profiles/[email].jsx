import {ProfileDTO} from "../../utils/profiles";
import ProfileForm from '../../components/ProfileForm';
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";

const onFormSubmit = async (profile) => {
    let edited = await profile.updateExistingProfile()
    if(!edited) {
        window.location.replace("/error/" + `Update failed`);
    }
}

export default function Profile() {

    let [isLoading, setIsLoading] = useState(true);
    let [profile, setProfile] = useState(ProfileDTO.empty());
    const {email} = useParams();

    useEffect(() => {
        let f = async () => {
            let profile = await ProfileDTO.fetchProfile(email);
            setProfile(profile)
            setIsLoading(false)
        }
        f()
    }, [email])

    if(isLoading) {
        return <div className={"w-full h-full flex justify-center"}>
            <h1>Loading...</h1>
        </div>
    }

    return <div className={"p-10 m-10"}>
        <div className={"w-full my-5 flex justify-center align-middle"}>
            <h1 className={"text-3xl mx-auto block"}>{email + "'s"} Profile</h1>
        </div>

        <ProfileForm profile={profile} isMailReadOnly={true}
                     submitText={"Update Profile"} onFormSubmit={onFormSubmit}/>

    </div>


}