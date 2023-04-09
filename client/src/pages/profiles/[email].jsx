import {ProfileDTO} from "@/utils/profiles";
import ProfileForm from "@/components/ProfileForm";

/**
 *
 * @param props: {profile: ProfileDTO}
 * @returns {JSX.Element}
 * @constructor
 */
export default function Profile(props) {
    console.log(ProfileDTO.fromJson(props.profile))
    let profile = ProfileDTO.fromJson(props.profile)

    return <ProfileForm profile={profile} isReadOnly={false}
                        submitText={"Aggiorna Profilo"} onFormSubmit={(profile) => console.log(profile)}/>


}


export async function getServerSideProps(context) {
    let {email} = context.params;
    let profile = await ProfileDTO.fetchProfile(email);
    return {
        props: {
            profile: JSON.stringify(profile)
        }
    }
}