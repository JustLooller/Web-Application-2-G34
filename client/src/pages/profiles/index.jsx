import {useMemo, useState} from "react";
import Link from "next/link";
import {ProfileDTO} from "@/utils/profiles";
import ProfileForm from "@/components/ProfileForm";

export default function Profiles() {

    let [searchEmail, setSearchEmail] = useState("");

    const MemoizedProfileForm = useMemo(() => <ProfileForm profile={ProfileDTO.empty()} isReadOnly={false}
                                                    submitText={"Crea Profilo"} onFormSubmit={(profile) => console.log(profile)}/>, []);

    return <div className="grid grid-cols-1 p-10">

        <div className="flex justify-center flex-col pb-10">
            <h1 className="self-center text-3xl"> Cerca un profilo esistente </h1>

            <div className="py-2 flex self-center justify-center">
                <input className="rounded-xl" type="email" name="searchEmail" value={searchEmail}
                       onChange={(e) => setSearchEmail(e.target.value)}/>
                <Link className="block bg-gray-100 max-w-fit self-center py-2 px-1 mx-2 rounded-md"
                      href={"/profiles/" + searchEmail}> Cerca il Profilo </Link>
            </div>
        </div>
        <hr className="py-2"/>
        <div className="flex flex-col justify-center align-center">
            <h1 className="self-center text-3xl mb-3"> Crea nuovo profilo </h1>
            <div className={"w-2/3 m-auto"}>

            {MemoizedProfileForm}
            </div>
        </div>

    </div>
}