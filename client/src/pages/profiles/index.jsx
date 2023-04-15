import {useMemo, useState} from "react";
import Link from "next/link";
import {ProfileDTO} from "@/utils/profiles";
import ProfileForm from "@/components/ProfileForm";

export default function Profiles() {

    let [searchEmail, setSearchEmail] = useState("");

    const MemoizedProfileForm = useMemo(() => <ProfileForm profile={ProfileDTO.empty()} isReadOnly={false}
                                                           submitText={"Create new Profile"}
                                                           onFormSubmit={(profile) => profile.insertNewProfile()}/>, []);

    return <div className="grid grid-cols-1 p-10">

        <div className="flex justify-center flex-col pb-10">
            <h1 className="self-center text-3xl"> Search </h1>

            <div className="py-2 flex self-center justify-center">
                <input className="rounded-xl" type="email" name="searchEmail" value={searchEmail}
                       onChange={(e) => setSearchEmail(e.target.value)}/>
                <Link
                    className="block bg-blue-200 max-w-fit self-center py-2 px-2 mx-4 rounded-md hover:bg-blue-500 hover:text-white "
                    href={"/profiles/" + searchEmail}> Search </Link>
            </div>
        </div>
        <hr className="py-2"/>
        <div className="flex flex-col justify-center align-center">
            <h1 className="self-center text-3xl mb-3"> Create </h1>
            <div className={"w-2/3 m-auto"}>

                {MemoizedProfileForm}
            </div>
        </div>

    </div>
}