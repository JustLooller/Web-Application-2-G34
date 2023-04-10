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

    return <div className={"p-10 m-10"}>
        <div className={"w-full my-5 flex justify-center align-middle"}>
            <h1 className={"text-3xl mx-auto block"}> Profilo di {profile.email} </h1>
        </div>

        <ProfileForm profile={profile} isMailReadOnly={true}
                     submitText={"Aggiorna Profilo"} onFormSubmit={(profile) => console.log(profile)}/>

    </div>


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