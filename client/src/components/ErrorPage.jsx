import Header from "../components/Header";

export default function ErrorPage() {
    return <>
        <Header/>
        <div className={'w-full h-full flex justify-center'}>
            <h1> Something went awfully wrong...</h1>
        </div>
    </>
}