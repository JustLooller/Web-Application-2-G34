import Head from 'next/head'
import Link from "next/link";

export default function Home() {

    return (
        <>
            <Head>
                <title>Camurria</title>
                <meta name="description" content="Generated by create next app"/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            <main className={"p-5 flex flex-wrap justify-center  align-middle w-screen h-screen self-center"}>
                <div id="products" className={"h-1/2 md:h-full flex justify-center w-full md:w-1/2 mb-5px text-center"}>
                    <Link className="text-3xl self-center underline" href={"/products/"}> Vai alla sezione Prodotti </Link>
                </div>
                <div id="accounts" className={"h-1/2 md:h-full flex justify-center w-full md:w-1/2 mb-5px text-center"}>
                    <Link href={"/profiles/"} className="text-3xl underline self-center">
                        Vai alla sezione Profili
                    </Link>
                </div>
            </main>
        </>
    )
}
