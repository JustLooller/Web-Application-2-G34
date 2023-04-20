import {Link} from "react-router-dom";

export default function Home() {

    return (
        <>
            <main className={"p-5 flex flex-wrap justify-center  align-middle w-screen h-screen self-center"}>
                <div id="products" className={"h-1/2 md:h-full flex justify-center w-full md:w-1/2 mb-5px text-center"}>
                    <Link className="text-3xl self-center underline" to={"/products/"}> Go to Products </Link>
                </div>
                <div id="accounts" className={"h-1/2 md:h-full flex justify-center w-full md:w-1/2 mb-5px text-center"}>
                    <Link to={"/profiles/"} className="text-3xl underline self-center">
                        Go to Accounts
                    </Link>
                </div>
            </main>
        </>
    )
}