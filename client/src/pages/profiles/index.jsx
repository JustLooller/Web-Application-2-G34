import React, {useMemo, useState} from "react";
import {ProfileDTO} from "../../utils/profiles";
import ProfileForm from "../../components/ProfileForm";
import Header from "../../components/Header";
import {Link} from "react-router-dom";
const onFormSubmit = async (profile) => {
    try{
        await profile.insertNewProfile();
        window.location.replace("/profiles/" + profile.email);
    }
    catch (e) {
        console.log();
        window.location.replace("/error/" + e.message);
    }
}

export default function Profiles() {

    let [searchEmail, setSearchEmail] = useState("");


    const MemoizedProfileForm = useMemo(() => <ProfileForm profile={ProfileDTO.empty()} isReadOnly={false}
                                                           submitText={"Create new Profile"}
                                                           onFormSubmit={onFormSubmit}/>, []);

    return <>
        <Header/>
        <div className="grid grid-cols-1 p-10">
            <div className="flex justify-center flex-col pb-10">
                <h1 className="self-center text-3xl"> Search </h1>

                <div className="py-2 self-center justify-center">
                    <input
                        className="rounded-md bg-gray-100 py-2 px-2 shadow-md focus:outline-none active:bg-gray-200 focus:ring focus:ring-gray-400 mr-2"
                        type="email" name="searchEmail" value={searchEmail}
                        onChange={(e) => setSearchEmail(e.target.value)}/>
                    <Link
                        className="bg-gray-100 rounded-md shadow-md px-2 py-2 mr-10 mt-5 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400"
                        to={"/profiles/" + searchEmail}> Search </Link>
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
    </>
}