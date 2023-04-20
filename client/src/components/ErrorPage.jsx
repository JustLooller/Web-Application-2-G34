import Header from "../components/Header";
import {useParams} from "react-router-dom";

export default function ErrorPage() {
    const errorMessage = useParams().message;

    if(errorMessage) {
        return <>
            <Header/>
            <div className={'w-full h-full flex justify-center'}>
                <h1> {errorMessage} </h1>
            </div>
        </>
    }

    return <>
        <Header/>
        <div className={'w-full h-full flex justify-center'}>
            <h1> Something went awfully wrong...</h1>
        </div>
    </>
}